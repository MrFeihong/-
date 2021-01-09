package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 调用业务添加
        setmealDao.add(setmeal);
        // 获取套餐Id
        Integer setmealId = setmeal.getId();
        // 遍历checkgroupIds数组
        if (null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                // 添加套餐与检查组关系
                setmealDao.addSetmealCheckGroup(setmealId,checkgroupId);
            }
        }
    }
}
