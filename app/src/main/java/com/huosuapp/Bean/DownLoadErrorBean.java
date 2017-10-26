package com.huosuapp.Bean;

import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
public class DownLoadErrorBean {

    /**
     * code : 404
     * msg : 未找到下载地址
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
