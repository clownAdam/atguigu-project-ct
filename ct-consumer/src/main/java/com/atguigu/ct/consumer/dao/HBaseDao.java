package com.atguigu.ct.consumer.dao;

import com.atguigu.ct.common.bean.BaseDao;
import com.atguigu.ct.common.constant.Names;
import com.atguigu.ct.common.constant.ValueConstant;
import com.atguigu.ct.consumer.bean.CallLog;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * HBase数据访问对象
 *
 * @author clown
 */
public class HBaseDao extends BaseDao {
    /**
     * 初始化
     */
    public void init() throws IOException {
        start();
        createNameSpaceNX(Names.NAMESPACE.getValue());
        String coprocessorClass ="com.atguigu.consumer.coprocessor.InsertCalleeCoprocessor";
        createTableXX(Names.TABLE.getValue(), coprocessorClass,ValueConstant.REGION_COUNT, Names.CF_CALLER.getValue(),Names.CF_CALLEE.getValue());
        end();
    }
    /**
     * 插入对象
     *
     * @param log
     */
    public void insertData(CallLog log) throws Exception{
        String rowKey = genRegionNum(log.getCall1(),log.getCallTime())+"_"+log.getCall1()+"_"+log.getCallTime()+"_"+log.getCall2()+"_"+log.getDuration();
        log.setRowKey(rowKey);
        putData(log);
    }


    /**
     * 插入数据
     *
     * @param value
     */
    public void insertData(String value) throws IOException {
        //将通话日志保存到hbase表中

        //获取通话日志数据
        String[] values = value.split("\t");
        //主叫
        String call1 = values[0];
        //被叫
        String call2 = values[1];
        //通话时间
        String callTime = values[2];
        //通话时长
        String duration = values[3];
        //创建数据对象
        /**
         * rowkey的设计
         * 1)长度原则:最大值64kb,推荐长度为10-100byte,最好是8的倍数,能短则短
         *              rowkey如果太长会影响性能
         * 2)唯一原则:rowkey应具备唯一性
         * 3)散列原则:
         *          3-1)盐值散列:不能直接使用时间戳直接作为rowkey.在rowkey前增加随机数
         *          3-2)字符串反转:电话号码|时间戳
         *          3-3)计算分区号:hashmap
         *  rowkey=regionNum + call1 + callTime + call2 + duration
         * */
        String rowKey = genRegionNum(call1,callTime)+"_"+call1+"_"+callTime+"_"+call2+"_"+duration+"_1";
        Put put = new Put(Bytes.toBytes(rowKey));
        byte[] family = Bytes.toBytes(Names.CF_CALLER.getValue());
        put.addColumn(family, Bytes.toBytes("call1"), Bytes.toBytes(call1));
        put.addColumn(family, Bytes.toBytes("call2"), Bytes.toBytes(call2));
        put.addColumn(family, Bytes.toBytes("callTime"), Bytes.toBytes(callTime));
        put.addColumn(family, Bytes.toBytes("duration"), Bytes.toBytes(duration));
        put.addColumn(family, Bytes.toBytes("flg"), Bytes.toBytes("1"));
///////////////////////////////////////////////////////////////////////////////////////
        /*String calleeRowKey = genRegionNum(call2,callTime)+"_"+call2+"_"+callTime+"_"+call1+"_"+duration+"_0";
        Put un_put = new Put(Bytes.toBytes(calleeRowKey));
        byte[] calleeFamily = Bytes.toBytes(Names.CF_CALLEE.getValue());
        un_put.addColumn(calleeFamily, Bytes.toBytes("call1"), Bytes.toBytes(call2));
        un_put.addColumn(calleeFamily, Bytes.toBytes("call2"), Bytes.toBytes(call1));
        un_put.addColumn(calleeFamily, Bytes.toBytes("callTime"), Bytes.toBytes(callTime));
        un_put.addColumn(calleeFamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
        un_put.addColumn(calleeFamily, Bytes.toBytes("flg"), Bytes.toBytes("0"));*/
        //保存数据
        ArrayList<Put> puts = new ArrayList<>();
        puts.add(put);
        putData(Names.TABLE.getValue(), puts);
    }
}
