package com.huosuapp.Ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.huosuapp.AppManager;
import com.huosuapp.Util.Constants;
import com.huosuapp.view.StateLayout;


public abstract class BaseActivity2 extends FragmentActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getViewID());
		initUI();
		initData();
		initListener();
		AppManager.getAppManager().addActivity(this);
	}


	/**
	 *定于一个handle用于子类更新UI
	 */
	protected Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			parUI( msg);
		}
	};




	protected abstract void initListener();
	/**
	 * 初始化UI
	 */
	public abstract void initUI();
	/**
	 * 初始化数据
	 */
	public abstract void initData();

	public abstract  int getViewID();

	public abstract  void parUI(Message mes);

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		AppManager.getAppManager().removeActivity(this);
	}


	private Toast toast = null;

	/** 不会一直重复重复重复重复的提醒了 */
	protected void showToast(String msg, int length) {
		if (toast == null) {
			toast = Toast.makeText(this, msg, length);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}





	/**
	 * 权限检查
	 */
	public boolean hasPermission(String...permissions){
		if (Build.VERSION.SDK_INT<23){
			return true;
		}

		for (String permission:permissions){
			if (ContextCompat.checkSelfPermission(this,permission)!=
					PackageManager.PERMISSION_GRANTED){
				return false;
			}
		}
		return true;
	}

	/**
	 * 请求权限
	 */
	public void requestPermission(int code , String... permissions){
		ActivityCompat.requestPermissions(this,permissions,code);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode){
			case Constants.WRITE_READ_EXTERNAL_CODE:
				writePermission();
				break;
		}
	}

	public void writePermission(){

	}

}
