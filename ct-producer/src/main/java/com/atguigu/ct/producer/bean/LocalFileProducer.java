package com.atguigu.ct.producer.bean;


import com.atguigu.ct.common.bean.DataIn;
import com.atguigu.ct.common.bean.DataOut;
import com.atguigu.ct.common.bean.Producer;
import com.atguigu.ct.common.util.DateUtil;
import com.atguigu.ct.common.util.NumberUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 本地数据文件生产者
 *
 * @author clown
 */
public class LocalFileProducer implements Producer {
    private DataIn in;
    private DataOut out;
    private volatile boolean flag = true;

    @Override
    public void setIn(DataIn in) {
        this.in = in;
    }

    @Override
    public void setOut(DataOut out) {
        this.out = out;
    }

    /**
     * 生产数据
     */
    @Override
    public void produce() {
        try {
            //读取通讯录的数据
            List<Contact> contacts = in.read(Contact.class);
            while (flag) {
                //从通讯录中随机查找2个电话号码(主叫,被叫)
                int coll1Index = new Random().nextInt(contacts.size());
                int call2Index;
                while (true){
                    call2Index = new Random().nextInt(contacts.size());
                    if (coll1Index != call2Index) {
                        break;
                    }
                }
                Contact call1 = contacts.get(coll1Index);
                Contact call2 = contacts.get(call2Index);

                //生成随机的通话时间2018年全年
                String startDate = "20180101000000";
                String endDate = "20190101000000";

                long startTime = DateUtil.parse(startDate, "yyyyMMddHHmmss").getTime();
                long endTime = DateUtil.parse(endDate, "yyyyMMddHHmmss").getTime();
                //通话时间
                long callTime = startTime+(long)((endTime-startTime)*Math.random());
                //通话时间字符串
                String callTimeString = DateUtil.format(new Date(callTime), "yyyyMMddHHmmss");
                //生成随机的通话时长
                String duration = NumberUtil.format(new Random().nextInt(3000), 4, "0");
                //生成通话记录
                CallLog callLog = new CallLog(call1.getTel(), call2.getTel(), callTimeString, duration);
                //将通话记录刷洗到数据文件中
                System.out.println(callLog);
                out.write(callLog);
                //休眠0.5s
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭生产者
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }
}
