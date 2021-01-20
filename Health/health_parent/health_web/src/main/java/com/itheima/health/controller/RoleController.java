package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(){
        List<Role> list = roleService.findAll();
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,list);
    }

    /**
     * 角色分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用业务查询，封装结果
        PageResult<QueryPageBean> pageResult = roleService.findPage(queryPageBean);
        // 返回结果
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,pageResult);
    }

    @PostMapping("/addRole")
    public Result addRole(@RequestBody Role role, Integer[] permission,Integer[] menu){
        // 调用业务查询
        roleService.addRole(role,permission,menu);
        // 返回结果
        return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
    }

    @PostMapping("/edit")
    public Result edit(Role role){
        return null;
    }

    @PostMapping("/deleteById")
    public Result deleteById(Integer id){
        return null;
    }

}
