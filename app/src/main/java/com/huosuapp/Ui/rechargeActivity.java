package com.huosuapp.Ui;

import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huosuapp.text.R;


/**
 * 充值界面
 */
public class rechargeActivity extends BaseActivity {
    private LinearLayout all_returns;//点击界面返回箭头
    private TextView textView1, textView2,textView3,textView4,textView5,textView6;
    private EditText ed_my_money;
    private ImageView iv_zhifubao,iv_weixinzhifu,iv_yinlianzhifu;
    private LinearLayout llt_zhifubao,llt_weixin,llt_yinlian;



    @Override
    public void parUI(Message mes) {

    }

    @Override
    protected void initListener() {
        all_returns.setOnClickListener(this);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView6.setOnClickListener(this);
        llt_zhifubao.setOnClickListener(this);
        llt_weixin.setOnClickListener(this);
        llt_yinlian.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        stateLayout.showContentView();
        all_returns= (LinearLayout) findViewById(R.id.rl_nav_recharge);
        textView1= (TextView) findViewById(R.id.tv_abc1);
        textView2= (TextView) findViewById(R.id.tv_abc2);
        textView3= (TextView) findViewById(R.id.tv_abc3);
        textView4= (TextView) findViewById(R.id.tv_abc4);
        textView5= (TextView) findViewById(R.id.tv_abc5);
        textView6= (TextView) findViewById(R.id.tv_abc6);
        ed_my_money= (EditText) findViewById(R.id.my_money);
        iv_zhifubao= (ImageView) findViewById(R.id.zhifubao_dagou);
        iv_weixinzhifu= (ImageView) findViewById(R.id.weixin_dagou);
        iv_yinlianzhifu= (ImageView) findViewById(R.id.yinhangzhifu_dagou);
        llt_zhifubao= (LinearLayout) findViewById(R.id.llt_zhifubao);
        llt_weixin= (LinearLayout) findViewById(R.id.llt_weixin);
        llt_yinlian= (LinearLayout) findViewById(R.id.llt_yinlian);

    }

    @Override
    public void initData() {

    }

    @Override
    public int getViewID() {
        return R.layout.recharge_main;
    }


    @Override
    public void onClick(View view) {
        //initColor();
        switch (view.getId()){
            case R.id.rl_nav_recharge:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
            case R.id.tv_abc1:
                initColor();
                textView1.setBackgroundResource(R.drawable.money_btn1);
                ed_my_money.setText("充值100平台币");
                break;
            case R.id.tv_abc2:
                initColor();
                textView2.setBackgroundResource(R.drawable.money_btn1);
                ed_my_money.setText("充值500平台币");
                break;
            case R.id.tv_abc3:
                initColor();
                textView3.setBackgroundResource(R.drawable.money_btn1);
                ed_my_money.setText("充值1000平台币");
                break;
            case R.id.tv_abc4:
                initColor();
                textView4.setBackgroundResource(R.drawable.money_btn1);
                ed_my_money.setText("充值5000平台币");
                break;
            case R.id.tv_abc5:
                initColor();
                textView5.setBackgroundResource(R.drawable.money_btn1);
                ed_my_money.setText("充值10000平台币");
                break;
            case R.id.tv_abc6:
                initColor();
                textView6.setBackgroundResource(R.drawable.money_btn1);
                ed_my_money.setText("充值20000平台币");
                break;
            case R.id.llt_zhifubao:
                initDaGou();
                iv_zhifubao.setVisibility(View.VISIBLE);
                break;
            case R.id.llt_weixin:
                initDaGou();
                iv_weixinzhifu.setVisibility(View.VISIBLE);
                break;
            case R.id.llt_yinlian:
                initDaGou();
                iv_yinlianzhifu.setVisibility(View.VISIBLE);
                break;

        }
    }

    /**
     * 初始化没有选择的金额选择的颜色。
     */
    public void initColor(){
        textView1.setBackgroundResource(R.drawable.money_btn2);
        textView2.setBackgroundResource(R.drawable.money_btn2);
        textView3.setBackgroundResource(R.drawable.money_btn2);
        textView4.setBackgroundResource(R.drawable.money_btn2);
        textView5.setBackgroundResource(R.drawable.money_btn2);
        textView6.setBackgroundResource(R.drawable.money_btn2);
    }


    /**
     * 初始化没有选择的支付方式的打钩。
     */
    public void initDaGou(){
        iv_weixinzhifu.setVisibility(View.GONE);
        iv_yinlianzhifu.setVisibility(View.GONE);
        iv_zhifubao.setVisibility(View.GONE);
    }

}
