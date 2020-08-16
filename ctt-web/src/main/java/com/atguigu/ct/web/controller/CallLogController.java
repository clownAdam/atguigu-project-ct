package com.atguigu.ct.web.controller;

import com.atguigu.ct.web.bean.CallLog;
import com.atguigu.ct.web.service.CallLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 通话日志控制器对象
 * @author clown
 */
@Controller
public class CallLogController {
    @Autowired
    private CallLogService callLogService;
    @RequestMapping("/query")
    public String query(){
        return "query";
    }
//    @ResponseBody
    @RequestMapping("/view")
    public String view(String tel, String callTime, Model model){
        System.out.println("hello");
        //查询统计结果
       List<CallLog> logs = callLogService.queryMonthDates(tel,callTime);
        System.out.println("logs = " + logs);
        model.addAttribute("callLogs",logs);
        return "view";
    }
}
