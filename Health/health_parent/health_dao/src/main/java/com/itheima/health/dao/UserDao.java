package com.itheima.health.dao;

import com.itheima.health.pojo.User;


public interface UserDao {
    /**
     * 通过用户名查询用户角色权限
     * @param username
     * @return
     */
    User findByUsername(String username);
}
