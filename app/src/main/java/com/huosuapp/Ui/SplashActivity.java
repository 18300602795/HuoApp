package com.huosuapp.Ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huosuapp.MyApplication;
import com.huosuapp.Util.ChannelUtil;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.text.R;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 闪屏页面
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ttw_splash);
        String cachePath= MyApplication.apkdownload_path+"CacheTable";
        File file=new File(cachePath);
        if (!file.exists()){
            file.mkdirs();
        }
        String channel = ChannelUtil.getChannel(this,
                ChannelUtil.AGENT_FILE);
        if (!StringUtils.isEmpty(channel)) {
            SharePrefUtil.saveString(this,SharePrefUtil.KEY.AGENT,channel);
            Log.e("--------------", "onCreate: "+channel);
        }

        Log.e("--------------", "onCreate: 空的"+channel);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 2000);
    }
}
