package com.huosuapp.Ui;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.Adapter.down_load_main_adapter;
import com.huosuapp.Fragment.DownloadInstalledFragment;
import com.huosuapp.Fragment.download_textqueue_fragment;
import com.huosuapp.text.R;
import com.huosuapp.view.PagerTab;

import java.util.ArrayList;

/**
 * 下载页
 */
public class DownloadActivity extends BaseActivity implements OnClickListener{

	private ArrayList<Fragment> list;//装载着viewpager里面的Fragment
	private ViewPager mpPager; //Viewpager
	private PagerTab mPagerTab;
	private TextView tv ;
	private RelativeLayout rel;


	@Override
	public void parUI(Message mes) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {	
		case R.id.all_return:
			System.out.println("点击了返回键");
			finish();
			overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void initUI() {
		mpPager =  (ViewPager) findViewById(R.id.download_viewpagr);
		mPagerTab=(PagerTab) findViewById(R.id.download_pagertab);
		rel= (RelativeLayout) findViewById(R.id.all_return);
		tv=(TextView) findViewById(R.id.tv_nav);
		tv.setText("下载管理");
		list=new ArrayList<Fragment>();
		stateLayout.showContentView();
		list.add(new download_textqueue_fragment());
		list.add(new DownloadInstalledFragment());
		mpPager.setAdapter(new down_load_main_adapter(getSupportFragmentManager(), list));
		mpPager.setOffscreenPageLimit(list.size());
		mPagerTab.setViewPager(mpPager);
		rel.setOnClickListener(this);

	}
	@Override
	public void initData() {
	}

	@Override
	protected void initListener() {

	}

	@Override
	public int getViewID() {
		return R.layout.download_main;
	}


}
