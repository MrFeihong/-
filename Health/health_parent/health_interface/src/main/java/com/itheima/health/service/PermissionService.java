package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.BusinessException;
import com.itheima.health.pojo.Permission;


public interface PermissionService {

    /**
     * 添加权限
     * @param permission
     */
    void add(Permission permission)throws BusinessException;


    /**
     * 分页查询权限
     * @param queryPageBean
     * @return
     */
    PageResult<Permission> findPage(QueryPageBean queryPageBean);


    /**
     * 修改权限
     * @param permission
     */
    void update(Permission permission)throws BusinessException;

    /**
     * 根据id查询权限回显
     * @param id
     * @return
     */
    Permission findById(Integer id);


    /**
     * 根据id删除权限
     * @param id
     */
    void deleteById(Integer id)throws BusinessException;



}
