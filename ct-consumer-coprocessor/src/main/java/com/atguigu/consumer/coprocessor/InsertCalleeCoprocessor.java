package com.atguigu.consumer.coprocessor;

import com.atguigu.ct.common.bean.BaseDao;
import com.atguigu.ct.common.constant.Names;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 使用协处理器保存被叫用户的数据
 *  协处理器的使用：
 *      1.创建类
 *      2.让表知道协处理类(和表有关联)
 *
 * @author clown
 */
public class InsertCalleeCoprocessor extends BaseRegionObserver {
    /**
     * put之后:
     *  保存主叫用户数据之后,由hbase保存被叫用户数据
     * @param e
     * @param put
     * @param edit
     * @param durability
     * @throws IOException
     */
    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        //获取表
        Table table = e.getEnvironment().getTable(TableName.valueOf(Names.TABLE.getValue()));
        //保存数据
//        String calleeRowKey = genRegionNum(call2,callTime)+"_"+call2+"_"+callTime+"_"+call1+"_"+duration+"_0";
        CoprocessorDao dao = new CoprocessorDao();
        //主叫用户rowkey
        String rowKey = Bytes.toString(put.getRow());
        String[] values = rowKey.split("_");
        String call1=values[1];
        String call2=values[2];
        String callTime=values[3];
        String duration=values[4];
        String flg=values[5];
        String calleeRowKey=dao.genRegionNum(call2,callTime)+"_"+call2+"_"+callTime+"_"+call1+"_"+duration+"_0";
        if("1".equals(flg)){
            Put un_put = new Put(Bytes.toBytes(calleeRowKey));
            byte[] calleeFamily = Bytes.toBytes(Names.CF_CALLEE.getValue());
            un_put.addColumn(calleeFamily, Bytes.toBytes("call1"), Bytes.toBytes(call2));
            un_put.addColumn(calleeFamily, Bytes.toBytes("call2"), Bytes.toBytes(call1));
            un_put.addColumn(calleeFamily, Bytes.toBytes("callTime"), Bytes.toBytes(callTime));
            un_put.addColumn(calleeFamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
            un_put.addColumn(calleeFamily, Bytes.toBytes("flg"), Bytes.toBytes("0"));
            table.put(un_put);
            table.close();
        }
    }
    private class CoprocessorDao extends BaseDao{
        @Override
        protected int genRegionNum(String tel, String date) {
            return super.genRegionNum(tel, date);
        }
    }
}
