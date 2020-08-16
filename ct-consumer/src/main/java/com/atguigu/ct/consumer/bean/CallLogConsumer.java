package com.atguigu.ct.consumer.bean;

import com.atguigu.ct.common.bean.Consumer;
import com.atguigu.ct.common.constant.Names;
import com.atguigu.ct.consumer.dao.HBaseDao;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * 通话记录日志消费对象
 *
 * @author clown
 */
public class CallLogConsumer implements Consumer {

    /**
     * 消费数据
     */
    @Override
    public void consumer() {
        try {
            //创建配置对象
            Properties props = new Properties();
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("consumer.properties"));
            //获取flume采集的数据
            KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

            //关注主题,从主题获取数据
            consumer.subscribe(Arrays.asList(Names.TOPIC.getValue()));
            //hbase数据访问对象
            HBaseDao dao = new HBaseDao();
            //初始化
            dao.init();
            //消费数据
            while (true) {
                //返回采集的结果集
                ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    System.out.println(consumerRecord.value());
                    //18840172592	16574556259	20181010231644	1606
                    //存储--插入数据
                    dao.insertData(consumerRecord.value());
                    /*CallLog log = new CallLog(consumerRecord.value());
                    dao.insertData(log);*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {

    }
}
