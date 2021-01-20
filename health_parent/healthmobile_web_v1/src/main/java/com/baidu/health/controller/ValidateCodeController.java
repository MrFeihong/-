package com.baidu.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.constant.RedisMessageConstant;
import com.baidu.health.entity.Result;
import com.baidu.health.utils.SMSUtils;
import com.baidu.health.utils.ValidateCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.validation.constraints.NotBlank;


@Validated
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    private static final Logger log = LoggerFactory.getLogger(ValidateCodeController.class);

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/send4Order")
    public Result send4Order(@NotBlank(message = "手机号码不能为空") String telephone){
        Jedis jedis = jedisPool.getResource();
        // 判断redis中是否存在验证码
        String key = RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone;
        if (jedis.exists(key)){
            // 存在 则提示已经发送过了，请注意查收
            return new Result(false,"验证码已经发送过了,请注意查收");
        }
        // 不存在 则先生成验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        log.debug("=========生成验证码为{}"+validateCode);
        // 调用SMSUtil发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode+"");
        } catch (ClientException e) {
            //e.printStackTrace();
            log.error("发送验证码失败",e);
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
         // - 存入redis，设置有效期
        jedis.setex(key,10*60,validateCode+"");
        jedis.close();
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
    /**
     * 登陆验证码
     */
    @PostMapping("/send4Login")
    public Result send4Login(String telephone){
        Jedis jedis = jedisPool.getResource();
        //- 判断redis中是否存在验证码
        // 拼接获取redis中的key
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        if (jedis.exists(key)) {
            //- 存在则提示已经发送过了，请注意查收
            return new Result(false, "验证码已经发送过了，请注意查收");
        }
        //- 不存在
        //  - 先生成验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        log.debug("========生成的验证码:{}",validateCode);
        //  - 调用SMSUtils发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode+"");
        } catch (ClientException e) {
            //e.printStackTrace();
            log.error("发送验证码失败: {}:{}",telephone,validateCode);
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //  - 存入redis，设置有效期
        jedis.setex(key,10*60,validateCode+"");
        jedis.close();
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
