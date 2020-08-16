package com.atguigu.ct.web.dao;

import com.atguigu.ct.web.bean.CallLog;

import java.util.List;
import java.util.Map;

/**
 * 通话日志数据访问对象
 * @author clown
 */
public interface CallLogDao {
    List<CallLog> queryMonthDates(Map<String, Object> paramMap);
}
