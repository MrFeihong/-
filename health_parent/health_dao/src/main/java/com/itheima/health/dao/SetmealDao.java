package com.itheima.health.dao;

import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

public interface SetmealDao {

    /**
     * 添加套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 添加套餐与检查组关系
     * @param setmealId
     * @param checkgroupId
     */
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

}
