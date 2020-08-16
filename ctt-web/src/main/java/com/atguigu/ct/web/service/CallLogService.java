package com.atguigu.ct.web.service;

import com.atguigu.ct.web.bean.CallLog;

import java.util.List;

/**
 * @author clown
 */
public interface CallLogService {
    List<CallLog> queryMonthDates(String tel, String callTime);
}
