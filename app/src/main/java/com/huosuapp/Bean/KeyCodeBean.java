package com.huosuapp.Bean;

import java.util.List;

/**
 * Created by liu hong liang on 2016/9/13.
 */
public class KeyCodeBean {


    /**
     * code : 200
     * msg : 请求成功
     * data : {"count":1,"gift_list":[{"giftid":8,"gameid":1,"giftname":"测试","total":1,"remain":1,"content":"","icon":"http://sdk.1tsdk.com/upload/20160905/57cd4461bf416.jpg","starttime":"2016-09-14 13:49:00","enttime":"2016-09-30 13:49:00","scope":"","giftcode":"","isget":0}]}
     */

    private int code;
    private String msg;
    /**
     * count : 1
     * gift_list : [{"giftid":8,"gameid":1,"giftname":"测试","total":1,"remain":1,"content":"","icon":"http://sdk.1tsdk.com/upload/20160905/57cd4461bf416.jpg","starttime":"2016-09-14 13:49:00","enttime":"2016-09-30 13:49:00","scope":"","giftcode":"","isget":0}]
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
        private int count;
        /**
         * giftid : 8
         * gameid : 1
         * giftname : 测试
         * total : 1
         * remain : 1
         * content :
         * icon : http://sdk.1tsdk.com/upload/20160905/57cd4461bf416.jpg
         * starttime : 2016-09-14 13:49:00
         * enttime : 2016-09-30 13:49:00
         * scope :
         * giftcode :
         * isget : 0
         */

        private List<GiftListBean> gift_list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<GiftListBean> getGift_list() {
            return gift_list;
        }

        public void setGift_list(List<GiftListBean> gift_list) {
            this.gift_list = gift_list;
        }

        public static class GiftListBean {
            private int giftid;
            private int gameid;
            private String giftname;
            private long total;
            private long remain;
            private String content;
            private String icon;
            private String starttime;
            private String enttime;
            private String scope;
            private String giftcode;
            private int isget;
            private String code;
            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }


            public int getGiftid() {
                return giftid;
            }

            public void setGiftid(int giftid) {
                this.giftid = giftid;
            }

            public int getGameid() {
                return gameid;
            }

            public void setGameid(int gameid) {
                this.gameid = gameid;
            }

            public String getGiftname() {
                return giftname;
            }

            public void setGiftname(String giftname) {
                this.giftname = giftname;
            }

            public long getTotal() {
                return total;
            }

            public void setTotal(long total) {
                this.total = total;
            }

            public long getRemain() {
                return remain;
            }

            public void setRemain(long remain) {
                this.remain = remain;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getEnttime() {
                return enttime;
            }

            public void setEnttime(String enttime) {
                this.enttime = enttime;
            }

            public String getScope() {
                return scope;
            }

            public void setScope(String scope) {
                this.scope = scope;
            }

            public String getGiftcode() {
                return giftcode;
            }

            public void setGiftcode(String giftcode) {
                this.giftcode = giftcode;
            }

            public int getIsget() {
                return isget;
            }

            public void setIsget(int isget) {
                this.isget = isget;
            }
        }
    }
}
