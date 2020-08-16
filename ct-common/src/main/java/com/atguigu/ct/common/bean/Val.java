package com.atguigu.ct.common.bean;

/**
 * 值对象接口
 *
 * @author clown
 */
public interface Val {
    /**
     * 获取值
     *
     * @return
     */
    public Object getValue();

    /**
     * 设置值
     *
     * @param val 值属性
     * @return
     */
    public void setValue(Object val);
}
