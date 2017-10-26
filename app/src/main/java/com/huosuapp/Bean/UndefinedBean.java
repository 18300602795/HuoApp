package com.huosuapp.Bean;

import java.io.Serializable;

/**
 * 用于保存个人信息的类
 */

public class UndefinedBean implements Serializable {
    public String pet_name;//昵称
    public String sex;//性别
    public String happy_brithday;//生日日期
    public String signature;//签名

    public String getSex() {
        return sex;
    }

    public String getHappy_brithday() {
        return happy_brithday;
    }

    public String getSignature() {
        return signature;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setHappy_brithday(String happy_brithday) {
        this.happy_brithday = happy_brithday;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
