package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    /**
     * 订阅检查项服务
     */
    @Reference
    private CheckItemService checkItemService;


    /**
     * 查询所有
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(){

        // 调用业务查询
        List<CheckItem> list = checkItemService.findAll();
        // 封装返回
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
    }

    /**
     * 添加检查项
     * @param checkItem
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        // 调用业务添加
        checkItemService.add(checkItem);
        // 返回结果
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页查询检查项
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用服务 分页查询
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);
        // 返回结果      ;
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);
    }


    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用业务查询
        CheckItem checkItem = checkItemService.findById(id);
        // 返回结果
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }
    /**
     * 修改检查项
     * @param checkItem
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
        // 调用业务更新
        checkItemService.update(checkItem);
        // 返回结果
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }


    /**
     * 通过id删除
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用业务
        checkItemService.deleteById(id);
        // 返回结果
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

}
