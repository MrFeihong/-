package com.baidu.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.entity.Result;
import com.baidu.health.pojo.Menu;
import com.baidu.health.service.MenuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单管理
 */

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    /**
     * 新增菜单页面
     * @param menu
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu){
        //level自动生成 依据path
        String path = menu.getPath();   //String path = menu.getPath();   // 获取path
        char result = path.charAt(0);
        if(result == 47){   //47是斜杠的ASCII码
            menu.setLevel(2);
        }else{
            menu.setLevel(1);
        }
            menuService.add(menu);//发送请求
            return new Result(true,"添加成功");
    }


    /**
     * 分页查询菜单页面
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        return menuService.findPage(queryPageBean);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id){
            menuService.delete(id);//发送请求
            return new Result(true, "删除成功");
    }

    /**
     * 根据id查询菜单页面
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
            Menu menu = menuService.findById(id);//发送请求
            return new Result(true,"查询成功",menu);
    }

    /**
     * 编辑菜单页面
     * @param menu
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu  menu){
            menuService.edit(menu);//发送请求
            return new Result(true, "更新成功");
    }


    /**
     * 查询所有菜单集合（按层级顺序）
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
            List<Menu> list = menuService.findAll();
            return new Result(true,"查询成功",list);
    }
}
