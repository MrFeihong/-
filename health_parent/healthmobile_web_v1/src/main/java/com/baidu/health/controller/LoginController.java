package com.baidu.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.constant.RedisMessageConstant;
import com.baidu.health.entity.Result;
import com.baidu.health.pojo.Member;
import com.baidu.health.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @PostMapping("/check")
    public Result login(@RequestBody Map<String,String> loginInfo, HttpServletResponse res){
        // 校验验证码
        //- 拼接redis的key, 获取redis中的验证码
        //先获取手机号
        String telephone = loginInfo.get("telephone");
        // 拼接手机号获取key
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        Jedis jedis = jedisPool.getResource();
        // 查看redis中是否有该验证码
        String codeInRedis = jedis.get(key);
        log.info("codeInRedis:{}",codeInRedis);

        if(StringUtils.isEmpty(codeInRedis)) {
            //- 没有值 说验证码失效或者没有获取验证码
            //  - 提示重新获取验证码
            return new Result(false, "请重新获取验证码!");
        }
        log.info("codeFromUI:{}",loginInfo.get("validateCode"));
        //- 有值
        //  - 校验前端提交过来的验证码是否相同
        if(!codeInRedis.equals(loginInfo.get("validateCode"))) {
            //  - 不相同，提交验证码错误
            return new Result(false, "验证码不正确!");
        }
        //  - 相同，删除key
        jedis.del(key); // 防止重复提交

        // 通过手机号码判断是否为会员
        Member member = memberService.findByTelephone(telephone);
        if(null == member){
            // 注册为会员
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            member.setRemark("快速登陆");
            memberService.add(member);
        }
        //不为空或者已经注册成会员了
        // 添加cookie跟踪
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setMaxAge(30*24*60*60);// cookie存活时间
        cookie.setPath("/");// 所有的访问url都带上这个cookie
        // 输出给客户端
        res.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

    @RequestMapping("/getTelephone")
    public Result getTelephone(@CookieValue("login_member_telephone") String telephone){
        return new Result(true, "获取手机号码成功",telephone);
    }
}


