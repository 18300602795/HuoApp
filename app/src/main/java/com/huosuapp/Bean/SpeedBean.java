package com.huosuapp.Bean;

/**
 * Created by liu hong liang on 2016/9/18.
 */
public class SpeedBean {
    private long currentSize;
    private long currentTime;

    public SpeedBean(long currentSize, long currentTime) {
        this.currentSize = currentSize;
        this.currentTime = currentTime;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
