package com.baidu.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.entity.Result;
import com.baidu.health.pojo.CheckGroup;
import com.baidu.health.service.CheckGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 查询所有的检查组 该方法不能直接暴露在外界中
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> CheckGroupList = checkGroupService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,CheckGroupList);
    }


    /**
     * 检查组的分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用服务 分页查询
        PageResult<CheckGroup> pageResult = checkGroupService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }


    /**
     * 添加检查组
     * @param checkGroup 检查组信息
     * @param checkitemIds 勾选的检查项id
     * @return
     */
    @PostMapping("/add")
    public Result add(@Validated @RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        checkGroupService.add(checkGroup,checkitemIds);
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 根据id查询检查组信息回显
     * @param id 检查组id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用业务服务
        //TODO 使用懒
        CheckGroup LzyCheckGroup = new CheckGroup();
        LzyCheckGroup.setId(id);
        CheckGroup checkGroup = checkGroupService.findById(LzyCheckGroup);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }


    /**
     * 通过检查组id查询选中的检查项id
     * @param id
     * @return
     */
    @GetMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(int id){
        // 通过检查组id查询选中的检查项id集合
        List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
    }

    /**
     * 修改检查组
     * @param checkgroup 检查组信息
     * @param checkitemIds 选中的检查项id数组
     * @return
     */
    @PostMapping("/update")
    public Result update(@Validated @RequestBody CheckGroup checkgroup, Integer[] checkitemIds){
        // 调用服务 修改检查组
        checkGroupService.update(checkgroup, checkitemIds);
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 删除检查组
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        //调用业务服务删除
        checkGroupService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
