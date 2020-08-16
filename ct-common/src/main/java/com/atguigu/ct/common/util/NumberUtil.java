package com.atguigu.ct.common.util;

import java.text.DecimalFormat;

/**
 * 数字的工具类
 * @author clown
 */
public class NumberUtil {
    /**
     * 将数字格式化为固定长度字符串
     * @param num 数
     * @param length 格式化长度
     * @param prefix 长度不够填充
     * @return 字符串
     */
    public static String format(int num,int length,String prefix){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(prefix);
        }
        DecimalFormat df = new DecimalFormat(stringBuilder.toString());
        String format = df.format(num);
        return format;
    }

    public static void main(String[] args) {
        String format = format(10, 5,"0");
        System.out.println(format);
    }
}
