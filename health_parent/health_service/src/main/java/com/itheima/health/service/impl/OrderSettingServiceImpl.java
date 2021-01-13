package com.itheima.health.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入设置
     * @param orderSettingList
     */
    @Override
    @Transactional
    public void addBatch(List<OrderSetting> orderSettingList) {
        // 判断List<Ordersetting>不为空
        if (!CollectionUtils.isEmpty(orderSettingList)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 遍历导入的预约设置信息List<Ordersetting>
            for (OrderSetting os : orderSettingList) {
                // 通过预约的日期来查询预约设置表，看这个日期的设置信息有没有
                OrderSetting osInDB = orderSettingDao.findByOrderDate(os.getOrderDate());
                // 判断当前日期有没有预约设置
                if (null == osInDB){
                //没有就插入数据
                    orderSettingDao.add(os);
                }else {
                // 有预约设置
                    // 获取预约人数
                    int reservations = osInDB.getReservations();
                    // 获取最大预约数
                    int number = os.getNumber();
                    // 判断已预约人数是否大于要更新的最大预约数
                    if (reservations > number){
                    // 大于则要报错，接口方法 异常声明
                        throw new MyException(sdf.format(os.getOrderDate()) +"：预约人数已满");
                    }else {
                    // 小于，则可以更新已预约人数
                        orderSettingDao.updateNumber(os);
                    }
                }
            }
        }

    }


    /**
     * 按月查询预约设置
     * @return
     */
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        month+="%";
        return orderSettingDao.getOrderSettingByMonth(month);
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 通过预约的日期来查询预约设置表，看这个日期的设置信息有没有
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            // 判断当前日期有没有预约设置
            if (null == osInDB){
                //没有就插入数据
                orderSettingDao.add(orderSetting);
            }else {
                // 有预约设置
                // 获取预约人数
                int reservations = osInDB.getReservations();
                // 获取最大预约数
                int number = orderSetting.getNumber();
                // 判断已预约人数是否大于要更新的最大预约数
                if (reservations > number){
                    // 大于则要报错，接口方法 异常声明
                    throw new MyException(sdf.format(orderSetting.getOrderDate()) +"：预约人数已满");
                }else {
                    // 小于，则可以更新已预约人数
                    orderSettingDao.updateNumber(orderSetting);
                }
            }

    }


}
