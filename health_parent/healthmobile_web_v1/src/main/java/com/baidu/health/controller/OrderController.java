package com.baidu.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.constant.RedisMessageConstant;
import com.baidu.health.entity.Result;
import com.baidu.health.pojo.Order;
import com.baidu.health.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String, String> orderInfo ){
        // 校验验证码是否存在 先获取手机号码
        String telephone = orderInfo.get("telephone");
        if (null==telephone) {
            return new Result(false, "手机号码不能为空");
        }
        // 然后拼接redis的key，获取redis中的验证码
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        Jedis jedis = jedisPool.getResource();
        // 从redis中获取校验验证码
        String codeInRedis = jedis.get(key);
        // 日志打印一下redis中的验证码
        log.info("codeInRedis:{}",codeInRedis);
        // 判断redis中是否有值 有值则判断是否相同 没值则提示
        if(StringUtils.isEmpty(codeInRedis)) {
            // 没有值 则说明验证码没有发送或者验证码失效
            return new Result(false, "请重新获取验证码!");
        }
        log.info("codeFromUI:{}",orderInfo.get("validateCode"));

        // redis中有值则校验验证码是否正确
        if (null==orderInfo.get("validateCode")){
            return new Result(false, "验证码不能为空");
        }

        // 校验前端提交过来的验证码是否相同
        if(!codeInRedis.equals(orderInfo.get("validateCode"))) {
            // 不相同，提交验证码错误
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
     * 查询预约成功订单信息 回显数据
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Map<String,String> orderInfo = orderService.findDetailById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }


}
