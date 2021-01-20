package com.baidu.health.service;

import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 添加检查项
     * @param checkItem
     */
    void add(CheckItem checkItem) throws BusinessException;

    /**
     * 根据id删除检查项
     * @param id
     */
    void deleteById(int id) throws BusinessException;

    /**
     * 根据id查询检查项回显客户端
     * @param lzyCheckItem
     * @return
     */
    CheckItem findById(CheckItem lzyCheckItem);

    /**
     * 更新检查项
     * @param checkItem
     */
    void update(CheckItem checkItem) throws BusinessException;

    /**
     * 检查项分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);
}
