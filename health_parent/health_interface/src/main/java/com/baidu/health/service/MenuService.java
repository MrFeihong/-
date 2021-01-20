package com.baidu.health.service;



import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.Menu;

import java.util.List;



import java.util.List;

public interface MenuService {
    /**
     *
     * @param id
     * @return
     */
     List findUserMenuRole(Integer id);

    /**
     * 新增菜单页面
     * @param menu
     * @return
     */
     void add(Menu menu);

    /**
     * 分页查询菜单页面
     * @param queryPageBean
     * @return
     */
     PageResult findPage(QueryPageBean queryPageBean);
    /**
     * 根据id删除
     * @param id
     * @return
     */
     void delete(Integer id) throws BusinessException;

    /**
     * 根据id查询菜单页面
     * @param id
     * @return
     */
     Menu findById(Integer id);


    /**
     * 编辑菜单页面
     * @param menu
     * @return
     */
     void edit(Menu menu);

    /**
     * 查询所有菜单集合（按层级顺序）
     * @return
     */
     List<Menu> findAll();

    //List findUserMenu(String keyword);

    //List findUserMenu(String role_admin);
}
