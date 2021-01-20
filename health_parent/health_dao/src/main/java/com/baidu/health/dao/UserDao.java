package com.baidu.health.dao;

import com.baidu.health.pojo.User;
import com.github.pagehelper.Page;

import java.util.Map;

public interface UserDao {
    /**
     * 通过用户名查询用户角色权限
     * @param username
     * @return
     */
    User findByUsername(String username);


    Page<User> findPage(String queryString);

    public void setUserAndRole(Map<String, Integer> map);

    void add(User user);

    User findById(Integer id);

    void edit(User user);

    void deleteAssociation(Integer id);

    void delete(Integer id);

    Integer[] findRoleIdsById(Integer id);

    User findByUsername2(String username);
}
