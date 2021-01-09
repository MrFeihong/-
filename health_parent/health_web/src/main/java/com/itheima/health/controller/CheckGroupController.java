package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    /**
     * 订阅服务
     */
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 查询所有检查组
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(){
        // 调用业务
        List<CheckGroup> list = checkGroupService.findAll();
        // 返回结果
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
    }

    /**
     * 添加检查组
     * @param checkgroup 检查组信息
     * @param checkitemIds 选中的检查项id数组
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkgroup, Integer[] checkitemIds){
        // 调用业务
        checkGroupService.add(checkgroup, checkitemIds);
        // 返回结果
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用业务
        PageResult<CheckGroup> pageResult = checkGroupService.findPage(queryPageBean);
        // 返回结果
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }

    /**
     * 通过id查询
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用业务
        CheckGroup checkGroup = checkGroupService.findById(id);
        // 返回结果
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }

    /**
     * 根据id查询检查组中的检查项
     * @param id
     * @return
     */
    @GetMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(int id){
        // 根据id查询检查组中的检查项
        List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        // 返回结果
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
    }

    /**
     * 修改
     * @param checkgroup
     * @param checkitemIds
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkgroup, Integer[] checkitemIds){
        // 调用业务
        checkGroupService.update(checkgroup, checkitemIds);
        // 返回结果
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }


    /**
     * 根据id删除
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
       // 调用业务
        checkGroupService.deleteById(id);
       // 返回结果
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
