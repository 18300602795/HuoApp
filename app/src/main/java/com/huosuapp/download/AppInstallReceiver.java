package com.huosuapp.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huosuapp.Bean.DownStateChange;
import com.huosuapp.Util.ApkUtils;
import com.lidroid.xutils.http.HttpHandler;

import org.greenrobot.eventbus.EventBus;

/**
 * author janecer
 * 2014年4月18日上午11:58:45
 */
public class AppInstallReceiver extends BroadcastReceiver {
    public static final String PACAGENAME="packagename";
	private static final String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		//Logger.msg(TAG, "广播来了："+intent.getAction());
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) { 

			String packageName = intent.getDataString().substring(8);
			try {
				try {
					if(!ApkUtils.isServiceStarted(context,context.getPackageName())){
						return ;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				DownloadManager dm=DownloadService.getDownloadManager(context);
				DownloadInfo dinfo=dm.getDownloadInfoByPname(packageName);
				if(dinfo.getState()== HttpHandler.State.SUCCESS){//成功下载
					dm.setInstallSuccess(dinfo);
					EventBus.getDefault().post(new DownStateChange(Integer.parseInt(dinfo.getAppId())));
				}
//				ApkUtils.uninstallApp(context,dinfo.getFileName());
//				dm.removeDownload(dinfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
