package com.atguigu.ct.common.bean;

import java.io.Closeable;

/**
 * 生产-->数据的输出
 *
 * @author clown
 */
public interface DataOut extends Closeable {
    /**
     * 设置文件输入路径
     *
     * @param path
     */
    public void setPath(String path);

    /**
     * 将数据写出
     *
     * @param data 写出数据
     * @throws Exception
     */
    public void write(Object data) throws Exception;

    /**
     * 将数据写出
     *
     * @param data 写出数据
     * @throws Exception
     */
    public void write(String data) throws Exception;
}
