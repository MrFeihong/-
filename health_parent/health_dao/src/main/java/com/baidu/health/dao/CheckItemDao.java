package com.baidu.health.dao;

import com.baidu.health.pojo.CheckItem;
import com.github.pagehelper.Page;


import java.util.List;

public interface CheckItemDao {
    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 校验完添加检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 根据name查询添加的检查项是否重复
     * @param checkItem
     * @return CheckItem
     */
    CheckItem findByName(CheckItem checkItem);
    /**
     * 根据code查询添加的检查项是否重复
     * @param checkItem
     * @return CheckItem
     */
    CheckItem findByCoed(CheckItem checkItem);

    /**
     * 校验查询检查项是否被检查组调用
     * @param id
     * @return
     */
    int CountByCheckItemId(int id);

    /**
     * 根据id删除检查项
     * @param id
     */
    void deleteById(int id);

    /**
     * 根据id回显
     * @param
     * @returnc checkItem
     */
    CheckItem findById(CheckItem lzyCheckItem);

    /**
     * 更新检查项
     * @param checkItem
     */
    void update(CheckItem checkItem);

    /**
     *检查项分页查询
     * @param queryString
     * @return
     */
    Page<CheckItem> findByCondition(String queryString);

    /**
     * 根据 id name coed 查询检查项目
     * @param checkItem
     * @return
     */
    CheckItem findByIdOrNameOrcoed(CheckItem checkItem);

}
