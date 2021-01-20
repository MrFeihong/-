package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;

import java.util.List;

public interface RoleDao {

    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll();

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Role> findByCondition(String queryString);
}
