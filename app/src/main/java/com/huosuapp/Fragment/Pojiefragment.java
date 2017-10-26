package com.huosuapp.Fragment;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.huosuapp.Adapter.PojieMainadapter;
import com.huosuapp.text.R;
import com.huosuapp.view.PagerTab;

import java.util.ArrayList;

/**
 * 游戏界面的fragment
 */
public class Pojiefragment extends Basefragment {
    private ViewPager viewPager;
    private PagerTab pagerTab;
    private PojieMainadapter pojieMainadapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();


    /** 是否已被加载过一次，第二次就不再去请求数据了 */
//    private boolean mHasLoadedOnce=false;
    @Override
    public void parUI(Message mes) {

    }
    @Override
    public View getContentView() {
        return null;
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.pojiefragment;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void loadData() {
        //加载过则跳出
        if (mHasLoadedOnce||!isVisible){
            return;
        }
        stateLayout.showLoadingView();
        fragments.add(new pojie_online_fragment());
        fragments.add(new pojie_stand_fragment());
        pojieMainadapter=new PojieMainadapter(getFragmentManager(),fragments);
        viewPager=findView(R.id.pojie_viewpagr);
        pagerTab=findView(R.id.pojie_pagertab);
        stateLayout.showContentView();
        viewPager.setAdapter(pojieMainadapter);
        pagerTab.setViewPager(viewPager);
        mHasLoadedOnce=true;
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

}
