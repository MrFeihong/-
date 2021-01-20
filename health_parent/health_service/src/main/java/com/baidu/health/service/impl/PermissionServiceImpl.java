package com.baidu.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.exceptions.BusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.baidu.health.dao.PermissionDao;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;

import com.baidu.health.pojo.Permission;
import com.baidu.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 添加权限
     * @param permission
     * @throws BusinessException
     */
    @Override
    public void add(Permission permission) {

        // 查询权限名称
        Permission findName = permissionDao.findByName(permission);
        // 校验权限名称
        if(null != findName){
            throw new BusinessException("权限名称已存在");
        }
        // 查询权限关键字
        Permission findKeyWord = permissionDao.findByKeyWord(permission);
        // 校验权限关键字
        if(null != findKeyWord){
            throw new BusinessException("权限关键字已存在！");
        }

        // 调用Dao添加
        permissionDao.add(permission);

    }


    /**
     * 分页查询权限
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Permission> findPage(QueryPageBean queryPageBean){

        queryPageBean.setPageSize(queryPageBean.getPageSize()>50?50:queryPageBean.getPageSize());
        // 获取页码和每页大小
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 判断是否有条件查询
        if (StringUtils.isNotEmpty(queryPageBean.getQueryString())) {
            // 有查询条件， 使用模糊查询 拼接上%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        // page extends arrayList 返回的值
        Page<Permission> page = permissionDao.findByCondition(queryPageBean.getQueryString());
        PageResult<Permission> pageResult = new PageResult<Permission>(page.getTotal(), page.getResult());

        return pageResult;

    }


    /**
     * 根据id查询权限回显
     * @param id
     * @return
     */
    @Override
    public Permission findById(int id) {
        return permissionDao.findById(id);
    }

    /**
     * 修改权限
     * @param permission
     * @throws BusinessException
     */
    @Override
    public void update(Permission permission) {
        // 查询权限名称
        Permission findName = permissionDao.findByName(permission);
        // 查询权限关键字
        Permission findKeyWord = permissionDao.findByKeyWord(permission);

        // 校验权限名称
        if(null != findName){
            if (permission.getId()!=findName.getId()){
                throw new BusinessException("权限名称已存在！");
            }
        }
        // 校验权限关键字
        if(null != findKeyWord){
            if (findKeyWord.getId() != permission.getId()){
                throw new BusinessException("权限关键字已存在！");
            }
        }
        permissionDao.update(permission);

    }


    /**
     * 根据id删除权限
     * @param id
     * @throws BusinessException
     */

    @Override
    public void deleteById(int id)  {
        // 判断权限是否被使用
        int permissionId = permissionDao.permissionById(id);
        if (permissionId > 0){
            throw new BusinessException("权限被使用,无法删除");
        }

        permissionDao.deleteById(id);

    }
    /**
     * 查询所有权限
     * @return
     */
    @Override
    public List<Permission> findAll() {
        List<Permission>permissionList = permissionDao.findAll();
        return permissionList;
    }

}
