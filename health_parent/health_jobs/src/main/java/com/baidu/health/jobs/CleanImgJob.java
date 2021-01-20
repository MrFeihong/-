package com.baidu.health.jobs;

import com.alibaba.dubbo.config.annotation.Reference;

import com.baidu.health.service.OrderSettingService;
import com.baidu.health.service.SetmealService;
import com.baidu.health.utils.DateUtils;
import com.baidu.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Component
public class CleanImgJob {

    private static final Logger log = LoggerFactory.getLogger(CleanImgJob.class);

    /**
     * 订阅服务
     */
    @Reference
    private SetmealService setmealService;
    @Reference
    private OrderSettingService orderSettingService;

    @Scheduled(initialDelay = 3000, fixedDelay = 1800000)
//    @Scheduled(cron = "0/30 * * * * ?")
    public void cleanImg() {
        log.info("开发执行清理垃圾图片....");
        //1 调用QiNiuUtils.查询所有的图片
        List<String> imgIn7Niu = QiNiuUtils.listFile();
        // {} 点位符
        log.debug("7牛上一共有{}张图片", imgIn7Niu.size());
        //2 调用setmealService查询数据库的所有图片
        List<String> imgInDb = setmealService.findImgs();
        log.debug("数据库里一共有{}张图片", imgInDb == null ? 0 : imgInDb.size());
        //3 把七牛上的减去数据库的，剩下的就是要删除的图片
        imgIn7Niu.removeAll(imgInDb);
        if (imgIn7Niu.size() > 0) {
            log.debug("要清理的垃圾图片有{}张", imgIn7Niu.size());//4 调用七牛工具删除垃圾图片
            QiNiuUtils.removeFiles(imgIn7Niu.toArray(new String[]{}));
        } else {
            log.debug("没有需要清理的垃圾图片");
        }
        log.info("清理垃圾图片完成....");
    }

//    定时清理预约设置历史数据
//    说明：预约设置（OrderSetting）数据是用来设置未来每天的可预约人数，
//    随着时间的推移预约设置表的数据会越来越多，而对于已经过去的历史数据可以定时来进行清理，
//    例如每月最后一天凌晨2点执行一次清理任务
    @Scheduled(initialDelay = 3000, fixedDelay = 1800000)
//    @Scheduled(cron = "0/30 * * * * ?")
    public void cleanOrderSetting() throws Exception {
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=cal.getTime();
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String ZUOTIAN=sp.format(d);//获取昨天日期
        Date date = DateUtils.parseString2Date(ZUOTIAN);
        orderSettingService.cleanOrderSetting(date);
    }
}