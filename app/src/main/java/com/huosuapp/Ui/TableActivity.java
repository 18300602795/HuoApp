package com.huosuapp.Ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.huosuapp.Fragment.TodayFragment;
import com.huosuapp.Fragment.TomorrowFragment;
import com.huosuapp.Fragment.YesterdayFragment;
import com.huosuapp.text.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 开服表界面
 */
public class TableActivity extends FragmentActivity implements View.OnClickListener {
    private Button mTodayBtn;
    private Button mTomorrowBtn;
    private Button mYesterdayBtn;
    private FragmentManager fm;
    private TodayFragment todayFragment;
    private YesterdayFragment yesterdayFragment;
    private TomorrowFragment tomorrowFragment;
    private List<Fragment> fragments;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        initView();
        showFragment(1);
        mTodayBtn.setBackgroundResource(R.drawable.table_btn_gb_pregress);
    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.table_back);
        mTodayBtn = (Button) findViewById(R.id.table_today);
        mTomorrowBtn = (Button) findViewById(R.id.table_tomorrow);
        mYesterdayBtn = (Button) findViewById(R.id.table_yesterday);
        back.setOnClickListener(this);
        mTodayBtn.setOnClickListener(this);
        mTomorrowBtn.setOnClickListener(this);
        mYesterdayBtn.setOnClickListener(this);
        fm = getSupportFragmentManager();
        fragments = new ArrayList<>();
    }

    private void showFragment(int type) {
        FragmentTransaction transaction = fm.beginTransaction();
        hideAll(transaction);
        switch (type) {
            case 1:
                if (todayFragment == null) {
                    todayFragment = new TodayFragment();
                    transaction.add(R.id.content_fl, todayFragment);
                    fragments.add(todayFragment);
                }
                Log.e("222", "todayFragment：" + todayFragment.today_pager);
                transaction.show(todayFragment);
                break;
            case 2:
                if (tomorrowFragment == null) {
                    tomorrowFragment = new TomorrowFragment();
                    transaction.add(R.id.content_fl, tomorrowFragment);
                    fragments.add(tomorrowFragment);
                }
//                Log.e("222", "tomorrowFragment：" + tomorrowFragment.tomorrow_pager);
                transaction.show(tomorrowFragment);
                break;
            case 3:
                if (yesterdayFragment == null) {
                    yesterdayFragment = new YesterdayFragment();
                    transaction.add(R.id.content_fl, yesterdayFragment);
                    fragments.add(yesterdayFragment);
                }
//                Log.e("222", "yesterdayFragment：" + yesterdayFragment.yesterday_pager);
                transaction.show(yesterdayFragment);
                break;
        }
        transaction.commit();
    }
    private  void  hideAll(FragmentTransaction transcation){
        for (Fragment fragment: fragments) {
            transcation.hide(fragment);
//            transcation.commit();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.table_today:
                resetView();
                showFragment(1);
                mTodayBtn.setBackgroundResource(R.drawable.table_btn_gb_pregress);
                break;
            case R.id.table_tomorrow:
                resetView();
                showFragment(2);
                mTomorrowBtn.setBackgroundResource(R.drawable.table_btn_gb_pregress);
                break;
            case R.id.table_yesterday:
                resetView();
                showFragment(3);
                mYesterdayBtn.setBackgroundResource(R.drawable.table_btn_gb_pregress);
                break;
            case R.id.table_back:
                finish();
                break;
        }
    }


    /**
     * 重置所有button
     */
    private void resetView() {
        mTodayBtn.setBackgroundResource(R.drawable.table_btn_bg);
        mTomorrowBtn.setBackgroundResource(R.drawable.table_btn_bg);
        mYesterdayBtn.setBackgroundResource(R.drawable.table_btn_bg);
    }


}
