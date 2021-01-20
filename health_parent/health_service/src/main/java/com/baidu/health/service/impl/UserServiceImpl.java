package com.baidu.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.baidu.health.dao.PermissionDao;
import com.baidu.health.dao.RoleDao;
import com.baidu.health.dao.UserDao;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.pojo.Permission;
import com.baidu.health.pojo.Role;
import com.baidu.health.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户服务
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    //根据用户名查询用户信息,包括用户的角色和角色关联的权限
    public User findByUsername(String username) {
        User user = userDao.findByUsername2(username);//根据用户名查询用户表
        if(user == null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户id查询关联的角色
        Set<Role> roles = roleDao.findByUserId(userId);
        if(roles != null && roles.size() > 0){
            //遍历角色集合，查询每个角色关联的权限
            for (Role role : roles) {
                Integer roleId = role.getId();//角色id
                //根据角色id查询关联的权限
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);
                if(permissions != null && permissions.size() > 0){
                    //角色关联权限集合
                    role.setPermissions(permissions);
                }
            }
            //用户关联角色集合
            user.setRoles(roles);
        }

        return user;
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //使用分页查询
        PageHelper.startPage(currentPage,pageSize);
        Page<User> page = userDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void add(User user, Integer[] roleIds) {
        String password = user.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        password = bCryptPasswordEncoder.encode(password);
        user.setPassword(password);
        userDao.add(user);
        Integer userId = user.getId();
        this.setUserAndRole(userId,roleIds);

    }

    private void setUserAndRole(Integer userId, Integer[] roleIds) {
        if(roleIds != null && roleIds.length > 0){
            for (Integer roleId : roleIds) {//检查项id
                Map<String,Integer> map = new HashMap<>();
                map.put("userId",userId);
                map.put("roleId",roleId);
                userDao.setUserAndRole(map);
            }
        }
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public void edit(User user, Integer[] roleIds) {
        userDao.edit(user);
        userDao.deleteAssociation(user.getId());
        this.setUserAndRole(user.getId(),roleIds);
    }

    @Override
    public void delete(Integer id) {
        userDao.deleteAssociation(id);
        userDao.delete(id);
    }

    @Override
    public Integer[] findRoleIdsById(Integer id) {
        return userDao.findRoleIdsById(id);
    }
}
