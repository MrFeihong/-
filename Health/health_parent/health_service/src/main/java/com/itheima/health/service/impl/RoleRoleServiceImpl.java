package com.itheima.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = RoleService.class)
public class RoleRoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    /**
     * 分页查询角色
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Role> findPage(QueryPageBean queryPageBean) {
        // 获取页码和每页大小
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 条件查询
        if(StringUtils.isNotEmpty(queryPageBean.getQueryString())){
            // 模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 分页查询语句
        Page<Role> page = roleDao.findByCondition(queryPageBean.getQueryString());
        // 分页结果封装到对象中
        PageResult<Role> pageResult = new PageResult<Role>(page.getTotal(), page.getResult());
        // 返回结果
        return pageResult;
    }

   @Override
   public void addRole(Role role, Integer[] permission, Integer[] menu) {

   }
}
