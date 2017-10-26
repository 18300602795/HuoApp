package com.huosuapp.Bean;

/**
 * Created by liu hong liang on 2016/9/14.
 */
public class DownCountChange {
    public int gameId;
    public long downcnt;
    public DownCountChange(int gamenId, long downcnt) {
        this.gameId=gamenId;
        this.downcnt = downcnt;
    }
}
