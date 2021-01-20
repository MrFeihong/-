package com.baidu.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.dao.MemberDao;
import com.baidu.health.dao.OrderDao;
import com.baidu.health.service.ReportService;
import com.baidu.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 运营数据统计
     *
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 获取这周的星期一
        String monday = sdf.format(DateUtils.getThisWeekMonday());
        // 获取这周的星期天
        String sunday = sdf.format(DateUtils.getSundayOfThisWeek());
        // 1号
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDayOfThisMonth());
        // 本月最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());

        //reportDate 今天
        String today = sdf.format(new Date());

        // =================== 会员数量统计 =======================
        //todayNewMember
        int todayNewMember = memberDao.findMemberCountByDate(today);
        //totalMember
        int totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember
        int thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //thisMonthNewMember
        int thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);

        // ==================== 预约到诊统计 ======================
        //todayOrderNumber
        int todayOrderNumber = orderDao.findOrderCountByDate(today);
        //todayVisitsNumber
        int todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        //thisWeekOrderNumber
        int thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday,sunday);
        //thisWeekVisitsNumber
        int thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //thisMonthOrderNumber
        int thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth,lastDayOfThisMonth);
        //thisMonthVisitsNumber
        int thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);

        // ===================== 热门套餐 =========================
        // hotSetmeal
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

        Map<String,Object> reportData = new HashMap<String,Object>();
        reportData.put("reportDate",today);
        reportData.put("todayNewMember",todayNewMember);
        reportData.put("totalMember",totalMember);
        reportData.put("thisWeekNewMember",thisWeekNewMember);
        reportData.put("thisMonthNewMember",thisMonthNewMember);
        reportData.put("todayOrderNumber",todayOrderNumber);
        reportData.put("todayVisitsNumber",todayVisitsNumber);
        reportData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        reportData.put("hotSetmeal",hotSetmeal);

        return reportData;
    }
}
