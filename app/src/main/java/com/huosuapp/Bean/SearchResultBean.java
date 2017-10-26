package com.huosuapp.Bean;

import java.util.List;

/**
 * Created by admin on 2016/8/26.
 */
public class SearchResultBean {


    /**
     * code : 200
     * msg : 请求成功
     * data : {"count":1,"searchtype":"game","game_list":[{"gameid":8,"icon":null,"gamename":"再来一瓶","type":7,"runtime":1466359483,"category":2,"hot":2,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"","size":""}]}
     */

    private String code;
    private String msg;
    /**
     * count : 1
     * searchtype : game
     * game_list : [{"gameid":8,"icon":null,"gamename":"再来一瓶","type":7,"runtime":1466359483,"category":2,"hot":2,"downcnt":0,"score":0,"distype":0,"discount":null,"rebate":null,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"","size":""}]
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
        private int count;
        private String searchtype;
        /**
         * gameid : 8
         * icon : null
         * gamename : 再来一瓶
         * type : 7
         * runtime : 1466359483
         * category : 2
         * hot : 2
         * downcnt : 0
         * score : 0
         * distype : 0
         * discount : null
         * rebate : null
         * likecnt : 0
         * sharecnt : 0
         * downlink :
         * oneword :
         * size :
         */

        private List<GameListBean> game_list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getSearchtype() {
            return searchtype;
        }

        public void setSearchtype(String searchtype) {
            this.searchtype = searchtype;
        }

        public List<GameListBean> getGame_list() {
            return game_list;
        }

        public void setGame_list(List<GameListBean> game_list) {
            this.game_list = game_list;
        }

        public static class GameListBean {
            private int gameid;
            private Object icon;
            private String gamename;
            private String type;
            private long runtime;
            private long category;
            private long hot;
            private long downcnt;
            private long score;
            private long distype;
            private Object discount;
            private Object rebate;
            private long likecnt;
            private long sharecnt;
            private String downlink;
            private String oneword;
            private String size;

            public int getGameid() {
                return gameid;
            }

            public void setGameid(int gameid) {
                this.gameid = gameid;
            }

            public Object getIcon() {
                return icon;
            }

            public void setIcon(Object icon) {
                this.icon = icon;
            }

            public String getGamename() {
                return gamename;
            }

            public void setGamename(String gamename) {
                this.gamename = gamename;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public long getRuntime() {
                return runtime;
            }

            public void setRuntime(long runtime) {
                this.runtime = runtime;
            }

            public long getCategory() {
                return category;
            }

            public void setCategory(long category) {
                this.category = category;
            }

            public long getHot() {
                return hot;
            }

            public void setHot(long hot) {
                this.hot = hot;
            }

            public long getDowncnt() {
                return downcnt;
            }

            public void setDowncnt(long downcnt) {
                this.downcnt = downcnt;
            }

            public long getScore() {
                return score;
            }

            public void setScore(long score) {
                this.score = score;
            }

            public long getDistype() {
                return distype;
            }

            public void setDistype(long distype) {
                this.distype = distype;
            }

            public Object getDiscount() {
                return discount;
            }

            public void setDiscount(Object discount) {
                this.discount = discount;
            }

            public Object getRebate() {
                return rebate;
            }

            public void setRebate(Object rebate) {
                this.rebate = rebate;
            }

            public long getLikecnt() {
                return likecnt;
            }

            public void setLikecnt(long likecnt) {
                this.likecnt = likecnt;
            }

            public long getSharecnt() {
                return sharecnt;
            }

            public void setSharecnt(long sharecnt) {
                this.sharecnt = sharecnt;
            }

            public String getDownlink() {
                return downlink;
            }

            public void setDownlink(String downlink) {
                this.downlink = downlink;
            }

            public String getOneword() {
                return oneword;
            }

            public void setOneword(String oneword) {
                this.oneword = oneword;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }
        }
    }
}
