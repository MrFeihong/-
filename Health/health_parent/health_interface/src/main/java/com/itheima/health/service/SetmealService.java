package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Setmeal;

import java.util.List;


public interface SetmealService {
    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);


    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Setmeal findById(int id);

    /**
     * 根据id查询检查组集合
     * @param id
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(int id);

    /**
     * 修改套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds);


    /**
     * 根据id删除套餐
     * @param id
     */
    void deleteById(int id)throws MyException;

    /**
     * 查询所有图片
     * @return
     */
    List<String> findImgs();

    /**
     * 查询所有的套餐
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 通过id查询套餐详情
     * @param id
     * @return
     */
    Setmeal findDetailById(int id);


}
