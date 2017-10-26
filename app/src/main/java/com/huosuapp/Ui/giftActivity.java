package com.huosuapp.Ui;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.Adapter.GiftActivityadapter;
import com.huosuapp.Bean.HiddenJiHuoCode;
import com.huosuapp.Fragment.gift_cody_fragment1;
import com.huosuapp.Fragment.key_cody_fragment1;
import com.huosuapp.text.R;
import com.huosuapp.view.PagerTab;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by admin on 2016/8/9.
 */
public class giftActivity extends BaseActivity {
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private PagerTab mPagerTab;
    private ViewPager viewPager;
    private TextView textView;
    private RelativeLayout rlt;
    key_cody_fragment1 key_cody_fragment1;
    @Override
    public void parUI(Message mes) {

    }

    @Override
    protected void initListener() {
        rlt.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        stateLayout.showContentView();
        fragments.add(new gift_cody_fragment1());
        key_cody_fragment1 = new key_cody_fragment1();
        fragments.add(key_cody_fragment1);
        mPagerTab= (PagerTab) findViewById(R.id.giftdetails_pagertab);
        viewPager= (ViewPager) findViewById(R.id.giftdetails_viewpagr);
        textView= (TextView) findViewById(R.id.tv_nav);
        rlt= (RelativeLayout) findViewById(R.id.all_return);
        textView.setText("礼包");
        HiddenJiHuoCode stickyEvent = EventBus.getDefault().getStickyEvent(HiddenJiHuoCode.class);
        if(stickyEvent!=null){
            hiddenJihuo(stickyEvent);
        }
    }

    /**
     * 没有激活码
     * @param hiddenJiHuoCode
     */
    public void hiddenJihuo(HiddenJiHuoCode hiddenJiHuoCode){
        fragments.clear();
        fragments.add(new gift_cody_fragment1());
        ViewPager singleViewPager= (ViewPager) findViewById(R.id.single_gift_viewpage);
        singleViewPager.setVisibility(View.VISIBLE);
        singleViewPager.setAdapter(new GiftActivityadapter(getSupportFragmentManager(),fragments));
        mPagerTab.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
//		mPagerTab.setViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initData() {
        viewPager.setAdapter(new GiftActivityadapter(getSupportFragmentManager(),fragments));
        mPagerTab.setViewPager(viewPager);
    }

    @Override
    public int getViewID() {
        return R.layout.gift_main;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.all_return:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
        }
    }
}
