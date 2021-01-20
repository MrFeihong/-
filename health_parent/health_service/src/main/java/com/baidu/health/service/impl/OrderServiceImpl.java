package com.baidu.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.dao.MemberDao;
import com.baidu.health.dao.OrderDao;
import com.baidu.health.dao.OrderSettingDao;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.Member;
import com.baidu.health.pojo.Order;
import com.baidu.health.pojo.OrderSetting;
import com.baidu.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 预约提交
     * @param orderInfo
     * @return
     */
    @Override
    @Transactional
    public Order submitOrder(Map<String, String> orderInfo) {
        // 先判断能不能预约
        //1.根据体检日期查询预约设置
        String orderDateStr = orderInfo.get("orderDate");
        // 前端用map接受的是string类型的数据库是data类型的使用需要格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = null;
        try {
            orderDate = sdf.parse(orderDateStr);
        } catch (ParseException e) {
            throw new BusinessException("日期格式不正确");
        }
        // 通过预约日期查询预约设置
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderDate);
        if(null != osInDB) {
            //    1.1数据库中有设置可预约人数
            //       判断预约是否已满
            //       已满：报错，所选日期预约已满，请选其它日期
            int reservations = osInDB.getReservations();// 已预约人数
            int number = osInDB.getNumber();// 最大预约数
            // 判断预约是否已满
            if(reservations >= number){
                // 预约已满
                throw new BusinessException("所选日期预约已满，请选其它日期");
            }
        }else {
            //  1.2 数据库中没设置可预约人数
            //  报错，所选日期不能预约，请选其它日期
            throw new BusinessException("所选日期不能预约，请选其它日期");
        }
        // 在判断有没有注册
        //2. 会员操作
        String telephone = orderInfo.get("telephone");
        //手机号码查询是否存在
        Member member = memberDao.findByTelephone(telephone);
        // 构建订单信息 不管用户有没有存在 先设置好用户从前端传入的信息
        Order order = new Order();
        // 获取从前端传入的套餐id
        String setmealId = orderInfo.get("setmealId");
        // 先设置订单的套餐id
        order.setSetmealId(Integer.valueOf(setmealId));
        // 设置订单预约日期 //orderDate: 前端 已经从String传成了date了
        order.setOrderDate(orderDate);
        //在来判断用户是否存在 不存在则给用户创建
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
            //member_id 查询/添加时获取 添加完需要获取用户的id 从数据库中返回的id 因为订单表需要用户的id
            order.setMemberId(member.getId());
        }else {
            //存在：
            //    则判断是否重复预约
            //    通过套餐id, 会员id, 预约日期
            Integer memberId = member.getId();
            // 先设置好用户存在则设置订单中的用户id
            order.setMemberId(memberId);
            //判断是否重复 三个一起判断是否重复
            List<Order> orderList = orderDao.findByCondition(order);
            //    存在：报错：不能重复预约
            //if(null != orderList && orderList.size() > 0)
            // ！为空则是turn不为空 不为空f取反t
            if(!CollectionUtils.isEmpty(orderList)){
                // 说明有重复的则报错
                throw new BusinessException("不能重复预约");
            }
        }

        //4. 更新已预约人数, 防止超卖，行锁, 更新成功返回1，失败返回0
        int count = orderSettingDao.editReservationsByOrderDate(osInDB);
        if(count == 0){
            throw new BusinessException("所选日期预约已满，请选其它日期");
        }
        //3. 订单表操作 添加预约
        //orderType: 微信预约   /电话 预约,
        //最后在设置完整的订单信息
        order.setOrderType(orderInfo.get("orderType"));
        //    SetmealMobileController 设置为微信预约  health_mobile 给互联的用户使用
        //    SetmealController 设置为电话预约        health_web给企业内部后台使用的
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        //添加订单
        orderDao.add(order);
        //最后返回order进行客户的回显
        return order;
    }

    /**
     * 查询预约成功订单信息
     * @param id
     * @return
     */
    @Override
    public Map<String, String> findDetailById(int id) {
        return orderDao.findById4Detail(id);
    }
}
