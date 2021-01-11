package com.itheima.health.controller;


import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    private static Logger log = LoggerFactory.getLogger(SetmealController.class);
    /**
     * 订阅套餐
     */
    @Reference
    private SetmealService setmealService;


    /**
     *新增套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用业务
        setmealService.add(setmeal,checkgroupIds);
        // 返回结果
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 上传图片
     * @param imgFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        // 获取源文件名,截取后缀名
        String originalFilename = imgFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成id
        String uniqueId = UUID.randomUUID().toString();
        // 拼接文件名
        String filename = uniqueId + suffix;
        // 调用工具上传图片
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), filename);
            // 构建返回的数据
            Map<String,String> map = new HashMap<String,String>(2);
            map.put("imgName", filename);
            map.put("domain",QiNiuUtils.DOMAIN);
            // 放到result里，再返回给页面
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            log.error("上传文件失败了",e);
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用业务层方法查询
        PageResult<Setmeal> setmealPageResult = setmealService.findPage(queryPageBean);
        // 返回查询结果
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmealPageResult);

    }

    /**
     * 根据id查询
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id ){
        // 调用服务查询id
        Setmeal setmeal = setmealService.findById(id);
        // 构建前端需要的数据和域名
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("setmeal", setmeal);
        map.put("domain",QiNiuUtils.DOMAIN);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,map);
    }

    /**
     * 根据id查询检查组集合
     * @return
     */
    @GetMapping("/findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(int id){
        // 调用服务查询id
        List<Integer> checkgroupIds = setmealService.findCheckGroupIdsBySetmealId(id);
        // 返回结果
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,checkgroupIds);
        
    }

    /**
     * 修改套餐
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        setmealService.update(setmeal,checkgroupIds);
        return new Result(true, "修改套餐成功");
    }

    
    /**
     *删除套餐
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){

        setmealService.deleteById(id);
        return new Result(true, "删除套餐成功");
    }


}
