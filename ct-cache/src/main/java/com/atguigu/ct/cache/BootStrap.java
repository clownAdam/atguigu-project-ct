package com.atguigu.ct.cache;

import com.atguigu.ct.common.util.JDBCUtil;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 启动缓存客户端,向redis中增加缓存数据
 *
 * @author clown
 */
public class BootStrap {

    public static void main(String[] args) {
        Map<String, Integer> userMap = new HashMap<>();
        Map<String, Integer> dateMap = new HashMap<>();
        //读取mysql中的数据
        //读取用户,时间数据
        String queryUserSql = "select id,tel from ct_user";
        String queryDateSql = "select id,year,month,day from ct_date";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(queryUserSql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String tel = rs.getString("tel");
                userMap.put(tel, id);
            }
            rs.close();
            ps = conn.prepareStatement(queryDateSql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String year = rs.getString("year");
                String month = rs.getString("month");
                if (month.length() == 1) {
                    month = "0" + month;
                }
                String day = rs.getString("day");
                if (day.length() == 1) {
                    day = "0" + day;
                }
                dateMap.put(year + month + day, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println(userMap.size());
        System.out.println(dateMap.size());
        //向redis中存储数据
        Jedis jedis = new Jedis("bd-104", 6379);
        Iterator<String> keyIterator = userMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Integer value = userMap.get(key);
            jedis.hset("ct_user", key, ""+value);
        }
        keyIterator = dateMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Integer value = dateMap.get(key);
            jedis.hset("ct_date", key, ""+value);
        }
    }
}
