package com.baidu.health.service;



import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.pojo.User;

import java.util.List;

public interface UserService {
    /**
     * 通过用户名查询用户角色权限
     * @param username
     * @return
     */
     User findByUsername(String username);


     PageResult findPage(QueryPageBean queryPageBean);

     List<User> findAll();

     void add(User user, Integer[] roleIds);

     User findById(Integer id);

     void edit(User user, Integer[] roleIds);

     void delete(Integer id);

     Integer[] findRoleIdsById(Integer id);

}
