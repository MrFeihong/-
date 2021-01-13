package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    /**
     * 查询预约日期的设置
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);


    /**
     * 添加预约设置
     * @param os
     */
    void add(OrderSetting os);

    /**
     * 更新已预约人数
     * @param os
     */
    void updateNumber(OrderSetting os);

    /**
     * 按月查询预约设置信息
     * @param month
     * @return
     */
    List<Map<String, Integer>> getOrderSettingByMonth(String month);




}
