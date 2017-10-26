package com.huosuapp.Bean;

import java.util.List;

/**
 * Created by admin on 2016/9/5.
 */
public class GameText {

    /**
     * code : 200
     * msg : 请求成功
     * data : {"gameid":2,"icon":"http://demo.1tsdk.com","gamename":"我叫MT","type":"2","runtime":1466066755,"category":1,"hot":1,"downcnt":74,"score":10,"distype":1,"discount":0,"rebate":0,"likecnt":0,"sharecnt":0,"downlink":"","oneword":"FASDF","size":"125M","image":["http://demo.1tsdk
     * .com/upload/20160905/57cd1a2b3429d.jpg","http://demo.1tsdk.com/upload/20160905/57cd1a30a239b.jpg"],"lang":"中文","sys":"",
     * "disc":"2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015
     * 年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！","verid":2,"vername":"5.1","giftcnt":0}
     */

    private int code;
    private String msg;
    /**
     * gameid : 2
     * icon : http://demo.1tsdk.com
     * gamename : 我叫MT
     * type : 2
     * runtime : 1466066755
     * category : 1
     * hot : 1
     * downcnt : 74
     * score : 10
     * distype : 1
     * discount : 0
     * rebate : 0
     * likecnt : 0
     * sharecnt : 0
     * downlink :
     * oneword : FASDF
     * size : 125M
     * image : ["http://demo.1tsdk.com/upload/20160905/57cd1a2b3429d.jpg","http://demo.1tsdk.com/upload/20160905/57cd1a30a239b.jpg"]
     * lang : 中文
     * sys :
     * disc : 2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！2015年最劲爆的卡牌游戏，掌上三国，上百大将任您调遣！
     * verid : 2
     * vername : 5.1
     * giftcnt : 0
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
        private int gameid;
        private String icon;
        private String gamename;
        private String type;
        private long runtime;
        private long category;
        private long hot;
        private long downcnt;
        private long score;
        private long distype;
        private long discount;
        private long rebate;
        private long likecnt;
        private long sharecnt;
        private String downlink;
        private String oneword;
        private String size;
        private String lang;
        private String sys;
        private String disc;
        private long verid;
        private String vername;
        private long giftcnt;
        private List<String> image;
    }
}
