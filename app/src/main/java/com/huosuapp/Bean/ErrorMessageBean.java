package com.huosuapp.Bean;

import java.util.List;

/**
 * Created by admin on 2016/9/1.
 */
public class ErrorMessageBean {

    /**
     * code : 400
     * msg : 用户名不存在
     * data : []
    */

    private int code;
    private String msg;
    private List<?> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
