package com.baidu.health.dao;

import com.github.pagehelper.Page;
import com.baidu.health.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface RoleDao {

     Set<Role> findByUserId(Integer userId);


    /**
     * 查询所有
     * @return
     */
     List<Role> findAll();

    /**
     * 添加角色
     * @param role
     */
     void add(Role role);

    /**
     * 设置角色和菜单表的关联关系
     * @param map
     */
     void setRoleAndMenu(Map<String, Integer> map);

    /**
     * 设置角色和权限表的关联关系
     * @param map
     */
     void setRoleAndPermission(Map<String, Integer> map);

    /**
     * 根据id查询角色
     * @param roleId
     * @return
     */
     Role findById(Integer roleId);

    /**
     * 查询角色对应的菜单Id
     * @param roleId
     * @return
     */
     Integer[] findMenuIdsByRoleId(Integer roleId);

    /**
     * 查询角色对应权限Id数组
     * @param roleId
     * @return
     */
     Integer[] findPermissionIdsByRoleId(Integer roleId);

    /**
     * 编辑保存角色信息
     * @param role
     */
     void edit(Role role);

    /**
     * 根据id删除
     * @param roleId
     */
     void deleteById(Integer roleId);

    /**
     * 删除角色和菜单关系表数据
     * @param roleId
     */
     void deleteRoleAndMenuRelation(Integer roleId);

    /**
     * 删除角色和权限关系表数据
     * @param roleId
     */
     void deleteRoleAndPermissionRelation(Integer roleId);

    /**
     * 查询当前页面数据
     * @param queryString
     * @return
     */
    Page<Role> findByCondition(String queryString);
}
