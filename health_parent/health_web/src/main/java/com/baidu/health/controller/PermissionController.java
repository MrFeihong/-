package com.baidu.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.entity.Result;
import com.baidu.health.pojo.Permission;
import com.baidu.health.service.PermissionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;


    /**
     * 添加权限
     * @param permission
     * @return
     */
    @PostMapping("/add")

    public Result add(@Validated @RequestBody Permission permission){
        // 调用业务层添加
        permissionService.add(permission);
        // 返回添加结果
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }

    /**
     * 分页查询权限
     * @param queryPageBean
     * @return
     */

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //封装到queryPageBean中里面有当前页数和每页条数和查询条件
        PageResult<Permission> pageResult = permissionService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,pageResult);
    }


    /**
     * 根据id查询权限回显
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Permission permission = permissionService.findById(id);
        return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
    }

    /**
     * 修改权限
     * @param permission
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody Permission permission){
        permissionService.update(permission);
        return new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
    }


    /**
     * 根据id删除权限
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        permissionService.deleteById(id);
        return new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

    /**
     * 查询所有权限
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(){
        List<Permission> permissionList =permissionService.findAll();
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,permissionList);
    }
}
