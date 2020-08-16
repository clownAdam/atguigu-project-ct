package com.atguigu.ct.common.bean;

/**
 * 数据对象
 *
 * @author clown
 */
public abstract class Data implements Val {
    /**
     * 内容
     */
    public String content;


    @Override
    public String getValue() {
        return content;
    }

    /**
     * 设置值
     *
     * @param val 值
     */

    @Override
    public void setValue(Object val) {
        content = (String) val;
    }
}
