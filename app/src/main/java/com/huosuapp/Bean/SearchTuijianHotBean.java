package com.huosuapp.Bean;

import java.util.List;

/**
 * Created by admin on 2016/8/26.
 */
public class SearchTuijianHotBean {

    /**
     * code : 200
     * msg : 请求成功
     * data : ["神武赵子龙","斗罗大陆","再来一瓶","无边战火"]
     */

    private String code;
    private String msg;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
