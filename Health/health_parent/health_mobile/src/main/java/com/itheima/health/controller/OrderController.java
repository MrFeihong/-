package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import com.itheima.health.service.OrderSettingService;
import com.tencentcloudapi.tcb.v20180608.models.OrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;


@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 提交预约
     * @param orderInfo
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> orderInfo){
        // 校验验证码
        //- 拼接redis的key, 获取redis中的验证码
        String telephone = orderInfo.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        Jedis jedis = jedisPool.getResource();
        String codeInRedis = jedis.get(key);
        log.info("codeInRedis:{}",codeInRedis);
        //- 有值
        if(StringUtils.isEmpty(codeInRedis)) {
            //- 没有值
            //  - 提示重新获取验证码
            return new Result(false, "请重新获取验证码!");
        }
        log.info("codeFromUI:{}",orderInfo.get("validateCode"));
        //  - 校验前端提交过来的验证码是否相同
        if(!codeInRedis.equals(orderInfo.get("validateCode"))) {
            //  - 不相同，提交验证码错误
            return new Result(false, "验证码不正确!");
        }
        //  - 相同，删除key, 则可以提交订单
        jedis.del(key); // 防止重复提交
        // 设置预约的类型，health_mobile, 微信预约
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        // 调用预约服务
        Order order = orderService.submitOrder(orderInfo);
        // 返回结果给页面，返回订单信息
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 查询预约成功订单信息
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Map<String,String> orderInfo = orderService.findDetailById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }
}
