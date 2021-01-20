package com.itheima.health.service;

import com.itheima.health.pojo.Member;


public interface MemberService {
    /**
     * 通过手机号码判断是否为会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 通过手机号码判断是否为会员
     * @param member
     */
    void add(Member member);
}
