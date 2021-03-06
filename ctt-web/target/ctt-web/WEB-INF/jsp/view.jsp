<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/8/15 0015
  Time: 23:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>view</title>
    <link href="/bootstrap/css/bootstrap.css" rel="stylesheet">
</head>
<body>
data：${callLogs}
<%--图标组件容器--%>
<div id="main" style="width: 800px;height:600px;"></div>
<script src="/jquery/jquery-2.1.1.min.js"></script>
<script src="/bootstrap/js/bootstrap.js"></script>
<script src="/jquery/echarts.min.js"></script>

<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    option = {
        title: {
            text: '用户通话统计',
            subtext: '纯属虚构'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['通话次数', '通话时长']
        },
        toolbox: {
            show: true,
            feature: {
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                data: [
                    <c:forEach items="${callLogs}" var="calllog">
                        ${calllog.dateId},
                    </c:forEach>
                ]
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '通话次数',
                type: 'bar',
                data: [
                    <c:forEach items="${callLogs}" var="calllog">
                        ${calllog.sumCall},
                    </c:forEach>
                ],
                markPoint: {
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            },
            {
                name: '通话时长',
                type: 'bar',
                data: [
                    <c:forEach items="${callLogs}" var="calllog">
                        ${calllog.sumDuration},
                    </c:forEach>
                ],
                markPoint: {
                    data: [
                        {name: '年最高', value: 182.2, xAxis: 7, yAxis: 183},
                        {name: '年最低', value: 2.3, xAxis: 11, yAxis: 3}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
</script>

</body>
</html>
