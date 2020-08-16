package com.atguigu.ct.consumer;

import com.atguigu.ct.common.bean.Consumer;
import com.atguigu.ct.consumer.bean.CallLogConsumer;

import java.io.IOException;

/**
 * 启动消费者
 * 使用kafka的消费者获取flume采集的数据
 * 将数据存储到hbase中
 *
 * @author clown
 */
public class BootStrap {
    public static void main(String[] args) throws IOException {
        //创建消费者
        Consumer consumer = new CallLogConsumer();
        //消费数据
        consumer.consumer();
        //关闭资源
        consumer.close();
    }
}
