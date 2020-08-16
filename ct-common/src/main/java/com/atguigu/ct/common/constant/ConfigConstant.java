package com.atguigu.ct.common.constant;

import java.util.*;

/**
 * 配置常量
 *
 * @author clown
 */
public class ConfigConstant {
    private static Map<String,String> valueMap = new HashMap<String,String>();
    static{
        Properties properties = new Properties();
        ResourceBundle ct = ResourceBundle.getBundle("ct");
        Enumeration<String> enums = ct.getKeys();
        while(enums.hasMoreElements()){
            String key = enums.nextElement();
            String value = ct.getString(key);
            valueMap.put(key,value);
        }
    }
    public static String getVal(String key){
        return valueMap.get(key);
    }

    public static void main(String[] args) {
        String val = ConfigConstant.getVal("ct.cf.caller");
        System.out.println(val);
    }
}
