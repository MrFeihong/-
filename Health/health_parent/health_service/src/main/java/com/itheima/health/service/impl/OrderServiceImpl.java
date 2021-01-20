package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderSettingDao orderSettingDao;


    @Override
    @Transactional
    public Order submitOrder(Map<String, String> orderInfo) {
        //1.根据体检日期查询预约设置
        String orderDateStr = orderInfo.get("orderDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = null;
        try {
            orderDate = sdf.parse(orderDateStr);
        } catch (ParseException e) {
            throw new MyException("日期格式不正确");
        }
        // 通过预约日期查询预约设置
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderDate);
        if(null != osInDB) {
            //    1.1有设置
            //        判断预约是否已满
            //        已满：报错，所选日期预约已满，请选其它日期
            int reservations = osInDB.getReservations();// 已预约人数
            int number = osInDB.getNumber();// 最大预约数
            if(reservations >= number){
                //预约已满
                throw new MyException("所选日期预约已满，请选其它日期");
            }
        }else {
            //    1.2 没设置
            //        报错，所选日期不能预约，请选其它日期
            throw new MyException("所选日期不能预约，请选其它日期");
        }
        //2. 会员操作
        String telephone = orderInfo.get("telephone");
        //手机号码查询是否存在
        Member member = memberDao.findByTelephone(telephone);

        // 构建订单信息
        Order order = new Order();
        String setmealId = orderInfo.get("setmealId");
        // 订单的套餐id
        order.setSetmealId(Integer.valueOf(setmealId));
        // 订单预约日期 //orderDate: 前端
        order.setOrderDate(orderDate);

        if(null == member) {
            //不存在：
            member = new Member();
            //    添加会员 返回主键
            //    会员信息由前端传过来
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setRemark("微信预约自动注册");
            member.setName(orderInfo.get("name"));
            member.setSex(orderInfo.get("sex"));
            member.setIdCard(orderInfo.get("idCard"));
            member.setPassword(telephone.substring(5)); // 默认密码
            memberDao.add(member);
            //member_id 查询/添加时获取
            order.setMemberId(member.getId());
        }else {
            //存在：
            //    判断是否重复预约
            //    通过套餐id, 会员id, 预约日期
            Integer memberId = member.getId();
            order.setMemberId(memberId);
            List<Order> orderList = orderDao.findByCondition(order);
            //    存在：报错：不能重复预约
            //if(null != orderList && orderList.size() > 0)
            if(!CollectionUtils.isEmpty(orderList)){
                throw new MyException("不能重复预约");
            }
        }
        //4. 更新已预约人数, 防止超卖，行锁, 更新成功返回1，失败返回0
        int count = orderSettingDao.editReservationsByOrderDate(osInDB);
        if(count == 0){
            throw new MyException("所选日期预约已满，请选其它日期");
        }
        //3. 订单表操作 添加预约
        //orderType: 微信预约   /电话 预约,
        order.setOrderType(orderInfo.get("orderType"));
        //    SetmealMobileController 设置为微信预约  health_mobile 给互联的用户使用
        //    SetmealController 设置为电话预约        health_web给企业内部后台使用的
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        orderDao.add(order);
        return order;
    }


    @Override
    public Map<String, String> findDetailById(int id) {
        return orderDao.findById4Detail(id);
    }
}
