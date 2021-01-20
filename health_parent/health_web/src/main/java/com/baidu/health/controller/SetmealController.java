package com.baidu.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.entity.Result;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.Setmeal;
import com.baidu.health.service.SetmealService;
import com.baidu.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    private static Logger log =LoggerFactory.getLogger(SetmealController.class);

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 套餐上传图片
     * @param imgFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //1.获取源文件名
        String originalFilename = imgFile.getOriginalFilename();
        //2.截取后缀名字
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //3.生成唯一id
        String uniqueId = UUID.randomUUID().toString();
        //4.拼接唯一文件名
        String fileName = uniqueId + suffix;
        //5.调用七牛云存储工具上传图片
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),fileName);
            //6.构建返回的数据
            Map<String, String> map = new HashMap<String, String>(2);
            map.put("imgName",fileName);
            map.put("domain",QiNiuUtils.DOMAIN);
            //7.放到Result，在返回给页面
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 套餐分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> setmealPageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmealPageResult);
    }
    /**
     * 添加检查组套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@Validated @RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        if (setmeal.getImg()==null||setmeal.getImg().length()==0){
            throw new BusinessException("图片不能为空");
        }
        Integer setmealId = setmealService.add(setmeal, checkgroupIds);
        // 添加成功, 生成静态页面
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Long currentTimeMillis = System.currentTimeMillis();
        // zadd setmeal:static:html 时间戳 套餐id|操作符|时间戳
        jedis.zadd(key, currentTimeMillis.doubleValue(),setmealId+"|1|" + currentTimeMillis);
        jedis.close();
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }
    /**
     * 通过id查询套餐信息 回显
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务查询
        Setmeal setmealLzy = new Setmeal();
        setmealLzy.setId(id);
        Setmeal setmeal = setmealService.findById(setmealLzy);
        // 前端要显示图片需要全路径
        // 封装到map中，解决图片路径问题
        Map<String,Object> map = new HashMap<String,Object>(2);
        map.put("setmeal", setmeal); // formData
        map.put("domain", QiNiuUtils.DOMAIN); // domain
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,map);
    }

    /**
     * 通过id查询选中的检查组ids
     */
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        List<Integer> checkgroupIds = setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true, "修改",checkgroupIds);
    }

    /**
     * 修改套餐
     */
    @PostMapping("/update")
    public Result update(@Validated @RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        if (setmeal.getImg()==null||setmeal.getImg().length()==0){
            throw new BusinessException("图片不能为空");
        }
        // 调用业务服务修改
        setmealService.update(setmeal, checkgroupIds);
        // 响应结果
        // 修改成功, 生成静态页面
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Long currentTimeMillis = System.currentTimeMillis();
        // zadd setmeal:static:html 时间戳 套餐id|操作符|时间戳
        jedis.zadd(key, currentTimeMillis.doubleValue(),setmeal.getId()+"|1|" + currentTimeMillis);
        jedis.close();
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 删除套餐
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result delete(int id){
        setmealService.deleteById(id);
        // 添加成功, 生成静态页面
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Long currentTimeMillis = System.currentTimeMillis();
        // zadd setmeal:static:html 时间戳 套餐id|操作符|时间戳
        jedis.zadd(key, currentTimeMillis.doubleValue(),id+"|0|" + currentTimeMillis);
        jedis.close();
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
