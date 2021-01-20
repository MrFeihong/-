package com.baidu.health.service;

import com.baidu.health.pojo.Member;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/12
 */
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
    /**
     * 统计每个月的会员总数量
     * @param months
     * @return
     */
    List<Integer> getMemberReport(List<String> months);


    /**
     * 根据月份查询对应的会员数量
     * @param months
     * @return
     */
    public List<Integer> findMemberCountByMonths(List<String> months);

    /**
     * 根据性别查询会员数量
     * @return
     */
    public List<Map> getMemberGenderReport();

    /**
     * 根据年龄查询会员数量
     * @return
     */
    public List<Map> getMemberAgeReport();
}
