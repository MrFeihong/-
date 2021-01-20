package com.baidu.health.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 月份信息
 */
public class MonthTime implements Serializable {
    private Date startTime;//起始时间
    private Date endTime;//截止时间

    public MonthTime(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "MonthTime{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
