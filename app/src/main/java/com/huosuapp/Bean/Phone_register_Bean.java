package com.huosuapp.Bean;

/**
 * Created by admin on 2016/8/29.
 */
public class Phone_register_Bean {

    /**
     * code : 201
     * msg : 注册成功
     * data : {"identifier":"28557c42f95c5adf","accesstoken":"bKa06V5a0C708V3h5R2X3Jf31B0tbu2W1t601ndC3266fu2jd5aq1V377d2i77fn","expaire_time":1473079829}
     */

    private int code;
    private String msg;
    /**
     * identifier : 28557c42f95c5adf
     * accesstoken : bKa06V5a0C708V3h5R2X3Jf31B0tbu2W1t601ndC3266fu2jd5aq1V377d2i77fn
     * expaire_time : 1473079829
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String identifier;
        private String accesstoken;
        private int expaire_time;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getAccesstoken() {
            return accesstoken;
        }

        public void setAccesstoken(String accesstoken) {
            this.accesstoken = accesstoken;
        }

        public int getExpaire_time() {
            return expaire_time;
        }

        public void setExpaire_time(int expaire_time) {
            this.expaire_time = expaire_time;
        }
    }
}
