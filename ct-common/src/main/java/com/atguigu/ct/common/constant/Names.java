package com.atguigu.ct.common.constant;

import com.atguigu.ct.common.bean.Val;

/**
 * 名称常量枚举类
 *
 * @author Administrator
 */
public enum Names implements Val {
    /*命名空间*/
    NAMESPACE("ct"),
    TABLE("ct:calllog"),
    CF_CALLER("caller"),
    CF_CALLEE("callee"),
    CF_INFO("info"),
    /*主题topic*/
    TOPIC("ct");
    private String name;

    private Names(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public void setValue(Object val) {
        this.name = (String) val;
    }
}
