package com.huosuapp.PopWindow;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.huosuapp.text.R;

import java.util.ArrayList;
import java.util.List;


public class SelectPopupWindow extends PopupWindow {

	private List<View> views = new ArrayList<View>();
	private View mMenuView;
	private Activity atx;

	public SelectPopupWindow(Activity context, OnClickListener itemsOnClick) {
		super(context);
		atx = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.sex_select_layout, null);
		initViews(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点�?

		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(MResource.getIdByName(context,
		// Constants.Resouce.STYLE, "AnimBottom"));
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(0xb0000000);
		 ColorDrawable dw = new ColorDrawable(0x00000000);
		// // 设置SelectPicPopupWindow弹出窗体的背�?
		 this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在�?择框外面则销毁弹出框
		 this.setFocusable(true);   
		 this.setOutsideTouchable(true);// 不可点击在window之外
		// this.setOutsideTouchable(false);// 不可点击在window之外
	


	}

	/***
	 * 对话框的一下初始化
	 * */
	private void initViews(OnClickListener itemsOnClick) {//点击事件还需要传
		mMenuView.findViewById(R.id.nan).setOnClickListener(itemsOnClick);
		mMenuView.findViewById(R.id.nv).setOnClickListener(itemsOnClick);
		mMenuView.findViewById(R.id.tv_cancel).setOnClickListener(itemsOnClick);
	}

}