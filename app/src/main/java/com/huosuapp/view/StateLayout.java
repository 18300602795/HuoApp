package com.huosuapp.view;


import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.huosuapp.text.R;


/**
 * 这个状态布局包含了4个状态，“正在加载”、“加载失败”、“加载为空”、“正常界面”
 * @author 
 *
 */
public abstract class StateLayout extends FrameLayout {

	private ProgressBar progressBarloadingView;
	public  View failView;
	private View emptyView;
	private View contentView;
	/** 包含“正在加载”、“加载失败”、“加载为空”状态的容器 */
	private FrameLayout container;

	public StateLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		// 创建包含“正在加载”、“加载失败”、“加载为空”状态的容器
		container = (FrameLayout) View.inflate(getContext(), R.layout.state_layout, null);
		// 由于每一个界面它们正常显示的界面都不一样，所以获取这些正常界面的方法写成抽象的，让子类去决定
		if (getContentView() != null) {
			contentView = getContentView();
		} else {
			contentView = View.inflate(getContext(), getContentViewLayoutId(), null);
		}
		
		container.addView(contentView);
		
		// 获取“正在加载”、“加载失败”、“加载为空”这3种状态的View的引用
		progressBarloadingView = (ProgressBar) container.findViewById(R.id.loadingView);
		failView = container.findViewById(R.id.failView);
		emptyView = container.findViewById(R.id.emptyView);
		
		// 默认显示“正在加载”
		progressBarloadingView.setVisibility(View.VISIBLE);
		failView.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		contentView.setVisibility(View.GONE);
		addView(container);

		/**
		 * 点击加载失败，重试
		 */
		failView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				aginData();
			}
		});

	}
	
	/** 显示“正在加载”的View */
	public void showLoadingView() {
		showView(progressBarloadingView);
	}
	
	/** 显示“加载失败”的View */
	public void showFailView() {
		showView(failView);
	}
	
	/** 显示“正在加载”的View */
	public void showEmptyView() {
		showView(emptyView);
	}
	
	/** 显示数据加载成功时”正常界面“的View */
	public void showContentView() {
		showView(contentView);
	}
	
	/**
	 * 显示指定的View，并且隐藏其它View
	 * @param view
	 */
	private void showView(View view) {
		for (int i = 0; i < container.getChildCount(); i++) {
			View child = container.getChildAt(i);			
			if (child == view) {
				// 显示指定的View
				child.setVisibility(View.VISIBLE);
			} else {
				// 其它的View隐藏
				child.setVisibility(View.GONE);
			}
		}
	}
	
	/** 返回一个正常界面的View */
	public abstract View getContentView();
	
	/** 返回一个正常界面的布局id */
	public abstract int getContentViewLayoutId();


	/**
	 * 点击加载失败，点击重试，
	 */
	public abstract void  aginData();

}
