package com.huosuapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huosuapp.Util.Logger;
import com.huosuapp.download.DownloadInfo;
import com.huosuapp.download.DownloadService;

/**
 * 安装完成发送广播
 */

public class InstallReceiver extends BroadcastReceiver {
    private DownloadInfo downloadInfo;
    private Context registerContext;
    public InstallReceiver( Context registerContext,DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
        this.registerContext=registerContext;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getStringExtra("androidurl");
        DownloadService.getDownloadManager(context).setInstallSuccess(downloadInfo);
        if(registerContext!=null){
            try {
                registerContext.unregisterReceiver(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Logger.msg("InstallReceiver", "___:"+packageName+"安装成功！收到消息了");
    }
}
