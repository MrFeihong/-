package com.baidu.health.pojo;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

/**
 * 预约设置
 */
public class OrderSetting implements Serializable{
    private Integer id ;

    @Future(message = "预约日期不能为过去日期")
    private Date orderDate;//预约设置日期

    @Min(value = 1,message = "可预约人数不能低于1人！！")
    @Max(value = 99999,message = "可预约人数不能超过99999人！！")
    private Integer number;//可预约人数

    @Min(value = 1,message = "已预约人数不能低于1人！！")
    @Max(value = 99999,message = "已预约人数不能超过99999人！！")
    private Integer reservations ;//已预约人数

    public OrderSetting() {
    }

    public OrderSetting(Date orderDate, int number) {
        this.orderDate = orderDate;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Integer getReservations() {
        return reservations;
    }

    public void setReservations(int reservations) {
        this.reservations = reservations;
    }
}
