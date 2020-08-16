package com.atguigu.ct.common.bean;

import java.io.Closeable;

/**
 * 生产者接口
 *
 * @author clown
 */
public interface Producer extends Closeable {
    /**
     * 数据来源
     *
     * @param in 数据来源
     */
    public void setIn(DataIn in);

    /**
     * 数据的输出
     *
     * @param out 数据的输出
     */
    public void setOut(DataOut out);

    /**
     * 生产数据
     */
    public void produce();
}
