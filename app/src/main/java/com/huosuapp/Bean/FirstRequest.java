package com.huosuapp.Bean;

import java.util.List;

/**
 * Created by admin on 2016/8/19.
 */
public class FirstRequest {


    /**
     * code : 201
     * msg : 请求成功
     * data : {"version":{"hasnew":0,"newurl":""},"splash":{"cnt":0,"img":[]},"helpinfo":{"qq":"3204451729","wx":"","qqgroup":"","tel":"13659658453"},"time":1471661608}
     */

    private int code;
    private String msg;
    /**
     * version : {"hasnew":0,"newurl":""}
     * splash : {"cnt":0,"img":[]}
     * helpinfo : {"qq":"3204451729","wx":"","qqgroup":"","tel":"13659658453"}
     * time : 1471661608
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
        /**
         * hasnew : 0
         * newurl :
         */

        private VersionBean version;
        /**
         * cnt : 0
         * img : []
         */

        private SplashBean splash;
        /**
         * qq : 3204451729
         * wx :
         * qqgroup :
         * tel : 13659658453
         */

        private HelpinfoBean helpinfo;
        private int time;

        public VersionBean getVersion() {
            return version;
        }

        public void setVersion(VersionBean version) {
            this.version = version;
        }

        public SplashBean getSplash() {
            return splash;
        }

        public void setSplash(SplashBean splash) {
            this.splash = splash;
        }

        public HelpinfoBean getHelpinfo() {
            return helpinfo;
        }

        public void setHelpinfo(HelpinfoBean helpinfo) {
            this.helpinfo = helpinfo;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public static class VersionBean {
            private int hasnew;
            private String newurl;

            public int getHasnew() {
                return hasnew;
            }

            public void setHasnew(int hasnew) {
                this.hasnew = hasnew;
            }

            public String getNewurl() {
                return newurl;
            }

            public void setNewurl(String newurl) {
                this.newurl = newurl;
            }
        }

        public static class SplashBean {
            private int cnt;
            private List<?> img;

            public int getCnt() {
                return cnt;
            }

            public void setCnt(int cnt) {
                this.cnt = cnt;
            }

            public List<?> getImg() {
                return img;
            }

            public void setImg(List<?> img) {
                this.img = img;
            }
        }

        public static class HelpinfoBean {
            private String qq;
            private String wx;
            private String qqgroup;
            private String tel;

            public String getQq() {
                return qq;
            }

            public void setQq(String qq) {
                this.qq = qq;
            }

            public String getWx() {
                return wx;
            }

            public void setWx(String wx) {
                this.wx = wx;
            }

            public String getQqgroup() {
                return qqgroup;
            }

            public void setQqgroup(String qqgroup) {
                this.qqgroup = qqgroup;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }
        }
    }
}
