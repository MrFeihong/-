package com.itheima.health.service;


import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    /**
     * 批量导入设置
     * @param orderSettingList
     */
    void addBatch(List<OrderSetting> orderSettingList)throws MyException;


    /**
     * 按月查询预约设置信息
     * @return
     */
    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    /**
     * 通过日期设置可预约的最大数
     * @param orderSetting
     * @throws MyException
     */
    void editNumberByDate(OrderSetting orderSetting)throws MyException;

}
