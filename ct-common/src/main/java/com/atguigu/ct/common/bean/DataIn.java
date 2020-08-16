package com.atguigu.ct.common.bean;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * 生产-->数据的来源
 * @author clown
 */
public interface DataIn extends Closeable {
    /**
     * 设置文件输入路径
     * @param path
     */
    public void setPath(String path);

    /**
     * 读取数据
     * @return
     * @throws IOException
     */
    public Object read() throws IOException;
    /**
     * 读取数据,返回数据集合
     * @param clazz 泛型
     * @return
     * @throws IOException
     */
    public <T extends Data> List<T> read(Class<T> clazz) throws IOException;
}
