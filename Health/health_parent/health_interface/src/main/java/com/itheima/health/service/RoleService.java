package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Role;

import java.util.List;

public interface RoleService {

    /**
     * 查询所有
     * @return
     */
    List<Role> findAll();

    /**
     * 分页查询角色
     * @param queryPageBean
     * @return
     */
    PageResult<Role> findPage(QueryPageBean queryPageBean);

    /**
     * 添加角色
     * @param role
     * @param permission
     * @param menu
     */
    void addRole(Role role, Integer[] permission, Integer[] menu);
}
