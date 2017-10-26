package com.huosuapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huosuapp.view.StateLayout;

import java.util.ArrayList;


public abstract class Basefragment extends Fragment {
	protected Activity context;
	protected StateLayout stateLayout;
	private Basefragment basefragment;

	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	protected boolean mHasLoadedOnce=false;
	/** Fragment当前状态是否可见 */
	protected boolean isVisible;


	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if(getUserVisibleHint()) {
			isVisible = true;
			loadData();
		} else {
			isVisible = false;
		}
	}

	/**
	 * 第一次进来才加载，取消预加载
	 */
	protected void loadData() {

	}


	/** 初始化数据的请求类型 */
	protected static final int TYPE_INIT_DATA = 0;
	/** 加载更多数据的请求类型 */
	protected static final int TYPE_LOADING_MORE = 1;
	/**
	 *定于一个handle用于子类更新UI
     */
	protected  Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			parUI( msg);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = getActivity();
		stateLayout = new MyStateLayout(context);
		initView();
		initData();
		initListener();
		return stateLayout;
	}


	/** 包括了4种状态的容器 */
	public class MyStateLayout extends StateLayout {

		public MyStateLayout(Context context) {
			super(context);
		}
		
		@Override
		public View getContentView() {
			return Basefragment.this.getContentView();
		}

		@Override
		public int getContentViewLayoutId() {
			return Basefragment.this.getContentViewLayoutId();
		}

		//点击加载失败，重试的方法
		@Override
		public void  aginData() {
			Basefragment.this.initData();
		}
	}

	/** 为了省略强转 */
	public <T> T findView(int id) {
		@SuppressWarnings("unchecked")
		T view = (T) stateLayout.findViewById(id);
		return view;
	}
	

	/** 检查初始化的数据是否正常，正常的话返回true */
	public boolean checkInitData(@SuppressWarnings("rawtypes") ArrayList dataList) {
		boolean result = false;
		// 根据服务器返回的数据来决定要显示哪个状态的View
		if (dataList == null) {
			stateLayout.showFailView();
		} else if (dataList.isEmpty()) {
			stateLayout.showEmptyView();
		} else {
			// 数据获取OK，可以把数据显示到界面了
			stateLayout.showContentView();
			result = true;
		}
		
		return result;
	}
	
	/** 返回一个正常界面的View */
	public abstract View getContentView();
	
	/** 返回一个正常界面的布局id */
	public abstract int getContentViewLayoutId();
	
	/** 初始化View */
	public abstract void initView();

	/** 初始化监听器 */
	public abstract void initListener();

	/** 初始化数据 */
	public abstract void initData();
	
	
	
/*	
	*//** 检查加载更多的数据是否正常，正常的话返回true *//*
	public boolean checkLoadMoreData(@SuppressWarnings("rawtypes") ArrayList dataList, LoadMoreListView listView) {
		boolean result = false;
		listView.onLoadingMoreComplete();
		// 根据服务器返回的数据来决定要显示哪个状态的View
		if (dataList == null) {
			listView.showLoadError();
		} else if (dataList.isEmpty()) {
			listView.showNoMoreData();
		} else {
			// 数据获取OK，可以把数据显示到界面了
			result = true;
		}
		return result;
	}*/
	
	/** 获取Fragment的标题 */
	public abstract CharSequence getTitle();


	public abstract  void parUI(Message mes);


	@Override
	public void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	private Toast toast = null;
	/** 不会一直重复重复重复重复的提醒了 */
	protected void showToast(String msg, int length) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, length);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
}
