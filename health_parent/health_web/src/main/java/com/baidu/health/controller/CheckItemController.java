package com.baidu.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.entity.Result;
import com.baidu.health.pojo.CheckItem;
import com.baidu.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    /**
     * 查询所有检查项 不暴露给外面
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckItem> checkItemList =checkItemService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItemList);
    }

    /**
     * 添加检查项
     * @param checkItem
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@Validated @RequestBody CheckItem checkItem){
        checkItemService.add(checkItem);
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 检查项分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //封装到queryPageBean中里面有当前页数和每页条数和查询条件
        PageResult<CheckItem> pageResult=checkItemService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }


    /**
     * 根据id删除检查项  改数据库的都需要使用post请求
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        checkItemService.deleteById(id);
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    /**
     * 根据id查询检查项回显
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        CheckItem lzyCheckItem = new CheckItem();
        lzyCheckItem.setId(id);
        CheckItem checkItem = checkItemService.findById(lzyCheckItem);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    /**
     * 修改检查项
     * @param checkItem
     * @return
     */
    @PostMapping("/update")
    public Result update(@Validated @RequestBody CheckItem checkItem){
        checkItemService.update(checkItem);
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

}
