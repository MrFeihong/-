package com.baidu.health.dao;

import com.baidu.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    /**
     *根据日期查询是否有该条数据 有则返回该日数据
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     *修改该日的可预约人数
     * @param orderSetting
     */
    void updateNumber(OrderSetting orderSetting);

    /**
     *添加该日的日期和可预约人数
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 通过月份模糊查询当月预约设置信息集合
     * @param month
     * @return
     */
    List<Map<String, Integer>> findOrderSettingByMonth(String month);

    /**
     * 更新已预约人数
     * @param orderSetting
     * @return 受影响的记录数：0：执行不成功，>0代表执行成功
     */
    int editReservationsByOrderDate(OrderSetting orderSetting);

    /**
     * 根据日期删除预约设置
     * @param date
     */
    void cleanOrderSetting(Date date);
}
