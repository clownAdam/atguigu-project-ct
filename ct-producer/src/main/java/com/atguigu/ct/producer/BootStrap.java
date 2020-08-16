package com.atguigu.ct.producer;

import com.atguigu.ct.common.bean.Producer;
import com.atguigu.ct.producer.bean.LocalFileProducer;
import com.atguigu.ct.producer.io.LocalFileDataIn;
import com.atguigu.ct.producer.io.LocalFileDataOut;

import java.io.IOException;

/**
 * 启动对象
 * @author clown
 */
public class BootStrap {
    public static void main(String[] args) throws IOException {
        if(args == null || args.length<=0){
            System.out.println("no arguments");
            System.exit(1);
        }
        if(args.length<2){
            System.out.println("系统参数不正确,请按照指定格式传递:java -jar *.jar inputPath outputPath");
            System.exit(1);
        }
        //构建生产者对象
        Producer producer = new LocalFileProducer();

        producer.setIn(new LocalFileDataIn(args[0]));
        producer.setOut(new LocalFileDataOut(args[1]));
        //生产数据
        producer.produce();
        //关闭生产者对象
        producer.close();
    }
}
