package com.huosuapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.huosuapp.Bean.DownCountChange;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 自定义控件，用于显示游戏类型跟下载次数
 */
public class DownCountText extends TextView {
    private static final String TAG =DownCountChange.class.getSimpleName() ;
    private Integer gameId;
    public DownCountText(Context context) {
        super(context);
    }

    public DownCountText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DownCountText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStateChange(DownCountChange downCountChange){
        Log.e(TAG, "onStateChange: 收到回调 "+downCountChange.gameId);
        if(gameId!=null){
            if(gameId==downCountChange.gameId){
                refreshUi(gameId,downCountChange.downcnt);
            }
        }
    }

    private void refreshUi(int gameId, long downcnt) {
//        动作  | 10次下载
        String textContent = getText().toString().trim();
        if(textContent.length()>2){
            String substring = textContent.substring(0, 2);
            setText(substring+"  | "+downcnt+"次下载");
        }
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
