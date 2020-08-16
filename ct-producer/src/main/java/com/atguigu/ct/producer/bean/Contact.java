package com.atguigu.ct.producer.bean;

import com.atguigu.ct.common.bean.Data;

/**
 * 联系人
 * @author clown
 */
public class Contact extends Data {
    private String tel;
    private String name;

    @Override
    public void setValue(Object val) {
        content = (String)val;
        String[] values = content.split("\t");
        setTel(values[0]);
        setName(values[1]);
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Contact["+tel+","+name+"]";
    }
}