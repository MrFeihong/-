package com.baidu.health.service;

import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.CheckGroup;


import java.util.List;

public interface CheckGroupService {

    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();

    /**
     * 添加检查组
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds)  throws BusinessException;

    /**
     * 检查项的分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询检查组回显数据
     * @param lzyCheckGroup
     * @return
     */
    CheckGroup findById(CheckGroup lzyCheckGroup);

    /**
     * 通过检查组id查询选中的检查项id
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 修改检查组
     * @param checkgroup 检查组信息
     * @param checkitemIds 选中的检查项id数组
     */
    void update(CheckGroup checkgroup, Integer[] checkitemIds) throws BusinessException;

    /**
     * 删除检查组
     * @param id
     */
    void deleteById(int id) throws BusinessException;

}
