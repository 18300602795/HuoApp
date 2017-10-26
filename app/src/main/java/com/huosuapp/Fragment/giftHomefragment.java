package com.huosuapp.Fragment;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.huosuapp.Adapter.GiftMainadapter;
import com.huosuapp.Bean.HiddenJiHuoCode;
import com.huosuapp.text.R;
import com.huosuapp.view.PagerTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * 礼包界面的fragment
 */
public class giftHomefragment extends Basefragment {
    private GiftMainadapter mGiftMainadapter;
	private PagerTab mPagerTab;
	private ViewPager viewPager;
	private ViewPager singleViewPager;
	key_cody_fragment key_cody_fragment;

	private ArrayList<Fragment> fragments = new ArrayList<>();
	@Override
	public View getContentView() {
		
		return null;
	}

	@Override
	public int getContentViewLayoutId() {
		return R.layout.giftfragment;
	}

	@Override
	public void initView() {
	}


	@Subscribe(threadMode = ThreadMode.MAIN)
	public void hiddenJihuo(HiddenJiHuoCode hiddenJiHuoCode){
		fragments.clear();
		fragments.add(new gift_cody_fragment());
		singleViewPager=findView(R.id.single_gift_viewpagr);
		singleViewPager.setVisibility(View.VISIBLE);
		singleViewPager.setAdapter(new GiftMainadapter(getFragmentManager(), fragments));
		mPagerTab.setVisibility(View.GONE);
		viewPager.setVisibility(View.GONE);
	}


	@Override
	protected void loadData() {
		//加载过则跳出
		if (mHasLoadedOnce||!isVisible){
			return;
		}
		EventBus.getDefault().register(this);
		stateLayout.showContentView();
		fragments.add(new gift_cody_fragment());
		key_cody_fragment= new key_cody_fragment();
		fragments.add(key_cody_fragment);
		mGiftMainadapter=new GiftMainadapter(getFragmentManager(), fragments);
		mPagerTab=findView(R.id.gift_pagertab);
		viewPager=findView(R.id.gigt_viewpagr);
		viewPager.setAdapter(mGiftMainadapter);
		mPagerTab.setViewPager(viewPager);
		mHasLoadedOnce=true;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void initListener() {
		
		
	}

	@Override
	public void initData() {
		
		
	}

	@Override
	public CharSequence getTitle() {

		return null;
	}

	@Override
	public void parUI(Message mes) {

	}

}
