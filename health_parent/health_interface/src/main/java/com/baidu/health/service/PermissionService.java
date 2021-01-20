package com.baidu.health.service;

import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.Permission;

import java.util.List;


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
    Permission findById(int id);


    /**
     * 根据id删除权限
     * @param id
     */
    void deleteById(int id)throws BusinessException;

    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAll();

}
