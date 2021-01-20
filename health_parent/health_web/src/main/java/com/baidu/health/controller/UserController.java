package com.baidu.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.entity.Result;
import com.baidu.health.pojo.Role;
import com.baidu.health.service.MenuService;
import com.baidu.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    @Reference
    private MenuService menuService;
    //获取当前登录（认证）用户的用户名
    @RequestMapping("/getLoginUsername")
    public Result getLoginUsername(){
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername();
            String password = user.getPassword();
            Collection<GrantedAuthority> authorities = user.getAuthorities();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return userService.findPage(queryPageBean);
    }

    //新增
    @RequestMapping("/add")
    public Result add(@RequestBody com.baidu.health.pojo.User user, Integer[] roleIds){
        try{
            userService.add(user, roleIds);//发送请求
            return new Result(true, "新增用户成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "新增用户失败");
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            com.baidu.health.pojo.User user = userService.findById(id);
            return new Result(true, "查询用户成功",user);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "查询用户失败");
        }
    }

    @RequestMapping("/findRoleIdsById")
    public Result findRoleIdsById(Integer id){
        try{
            Integer[] roleIds = userService.findRoleIdsById(id);
            return new Result(true, "查询角色ID成功",roleIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "查询角色ID失败");
        }
    }

    //编辑
    @RequestMapping("/edit")
    public Result edit(@RequestBody com.baidu.health.pojo.User user, Integer[] roleIds){
        try{
            userService.edit(user,roleIds);
            return new Result(true, "用户修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "用户修改失败");
        }
    }

    //根据id删除检查项
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try{
            userService.delete(id);//发送请求
            return new Result(true, "用户删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "用户删除失败");
        }
   }
    @RequestMapping("/findUserMenu")
    public Result findUserMenu(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if(user != null){
                String username = user.getUsername();
                com.baidu.health.pojo.User userName = userService.findByUsername(username);
                Set<Role> roles = userName.getRoles();
                for (Role role : roles) {
                    List userMenuList = menuService.findUserMenuRole(role.getId());
                    return new Result(true,MessageConstant.GET_MENU_SUCCESS,userMenuList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MENU_FAIL);
        }
        return null;
    }
}
