package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Permission;


public interface PermissionDao {


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
    Permission findById(Integer id);
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
    int permissionById(Integer id);

    /**
     * 根据id删除权限
     * @param id
     */
    void deleteById(Integer id);


}
