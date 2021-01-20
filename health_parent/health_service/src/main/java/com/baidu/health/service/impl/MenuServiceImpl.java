package com.baidu.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.exceptions.BusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.baidu.health.dao.MenuDao;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;

import com.baidu.health.pojo.Menu;
import com.baidu.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单服务
 */

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    /**
     * 新增菜单页面
     * @param menu
     * @return
     */
    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    /**
     * 分页查询菜单页面
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<Menu> page = menuDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @Override
    public void delete(Integer id)  {

        try {
            menuDao.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("被角色使用不能删除");
        }
    }

    /**
     * 根据id查询菜单页面
     * @param id
     * @return
     */
    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    /**
     * 编辑菜单页面
     * @param menu
     * @return
     */
    @Override
    public void edit(Menu menu) {

        menuDao.edit(menu);
    }

    /**
     * 查询所有菜单集合（按层级顺序）
     * @return
     */
    @Override
    public List<Menu> findAll() {
        // 查询所有菜单
        List<Menu> menuList = menuDao.findAll();
        // 创建空集合
        List<Menu> menus = new ArrayList<>();
        // 遍历查询到的数据
        for (Menu menu : menuList) {
            // 进行判断是否是父菜单
            if (menu.getLevel() == 1) {
                //表示 是父菜单进行添加
                menus.add(menu);
                // 根据父类的id查询 子菜单
                List<Menu> children = menuDao.findChildren(menu.getId());
                for (Menu child : children) {
                    menus.add(child);
                }
            }
        }
        return menus;
    }



    /**
     * 查询所有菜单集合（按层级顺序）
     * @param id
     * @return
     */
    public List findUserMenuRole(Integer id) {
        return menuDao.findUserMenuRole(id);
    }

}