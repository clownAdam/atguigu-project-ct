package com.atguigu.ct.common.bean;

import com.atguigu.ct.common.api.Column;
import com.atguigu.ct.common.api.RowKey;
import com.atguigu.ct.common.api.TableRef;
import com.atguigu.ct.common.constant.Names;
import com.atguigu.ct.common.constant.ValueConstant;
import com.atguigu.ct.common.util.DateUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * 基础数据访问对象
 *
 * @author clown
 */
public abstract class BaseDao {

    private ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();
    private ThreadLocal<Admin> adminHolder = new ThreadLocal<Admin>();

    /**
     * 开始
     *
     * @throws IOException
     */
    protected void start() throws IOException {
        getConnection();
        getAdmin();
    }

    /**
     * 结束
     */
    protected void end() throws IOException {
        Admin admin = getAdmin();
        if (admin != null) {
            admin.close();
            adminHolder.remove();
        }
        Connection conn = getConnection();
        if (conn != null) {
            conn.close();
            connHolder.remove();
        }
    }

    /**
     * 创建命名空间，如果存在，不需要创建，否则创建新的。
     *
     * @param namespace
     */
    protected void createNameSpaceNX(String namespace) throws IOException {
        Admin admin = getAdmin();
        try {
            admin.getNamespaceDescriptor(namespace);
        } catch (NamespaceNotFoundException e) {
            /*e.printStackTrace();*/
            NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();
            admin.createNamespace(namespaceDescriptor);
        }

    }

    /**
     * 如果表存在,删除重新创建
     * 没有，直接创建
     *
     * @param families 列族
     * @param t_name   表名
     */
    protected void createTableXX(String t_name, String... families) throws IOException {
        createTableXX(t_name, null, families);
    }

    /**
     * 如果表存在,删除重新创建
     * 没有，直接创建
     *
     * @param families    列族
     * @param regionCount 分区数
     * @param t_name      表名
     */
    protected void createTableXX(String t_name, Integer regionCount, String... families) throws IOException {
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(t_name);
        if (admin.tableExists(tableName)) {
            //表存在,删除
            deleteTable(t_name);
        }
        //创建表
        createTable(t_name, regionCount, families);
    }
    /**
     * 如果表存在,删除重新创建
     * 没有，直接创建
     *
     * @param families    列族
     * @param regionCount 分区数
     * @param t_name      表名
     */
    protected void createTableXX(String t_name,String coprocessorClass, Integer regionCount, String... families) throws IOException {
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(t_name);
        if (admin.tableExists(tableName)) {
            //表存在,删除
            deleteTable(t_name);
        }
        //创建表
        createTable(t_name,coprocessorClass, regionCount, families);
    }



    /**
     * 创建表
     *
     * @param t_name      表名
     * @param regionCount 分区数
     * @param families    列族
     * @throws IOException
     */
    private void createTable(String t_name, Integer regionCount, String... families) throws IOException {
        createTable(t_name, null, regionCount, families);
    }

    /**
     * 创建表(协处理器)
     *
     * @param t_name      表名
     * @param regionCount 分区数
     * @param families    列族
     * @throws IOException
     */
    private void createTable(String t_name, String coprocessorClass, Integer regionCount, String... families) throws IOException {
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(t_name);
        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
        if (families == null || families.length == 0) {
            families = new String[1];
            families[0] = Names.CF_INFO.getValue();
        }
        for (String family : families) {
            HColumnDescriptor columnDescriptor = new HColumnDescriptor(family);
            tableDescriptor.addFamily(columnDescriptor);
        }
        if (coprocessorClass != null && !"".equals(coprocessorClass)) {
            tableDescriptor.addCoprocessor(coprocessorClass);
        }
        //增加预分区
        if (regionCount == null || regionCount <= 1) {
            //创建表
            admin.createTable(tableDescriptor);
        } else {
            //分区键
            byte[][] splitKeys = genSplitKeys(regionCount);
            //创建表
            admin.createTable(tableDescriptor, splitKeys);
        }

    }

    /**
     * 生成分区键
     *
     * @param regionCount
     * @return
     */
    private byte[][] genSplitKeys(Integer regionCount) {
        int splitKeyCount = regionCount - 1;
        byte[][] bs = new byte[splitKeyCount][];
        //   0|,1|,2|,3|,4|
        //   (-∞,0|),[0|,1|),[1|,+∞),
        List<byte[]> bsList = new ArrayList<>();
        for (int i = 0; i < splitKeyCount; i++) {
            String splitKey = i + "|";
            bsList.add(Bytes.toBytes(splitKey));
        }
        Collections.sort(bsList, new Bytes.ByteArrayComparator());
        bsList.toArray(bs);
        return bs;
    }

    /**
     * 获取查询时startrow,stoprow集合
     *
     * @param tel
     * @param start
     * @param end
     * @return
     */
    protected List<String[]> getStartStopRowKeys(String tel, String start, String end) {
        ArrayList<String[]> rowkeys = new ArrayList<>();
        String startTime = start.substring(0, 6);
        String endTime = end.substring(0, 6);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(DateUtil.parse(startTime, "yyyyMM"));
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(DateUtil.parse(endTime, "yyyyMM"));

        while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
            String nowTime = DateUtil.format(startCal.getTime(), "yyyyMM");
            int regionNum = genRegionNum(tel, nowTime);
            String startRow = regionNum + "_" + tel + "_" + nowTime;
            String stopRow = startRow + "|";
            String[] rowkey = {startRow, stopRow};
            rowkeys.add(rowkey);
            startCal.add(Calendar.MONTH, 1);
        }
        return rowkeys;
    }
/*
    public static void main(String[] args) {
        for (String[] st : getStartStopRowKeys("13334563456", "201803", "201808")) {
            System.out.println(st[0]+"~~"+st[1]);
        }
    }
*/

    /**
     * 计算分区号
     *
     * @param tel  电话
     * @param date 时间
     * @return
     */
    protected int genRegionNum(String tel, String date) {
        String userCode = tel.substring(tel.length() - 4);
        String yearMonth = date.substring(0, 6);
        int userCodeHash = userCode.hashCode();
        int yearMonthHash = yearMonth.hashCode();
        /**
         * crc校验:采用异或算法
         */
        int crc = Math.abs(userCodeHash ^ yearMonthHash);

        //取模
        int regionNum = crc % ValueConstant.REGION_COUNT;
        return regionNum;
    }

    /**
     * 增加数据到hbase
     *
     * @param t_name 表名
     * @param put    数据增加方式
     */
    protected void putData(String t_name, Put put) throws IOException {
        //获取表对象
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(t_name));
        //增加数据
        table.put(put);
        //关闭表
        table.close();
    }

    /**
     * 增加多条数据到hbase
     *
     * @param t_name 表名
     * @param puts   数据list增加方式
     */
    protected void putData(String t_name, List<Put> puts) throws IOException {
        //获取表对象
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(t_name));
        //增加数据
        table.put(puts);
        //关闭表
        table.close();
    }

    /**
     * 增加对象数据到hbase
     */
    protected void putData(Object obj) throws Exception {
        //反射
        Class clazz = obj.getClass();
        TableRef tableRef = (TableRef) clazz.getAnnotation(TableRef.class);
        Field[] fs = clazz.getDeclaredFields();
        String stringRowKey = "";
        for (Field f : fs) {
            RowKey rowKey = f.getAnnotation(RowKey.class);
            if (rowKey != null) {
                f.setAccessible(true);
                stringRowKey = (String) f.get(obj);
                break;
            }
        }
        String tableName = tableRef.value();
        //获取表对象
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(tableName));
        //增加数据

        Put put = new Put(Bytes.toBytes(stringRowKey));
        for (Field f : fs) {
            Column column = f.getAnnotation(Column.class);
            if (column != null) {
                String family = column.family();
                String col_name = column.column();
                if (col_name == null || "".equals(col_name)) {
                    col_name = f.getName();
                }
                f.setAccessible(true);
                String value = (String) f.get(obj);
                put.addColumn(Bytes.toBytes(family), Bytes.toBytes(col_name), Bytes.toBytes(value));
            }
        }
        table.put(put);
        //关闭表
        table.close();
    }

    /**
     * 删除表
     *
     * @param t_name 表名
     * @throws IOException
     */
    protected void deleteTable(String t_name) throws IOException {
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(t_name);
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
    }

    /**
     * 获取连接对象
     */
    protected synchronized Connection getConnection() throws IOException {
        Connection conn = connHolder.get();
        if (conn == null) {
            Configuration conf = HBaseConfiguration.create();
            conn = ConnectionFactory.createConnection(conf);
            connHolder.set(conn);
        }
        return conn;
    }

    /**
     * 获取管理对象
     */
    protected synchronized Admin getAdmin() throws IOException {
        Admin admin = adminHolder.get();
        if (admin == null) {
            admin = getConnection().getAdmin();
            adminHolder.set(admin);
        }
        return admin;
    }

}
