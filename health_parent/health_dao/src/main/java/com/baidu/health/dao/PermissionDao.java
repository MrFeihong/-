package com.baidu.health.dao;

import com.github.pagehelper.Page;
import com.baidu.health.pojo.Permission;

import java.util.List;
import java.util.Set;


public interface PermissionDao {


     Set<Permission> findByRoleId(Integer roleId);
    /**
     * 校验权限名称
     * @param permission
     * @return
     */
    Permission findByName(Permission permission);

    /**
     * 校验权限关键字
     * @param permission
     * @return
     */
    Permission findByKeyWord(Permission permission);

    /**
     * 添加权限
     * @param permission
     */
    void add(Permission permission);



    /**
     * 分页查询权限
     * @param queryString
     * @return
     */
    Page<Permission> findByCondition(String queryString);


    /**
     * 根据id查询回显权限
     * @param
     * @return
     */
    Permission findById(int id);
    /**
     * 修改权限
     * @param permission
     */
    void update(Permission permission);


    /**
     * 查询权限id
     * @param id
     * @return
     */
    int permissionById(int id);

    /**
     * 根据id删除权限
     * @param id
     */
    void deleteById(int id);
    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAll();


}
