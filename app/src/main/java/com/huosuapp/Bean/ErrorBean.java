package com.huosuapp.Bean;

import java.util.Objects;

/**
 * Created by admin on 2016/9/7.
 */
public class ErrorBean {

    /**
     * code : 302
     * msg : 礼包已领完
     * data : []
     */

    private String code;
    private String msg;
    private Objects data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Objects getData() {
        return data;
    }

    public void setData(Objects data) {
        this.data = data;
    }
}
