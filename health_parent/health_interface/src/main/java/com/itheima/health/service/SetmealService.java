package com.itheima.health.service;

import com.itheima.health.pojo.Setmeal;

public interface SetmealService {
    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

}
