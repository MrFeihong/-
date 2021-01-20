package com.baidu.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.dao.MemberDao;
import com.baidu.health.pojo.Member;
import com.baidu.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 通过手机号码判断是否为会员
     *
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 通过手机号码判断是否为会员
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 统计每个月的会员总数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        List<Integer> list = new ArrayList<Integer>();
        if(null != months){
            // 2020-02
            for (String month : months) {
                month+="-31";
                list.add(memberDao.findMemberCountBeforeDate(month));
            }
        }
        return list;
    }
    //+++++++++++++++++++++++++++++++

    /**
     *
     * @param months
     * @return
     */
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> memberCounts = new ArrayList<>();
        if(months != null && months.size() > 0){
            for (String month : months) {//2019.01
                String endTime = month + ".31";
                Integer memberCount = memberDao.findMemberCountBeforeDate(endTime);
                memberCounts.add(memberCount);
            }
        }
        return memberCounts;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Map> getMemberGenderReport() {
        List<Map> genderCount = memberDao.getMemberGenderReport();
        for (Map map : genderCount) {

            if (map.get("name") == null){
                map.put("name","未知");
            }else if(map.get("name").equals("1")){
                map.put("name","男");
            }else{
                map.put("name","女");
            }
        }
        return genderCount;

    }

    /**
     *
     * @return
     */
    @Override
    public List<Map> getMemberAgeReport() {
        List<Map> result = memberDao.getMemberAgeReport();
        return result;
    }
}
