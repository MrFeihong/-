package com.baidu.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.entity.Result;
import com.baidu.health.pojo.OrderSetting;
import com.baidu.health.service.OrderSettingService;
import com.baidu.health.utils.POIUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 上传Excel文件批量导入
     * @param excelFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            // 读取excel内容
            List<String[]> strings = POIUtils.readExcel(excelFile);
            // 转成List<OrderSetting> 构建集合模型
            List<OrderSetting> orderSettingList = new ArrayList<OrderSetting>();
            // 日期格式解析
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            //构建模型Date
            Date orderDate = null;
            //设置实体类模型封装对象 构建实体类模型
            OrderSetting os = null;
            //遍历list list里面是数组封装了两个对象一个预约日期
            // 一个是可预约人数 两个数都是String类型的Excel表中传入的
            for (String[] dataArr : strings) {
                //拿到单个行的集合dataArr[0]表示日期 用日期格式化
                orderDate = sdf.parse(dataArr[0]);
                //dataArr[1]拿到可预约人数将他转成int类型的
                int number = Integer.valueOf(dataArr[1]);
                //创建一个实体类封装两个属性 给对象赋值
                os = new OrderSetting(orderDate,number);
                //添加进之前构建的模型中
                orderSettingList.add(os);
            }
            // 调用业务服务
            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 通过月份模糊查询当月预约设置信息集合
     * @param month
     * @return
     */
    @GetMapping("/findOrderSettingByMonth")
    public Result findOrderSettingByMonth(String month){
        //前端需要的是 当月每日的  日 可预约数 已预约数字 (1,500,300)
        //所以使用集合嵌套map的形式且map类型都是<String,Integer
        List<Map<String,Integer>> data = orderSettingService.findOrderSettingByMonth(month);
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,data);
    }

    /**
     * 根据传入的日期编辑可预约人数或者添加可预约人数和日期
     * @param orderSetting
     * @return
     */
    @PostMapping("editNumberByDate")
    public Result editNumberByDate(@Validated @RequestBody OrderSetting orderSetting){
        orderSettingService.editNumberByDate(orderSetting);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
