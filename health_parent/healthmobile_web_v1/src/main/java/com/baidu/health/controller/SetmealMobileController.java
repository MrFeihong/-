package com.baidu.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.entity.Result;
import com.baidu.health.pojo.Setmeal;

import com.baidu.health.service.SetmealService;
import com.baidu.health.utils.QiNiuUtils;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;


@Validated
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有
     */
    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        // 查询所有的套餐
        List<Setmeal> list = setmealService.findAll();
        // 遍历集合中的元素获取img属性拼接完整路径
        list.forEach(s->{
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
    }

    /**
     * 查询套餐详情 五表关联
     */
    @GetMapping("/findDetailById")
    public Result findDetailById(int id){
        Setmeal setmeal = setmealService.findDetailById(id);
        // 拼接图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    /**
     * 通过id查询套餐信息
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(@Min(value = 0,message = "查询id不能小于0")int id){
        // 因为自己之前写了findById方法 用的是setmeal所以这里只能使用这个了
        Setmeal setmeaLzy = new Setmeal();
        setmeaLzy.setId(id);
        Setmeal setmeal = setmealService.findById(setmeaLzy);
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
    /**
     * 通过id查询套餐详情
     * @param id
     * @return
     */
    @GetMapping("/findDetailById3")
    public Result findDetailById3(int id){
        // 调用服务查询套餐详情
        Setmeal setmeal = setmealService.findDetailById3(id);
        // 拼接图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

}
