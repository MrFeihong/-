package com.baidu.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.entity.Result;

import com.baidu.health.pojo.Role;
import com.baidu.health.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    /**
     * 分页查询
     *
     * @param pageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean pageBean) {
        return roleService.findPage(pageBean);
    }

    /**
     * 查询所有
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        List<Role> roleList = roleService.findAll();
        return new Result(true, "查询成功", roleList);
    }


    /**
     * 添加角色
     * @param role
     */
    @RequestMapping("/addRole")
    public Result addRole(@RequestBody Role role, Integer[] menuIds, Integer[] permissionIds) {
        roleService.addRole(role, menuIds, permissionIds);
        return new Result(true, "添加成功");

    }


    /**
     * 根据角色Id查询角色基础信息
     * @param roleId
     * @return
     */
    @RequestMapping("/findById")
    public Result findByRoleId(Integer roleId) {
        Role role = roleService.findById(roleId);
        return new Result(true, "查询成功", role);
    }

    /**
     * 根据角色Id查询对应的菜单Id数组和权限Id数组
     * 数据结构Map集合存储两个Integer数组
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/findMenuIdsAndPermissionIdsByRoleId")
    public Result findMenuIdsAndPermissionIdsByRoleId(Integer roleId) {
        Map<String, Integer[]> dataMap = roleService.findMenuIdsAndPermissionIdsByRoleId(roleId);
        return new Result(true, "查询成功", dataMap);
    }


    /**
     * 修改保存
     *
     * @param role
     * @param menuIds
     * @param permissionIds
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role, Integer[] menuIds, Integer[] permissionIds) {
        roleService.edit(role, menuIds, permissionIds);
        return new Result(true, "修改成功");
    }


    /**
     * 根据Id删除
     *
     * @param roleId
     * @throws
     */
    @RequestMapping("/deleteById")
    public Result deleteById(Integer roleId) {
        roleService.deleteById(roleId);
        return new Result(true, "删除成功");
    }
}
