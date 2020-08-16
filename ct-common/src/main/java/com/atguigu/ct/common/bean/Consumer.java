package com.atguigu.ct.common.bean;

import java.io.Closeable;

/**
 * 消费者接口(获取)
 * @author clown
 */
public interface Consumer extends Closeable {
    /**
     * 消费数据
     */
    public void consumer();
}
