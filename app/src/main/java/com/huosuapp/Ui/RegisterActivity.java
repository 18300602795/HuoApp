package com.huosuapp.Ui;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.Adapter.register_main_adapter;
import com.huosuapp.Fragment.phone_register_fragment;
import com.huosuapp.Fragment.username_register;
import com.huosuapp.text.BuildConfig;
import com.huosuapp.text.R;
import com.huosuapp.view.PagerTab;

import java.util.ArrayList;

/**
 * 注册界面 2016/7/12.
 */
public class RegisterActivity extends BaseActivity {
    private ViewPager mviewViewPager;
    private PagerTab mpPagerTab;
    private ArrayList<Fragment> list = new ArrayList<Fragment>();
    private TextView tv;
    private RelativeLayout r1;

    @Override
    public void parUI(Message mes) {

    }

    @Override
    public void initUI() {

        stateLayout.showContentView();
        if(BuildConfig.useMessage){
            list.add(new phone_register_fragment());
            list.add(new username_register());
        }else{
            list.add(new username_register());
        }
        mviewViewPager = (ViewPager) findViewById(R.id.register_viewpagr);
        mpPagerTab = (PagerTab) findViewById(R.id.register_pagertab);
        r1 = (RelativeLayout) findViewById(R.id.all_return);
        tv = (TextView) findViewById(R.id.tv_nav);
        tv.setText("账号注册 ");
        mviewViewPager.setAdapter(new register_main_adapter(getSupportFragmentManager(), list));
        mpPagerTab.setViewPager(mviewViewPager);
        r1.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getViewID() {
        return R.layout.register_main;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_return:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
        }
    }
    @Override
    protected void initListener() {

    }

}
