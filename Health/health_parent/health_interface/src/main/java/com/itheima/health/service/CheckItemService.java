package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();


    /**
     * 添加
     * @param checkItem
     */
    void add(CheckItem checkItem);


    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    CheckItem findById(Integer id);

    /**
     * 修改
     * @param checkItem
     */
    void update(CheckItem checkItem);


    /**
     * 根据id删除
     * @param id
     */
    void deleteById(Integer id)throws MyException;


}
