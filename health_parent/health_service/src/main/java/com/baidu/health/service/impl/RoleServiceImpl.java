package com.baidu.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.exceptions.BusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.baidu.health.dao.RoleDao;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.pojo.Role;
import com.baidu.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    /**
     * 分页查询
     * @param pageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean pageBean) {
        //获取当前页数和当前页面大小
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        //判断条件查询是否为空
        if (StringUtils.isNotEmpty(pageBean.getQueryString())) {
            //不为空 就行拼接
            pageBean.setQueryString("%" + pageBean.getQueryString() + "%");
        }
        //进行分页查询
        Page<Role> page = roleDao.findByCondition(pageBean.getQueryString());
        return new PageResult<Role>(page.getTotal(), page.getResult());
    }

    /**
     * 根据角色Id查询角色基础信息
     *
     * @param id
     * @return
     */
    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    /**
     * 根据角色Id查询对应的菜单Id数组和权限Id数组
     * 数据结构Map集合存储两个Integer数组
     *
     * @param roleId
     * @return
     */
    @Override
    @Transactional
    public Map<String, Integer[]> findMenuIdsAndPermissionIdsByRoleId(Integer roleId) {
        //获取菜单Id数组和权限Id数组
        Integer[] menuIds = roleDao.findMenuIdsByRoleId(roleId);
        Integer[] permissionIds = roleDao.findPermissionIdsByRoleId(roleId);
        //map接收
        Map<String, Integer[]> dataMap = new HashMap<>();
        dataMap.put("menuIds", menuIds);
        dataMap.put("permissionIds", permissionIds);
        return dataMap;
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    /**
     * 添加角色
     *
     * @param role
     */
    @Override
    @Transactional
    public void addRole(Role role, Integer[] menuIds, Integer[] permissionIds) {
        roleDao.add(role);

        Integer roleId = role.getId();

        setRoleAndMenuRelation(roleId, menuIds);
        setRoleAndPermissionRelation(roleId, permissionIds);
    }

    /**
     * 修改保存
     *
     * @param role
     * @param menuIds
     * @param permissionIds
     */
    @Override
    @Transactional
    public void edit(Role role, Integer[] menuIds, Integer[] permissionIds) {
        Integer roleId = role.getId();

        //删除原有的关联关系
        deleteRoleAndMenuRelation(roleId);
        deleteRoleAndPermissionRelation(roleId);

        //更新角色信息
        roleDao.edit(role);
        //建立新的关联关系
        setRoleAndMenuRelation(roleId, menuIds);
        setRoleAndPermissionRelation(roleId, permissionIds);
    }

    /**
     * 根据Id删除
     *
     * @param roleId
     * @throws
     */
    @Override
    @Transactional
    public void deleteById(Integer roleId) {
        // 查询关联的套餐
        Integer[] menuIds = roleDao.findMenuIdsByRoleId(roleId);
        // 查询关联的权限
        Integer[] permissionIds = roleDao.findPermissionIdsByRoleId(roleId);
        //进行判断
        if (menuIds != null && menuIds.length > 0) {
            throw new BusinessException("该角色被套餐使用不能删除");
        }
        if (permissionIds != null && permissionIds.length > 0) {
            throw new BusinessException("该角色被权限使用不能删除");
        }
        roleDao.deleteById(roleId);
    }

    /**
     * 删除角色和菜单关系表数据
     *
     * @param roleId
     */
    private void deleteRoleAndMenuRelation(Integer roleId) {

        roleDao.deleteRoleAndMenuRelation(roleId);
    }

    /**
     * 删除角色和权限关系表数据
     *
     * @param roleId
     */
    private void deleteRoleAndPermissionRelation(Integer roleId) {
        roleDao.deleteRoleAndPermissionRelation(roleId);
    }


    /**
     * 设置角色和菜单表的关联关系
     *
     * @param roleId
     * @param menuIds
     */
    private void setRoleAndMenuRelation(Integer roleId, Integer[] menuIds) {
        if (menuIds != null && menuIds.length > 0) {
            for (Integer menuId : menuIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("roleId", roleId);
                map.put("menuId", menuId);
                roleDao.setRoleAndMenu(map);
            }
        }
    }

    /**
     * 设置角色和权限表的关联关系
     *
     * @param roleId
     * @param permissionIds
     */
    private void setRoleAndPermissionRelation(Integer roleId, Integer[] permissionIds) {
        if (permissionIds != null && permissionIds.length > 0) {
            for (Integer permissionId : permissionIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("roleId", roleId);
                map.put("permissionId", permissionId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }
}
