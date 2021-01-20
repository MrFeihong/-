package com.baidu.health.service;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/15
 */
public interface ReportService {
    /**
     * 运营数据统计
     * @return
     */
    Map<String, Object> getBusinessReportData();
}
