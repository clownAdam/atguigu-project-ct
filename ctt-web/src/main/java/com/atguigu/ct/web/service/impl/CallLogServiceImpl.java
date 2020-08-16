package com.atguigu.ct.web.service.impl;

import com.atguigu.ct.web.bean.CallLog;
import com.atguigu.ct.web.dao.CallLogDao;
import com.atguigu.ct.web.service.CallLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通话日志服务对象
 *
 * @author clown
 */
@Service
public class CallLogServiceImpl implements CallLogService {
    @Autowired
    private CallLogDao callLogDao;

    /**
     * 查询用户指定时间的通话统计信息
     *
     * @param tel
     * @param callTime
     * @return
     */
    @Override
    public List<CallLog> queryMonthDates(String tel, String callTime) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tel", tel);
        if (callTime.length() > 4) {
            callTime = callTime.substring(0, 4);
        }
        paramMap.put("callTime", callTime);
        return callLogDao.queryMonthDates(paramMap);
    }
}
