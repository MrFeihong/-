package com.baidu.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.dao.OrderSettingDao;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.OrderSetting;
import com.baidu.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 上传Excel文件批量导入
     * @param orderSettingList
     */
    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) {
        // 先判断orderSettingList是否为空 isEmpty为空返回true !不会为空进入
        if (!CollectionUtils.isEmpty(orderSettingList)) {
            // 遍历导入的预约设置信息List<OrderSetting>
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (OrderSetting orderSetting : orderSettingList) {
                // 根据拿到的单个日 中的日期查询数据库是否有这个日期
                OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
                // 不为空则修改该日的信息
                if (null != osInDB) {
                    // 传入需要修改的可预约人数 不能小于已预约的人数
                    if (osInDB.getReservations() > orderSetting.getNumber()) {
                        // 数据库中的已经预约大于修改的可预约人数就报错
                        throw new BusinessException(sdf.format(orderSetting.getOrderDate()) + ": 最大可预约数不能小于已预约人数");
                    }else {
                        // 修改前判断一下用户传入的可预约数是否和数据库的可预约数一样
                        if (orderSetting.getNumber()==osInDB.getNumber()){
                            // 一样就不做修改 因为是批量导入不能相同报错
                            return;
                        }
                        // 修改的可预约数大于等于已预约数 则可以修改该日信息
                        orderSettingDao.updateNumber(orderSetting);
                    }
                } else {
                // 为空 走到这里说明没有查询到数据则添加数据
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * 通过月份查询可预约人数和已预约人数
     * @param month
     * @return
     */
    @Override
    public List<Map<String, Integer>> findOrderSettingByMonth(String month) {
        // 添加上模糊查询的条件
        month+="%";
        return orderSettingDao.findOrderSettingByMonth(month);
    }

    /**
     * 根据传入的日期编辑可预约人数或者添加可预约人数和日期
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 根据传入日期查询该日期是否存在 存在则编辑可预约人数不存在则是添加可预约人数和日期
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        // 不为空则修改该日的可预约人数
        if (null != osInDB) {
            // 传入需要修改的可预约人数 不能小于已预约的人数
            if (osInDB.getReservations() > orderSetting.getNumber()) {
                // 数据库中的已经预约大于修改的可预约人数就报错
                throw new BusinessException(sdf.format(orderSetting.getOrderDate()) + ": 最大可预约数不能小于已预约人数");
            }else {
                // 修改前判断一下用户传入的可预约数是否和数据库的可预约数一样
                if (orderSetting.getNumber()==osInDB.getNumber()){
                    // 一样就不做修改 并提示用户修改的值和之前一样
                    throw new BusinessException(sdf.format(orderSetting.getOrderDate()) + "修改的最大预约数和之前一致！！");
                }
                // 修改的可预约数大于等于已预约数 则可以修改该日信息
                orderSettingDao.updateNumber(orderSetting);
            }
        } else {
            // 为空 走到这里说明没有查询到数据则添加数据
            orderSettingDao.add(orderSetting);
        }
    }

    /**
     * 根据日期删除预约设置
     *
     * @param date
     */
    @Override
    public void cleanOrderSetting(Date date) {
        orderSettingDao.cleanOrderSetting(date);
    }

}
