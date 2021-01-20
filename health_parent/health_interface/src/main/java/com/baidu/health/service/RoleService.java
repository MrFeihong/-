package com.baidu.health.service;



import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    /**
     * 分页查询
     * @param pageBean
     * @return
     */
     PageResult findPage(QueryPageBean pageBean);

    /**
     * 根据角色Id查询角色基础信息
     * @param id
     * @return
     */
     Role findById(Integer id);

    /**
     * 查询所有
     * @return
     */
     List<Role> findAll();

    /**
     * 添加角色
     * @param role
     */
     void addRole(Role role, Integer[] menuIds, Integer[] permissionIds);

    /**
     * 修改保存
     * @param role
     * @param menuIds
     * @param permissionIds
     */
     void edit(Role role, Integer[] menuIds, Integer[] permissionIds);

    /**
     * 根据角色Id查询对应的菜单Id数组和权限Id数组
     *       数据结构Map集合存储两个Integer数组
     * @param roleId
     * @return
     */
     Map<String, Integer[]> findMenuIdsAndPermissionIdsByRoleId(Integer roleId);

    /**
     * 根据Id删除
     * @param roleId
     * @throws
     */
     void deleteById(Integer roleId) throws BusinessException;
}
