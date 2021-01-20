package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

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
     *分页查询
     * @param queryString
     * @return
     */
    Page<CheckItem> findByCondition(String queryString);


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
     *统计使用个数
     * @param id
     * @return
     */
    Integer findCountByCheckItemId(Integer id);
    /**
     * 根据id删除
     * @param id
     */
    void deleteById(Integer id);


}
