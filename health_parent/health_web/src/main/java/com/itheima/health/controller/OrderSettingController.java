package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    private static Logger log = LoggerFactory.getLogger(SetmealController.class);

    @Reference
    private OrderSettingService orderSettingService;


    /**
     * 批量导入设置
     * @param excelFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){
        // 调用POIUtils解析excel文件
        try {
            List<String[]> excelData = POIUtils.readExcel(excelFile);
            // 得到List<String[]>，再调用service 方法做导入，返回给页面
            log.debug("导入预设读取记录",excelData.size());
            final SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            List<OrderSetting> orderSettingList = excelData.stream().map(arr -> {
                OrderSetting os = new OrderSetting();
                try {
                    os.setOrderDate(sdf.parse(arr[0]));
                    os.setNumber(Integer.valueOf(arr[1]));
                } catch (ParseException e) {
                }
                return os;
            }).collect(Collectors.toList());
            // 调用业务层导入
            orderSettingService.addBatch(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            log.error("导入预设失败",e);
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 按月查询预约设置信息
     * @return
     */
    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        // 调用业务查询
        List<Map<String,Integer>> date = orderSettingService.getOrderSettingByMonth(month);
        // 返回结果
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,date);

    }

    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        // 调用服务更新
        orderSettingService.editNumberByDate(orderSetting);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }

}
