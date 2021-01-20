package com.baidu.health.dao;

import java.util.List;



import com.github.pagehelper.Page;
import com.baidu.health.pojo.Menu;




public interface MenuDao {
     List findUserMenuRole(Integer id);

    /**
     * 新增菜单页面
     * @param menu
     * @return
     */
     void add(Menu menu);


    /**
     * 查询当前页面数据
     * @param queryString
     * @return
     */
     Page<Menu> findByCondition(String queryString);


    /**
     * 根据id删除
     * @param id
     * @return
     */
     void deleteById(Integer id);


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

     List<Menu> findChildren(Integer id);

}