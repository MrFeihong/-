package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    
    /**
     * 查询所有
     * @return
     */
    List<CheckGroup> findAll();


    /**
     * 添加检查组
     * @param checkgroup
     * @param checkitemIds
     */
    void add(CheckGroup checkgroup, Integer[] checkitemIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CheckGroup findById(int id);

    /***
     *根据id查询检查组中的检查项
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 修改
     * @param checkgroup
     * @param checkitemIds
     */
    void update(CheckGroup checkgroup, Integer[] checkitemIds);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(int id)throws MyException;


}
