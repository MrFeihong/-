package com.itheima.health.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
public class CleanImgJob {

    private static final Logger log = LoggerFactory.getLogger(CleanImgJob.class);

    @Reference
    private SetmealService setmealService;

    /**
     * 清理七牛上的垃圾图片
     */
    //@Scheduled(initialDelay = 3000,fixedDelay = 1800000)
    @Scheduled(cron = "0 0 4 * * ?")
    public void cleanImg(){
        log.info("开发执行清理垃圾图片....");
        //1 调用QiNiuUtils.查询所有的图片
        List<String> imgIn7Niu = QiNiuUtils.listFile();
        // {} 点位符
        log.debug("7牛上一共有{}张图片",imgIn7Niu.size());
        //2 调用setmealService查询数据库的所有图片
        List<String> imgInDb = setmealService.findImgs();
        log.debug("数据库里一共有{}张图片",imgInDb==null?0:imgInDb.size());
        //3 把七牛上的减去数据库的，剩下的就是要删除的图片
        imgIn7Niu.removeAll(imgInDb);
        if(imgIn7Niu.size() > 0){
            log.debug("要清理的垃圾图片有{}张",imgIn7Niu.size());//4 调用七牛工具删除垃圾图片
            QiNiuUtils.removeFiles(imgIn7Niu.toArray(new String[]{}));
        }else{
            log.debug("没有需要清理的垃圾图片");
        }
        log.info("清理垃圾图片完成....");
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        List<String> list2 = new ArrayList<String>();
        list2.add("3");
        list2.add("4");

        list.removeAll(list2);
        list.forEach(System.out::println);
    }
}
