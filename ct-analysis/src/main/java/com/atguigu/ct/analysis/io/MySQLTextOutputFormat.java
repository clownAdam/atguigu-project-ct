package com.atguigu.ct.analysis.io;

import com.atguigu.ct.common.util.JDBCUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * mysql数据格式化的输出对象
 *
 * @author clown
 */
public class MySQLTextOutputFormat extends OutputFormat<Text, Text> {

    protected static class MySQLRecordWriter extends RecordWriter<Text, Text> {
        private Connection conn = null;
        private Jedis jedis = null;
        private Map<String, Integer> userMap = new HashMap<>();
        private Map<String, Integer> dateMap = new HashMap<>();

        public MySQLRecordWriter() {
            //获取资源
            conn = JDBCUtil.getConnection();
            jedis = new Jedis("bd-104",6379);
        }

        /**
         * 输出数据
         *
         * @param key
         * @param value
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void write(Text key, Text value) throws IOException, InterruptedException {
            String insertSql = "insert into ct_call(telId,dateId,sumCall,sumDuration) values(?,?,?,?)";
            PreparedStatement ps = null;
            String[] keys = key.toString().split("_");
            String tel = keys[0];
            String date = keys[1];
            String[] values = value.toString().split("_");
            String sumCall = values[0];
            String sumDuration = values[1];
            try {
                ps = conn.prepareStatement(insertSql);
                ps.setInt(1, Integer.parseInt(jedis.hget("ct_user",tel)));
                ps.setInt(2, Integer.parseInt(jedis.hget("ct_date",date)));
                ps.setInt(3, Integer.parseInt(sumCall));
                ps.setInt(4, Integer.parseInt(sumDuration));
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * 释放资源
         *
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    /**
     * 负责将数据输出到mysql
     *
     * @param taskAttemptContext
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public RecordWriter<Text, Text> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new MySQLRecordWriter();
    }

    /**
     * @param jobContext
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException {

    }

    /**
     * @param taskAttemptContext
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private FileOutputCommitter committer = null;

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

        if (committer == null) {
            Path output = getOutputPath(taskAttemptContext);
            committer = new FileOutputCommitter(output, taskAttemptContext);
        }
        return committer;
    }

    public Path getOutputPath(JobContext job) {
        String name = job.getConfiguration().get(FileOutputFormat.OUTDIR);
        return name == null ? null : new Path(name);
    }
}
