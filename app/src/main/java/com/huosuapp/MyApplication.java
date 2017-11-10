package com.huosuapp;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.TextView;

import com.huosuapp.Bean.TimeStampBean;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.Util.Utils;
import com.huosuapp.text.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 程序一运行的时候就会创建这个类，并调用onCreate方法，这个类的作用就是专门用来保存全局变量的
 * @author dzl
 *
 */
public class MyApplication extends Application {

	/** Application类型的上下文 */
	private static Context context;
	/** 让子和线程和主线程进行通讯 */

	private static Handler handler;

	public static final String pic_path ="IMAGE_FILE_NAME"; //全局保存用户信息头像的key

	private static final String TAG = "MyApplication";
	public static TextView tv_download_count;
	public static String apkdownload_path="";
	public static int time = 0;
	public static boolean isCache = true;
	public static String uid = "438";
	public static boolean isPinglunLogin;//  为true代表是从评论那里跳到登录的
    @Override
	public void onCreate() {
		super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();StrictMode.setVmPolicy(builder.build());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			builder.detectFileUriExposure();
		}
		context = this;
		handler = new Handler();
		apkdownload_path= Environment.getExternalStorageDirectory().getPath()+ File.separator+this.getPackageName();
		Logger.msg("安装包路径", apkdownload_path);
		File file=new File(apkdownload_path);
		if(!file.exists()){
			boolean flag=file.mkdirs();
		}
		apkdownload_path+=File.separator;
		//Logger.msg(TAG,"myapplication oncreate()");
		Utils.getAgentAndAppid(this);

		getData();
		initImageLoader();
	}

	private void initImageLoader() {

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
				.showImageForEmptyUri(R.drawable.ic_launcher) //
				.showImageOnFail(R.drawable.ic_launcher) //
				.cacheInMemory(true) //
				.cacheOnDisk(true) //
				.build();//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration//
				.Builder(getApplicationContext())//
				.defaultDisplayImageOptions(defaultOptions)//
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs()//
				.build();//
		ImageLoader.getInstance().init(config);
	}

	private void getData() {
		Map<String, String> map = new HashMap<>();
		String url=StringUtils.getCompUrlFromParams(Constants.GET_SERVER_TIME,map);
//		Log.e("第一次获取时间的url", "getData: " +url);
		OkHttpUtils.postString(url, false, map, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String res = response.body().string().trim();
				parjson(res);
//				Log.e("第一次获取时间的", "getData: " +res);
			}
		});
	}

	private void parjson(String res) {
		TimeStampBean timeStampBean= JsonUtil.getJsonUtil().json2Bean(res,TimeStampBean.class);
		if(timeStampBean!=null&&timeStampBean.getData()!=null){
			int i =SharePrefUtil.saveInt(MyApplication.this,SharePrefUtil.KEY.TIMESTAMP,
					timeStampBean.getData().getTime()-(int)(System.currentTimeMillis()/1000));
			time = i*2;
		}
	}

	/** 获取Application类型的上下文 */
	public static Context getContext() {
		return context;

	}

	/** 获取可以跟主线程通讯的Handler */
	public static Handler getHandler() {
		return handler;
	}


}
