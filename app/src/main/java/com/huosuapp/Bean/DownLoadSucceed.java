package com.huosuapp.Bean;

/**
 * Created by admin on 2016/9/2.
 */
public class DownLoadSucceed {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"url":"http://www.9game.cn/game/down_725862_8673971.html"}
     */

    private int code;
    private String msg;
    /**
     * url : http://www.9game.cn/game/down_725862_8673971.html
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
        private String url;
        private long downcnt;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getDowncnt() {
            return downcnt;
        }

        public void setDowncnt(long downcnt) {
            this.downcnt = downcnt;
        }
    }
}
