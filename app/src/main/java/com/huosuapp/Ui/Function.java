package com.huosuapp.Ui;

import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.text.R;


/**
 * Created by admin on 2016/9/1.
 */
public class Function extends BaseActivity {
    private TextView textView;
    private TextView tv_title;
    private TextView tv_about_my;
    private RelativeLayout rlt_allreturn;
    private LinearLayout llt_gongnengjieshao, aboutmy;
    private ImageButton ib_return;
    private int type; //0代表是功能介绍，1代表关于火树

    @Override
    protected void initListener() {
        ib_return.setOnClickListener(this);
        rlt_allreturn.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        stateLayout.showContentView();
        type = getIntent().getIntExtra("function", 0);
        textView = (TextView) findViewById(R.id.tv_function);
        llt_gongnengjieshao = (LinearLayout) findViewById(R.id.llt_gongnengjieshao);
        tv_title = (TextView) findViewById(R.id.tv_nav);
        aboutmy = (LinearLayout) findViewById(R.id.aboutmy);
        rlt_allreturn = (RelativeLayout) findViewById(R.id.rl_nav);
        ib_return = (ImageButton) findViewById(R.id.iv_login_return);
        tv_about_my = (TextView) findViewById(R.id.tv_about_my);
        setUI();
    }

    private void setUI() {
        switch (type) {
            case 0:
                llt_gongnengjieshao.setVisibility(View.VISIBLE);
                tv_title.setText("功能介绍");
                break;
            case 1:
                aboutmy.setVisibility(View.VISIBLE);
                tv_about_my.setText("");
                break;
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public int getViewID() {
        return R.layout.function_main;
    }

    @Override
    public void parUI(Message mes) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_nav:
            case R.id.iv_login_return:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
        }
    }
}
