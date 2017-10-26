package com.huosuapp.Bean;

/**
 * Created by admin on 2016/8/25.
 */
public class TimeStampBean {

    /**
     * code : 200
     * msg : 获取成功
     * data : {"time":1472127735}
     */

    private String code;
    private String msg;
    /**
     * time : 1472127735
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int time;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
