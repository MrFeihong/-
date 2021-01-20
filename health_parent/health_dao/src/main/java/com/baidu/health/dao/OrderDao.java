package com.baidu.health.dao;


import com.baidu.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    void add(Order order);
    List<Order> findByCondition(Order order);
    Map findById4Detail(Integer id);
    Integer findOrderCountByDate(String date);
    Integer findOrderCountAfterDate(String date);
    Integer findVisitsCountByDate(String date);
    Integer findVisitsCountAfterDate(String date);
    List<Map<String,Object>> findHotSetmeal();
    /**
     * 通过日期范围统计预约数
     * @param startDate
     * @param endDate
     * @return
     */
    int findOrderCountBetweenDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
