package com.huosuapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.MyApplication;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.text.R;
import com.huosuapp.update.VersionUpdateManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 设置页2016/7/22.
 */
public class SettingActivity extends BaseActivity implements VersionUpdateManager.VersionUpdateListener {
    private static final String TAG =SettingActivity.class.getSimpleName() ;
    private LinearLayout llt; //点击了退出
    private RelativeLayout rlt;
    private TextView tv;
    private LinearLayout llt_gongneng, llt_about_my, llt_check_version;
    private int Service;//服务器版本号
    private int Version = 0;  //本地版本号
    private int VersionName=0; //本地版本名称
    VersionUpdateManager manager=new VersionUpdateManager();

    @Override
    public void parUI(Message mes) {

    }

    @Override
    protected void initListener() {
        llt.setOnClickListener(this);
        rlt.setOnClickListener(this);
        llt_gongneng.setOnClickListener(this);
        llt_about_my.setOnClickListener(this);
        llt_check_version.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        /**
         * 获取本地版本号
         */
        getNativeVersion();

        stateLayout.showContentView();
        llt = (LinearLayout) findViewById(R.id.llt_exit);
        rlt = (RelativeLayout) findViewById(R.id.all_return);
        tv = (TextView) findViewById(R.id.tv_nav);
        llt_gongneng = (LinearLayout) findViewById(R.id.llt_gongneng);
        llt_about_my = (LinearLayout) findViewById(R.id.llt_about_my);
        llt_check_version = (LinearLayout) findViewById(R.id.llt_check_version);
        tv.setText("设置中心");
    }

    @Override
    public void initData() {
        //获取服务器版本信息
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<String, String>();
        String url = StringUtils.getCompUrlFromParams(Constants.URL_GET_VERSION_INFO,map);
        OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res =response.body().string().trim();
                Logger.msg("获取出来的版本信息",res);
            }
        });
    }

    @Override
    public int getViewID() {
        return R.layout.setting;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llt_exit:
                showPopwindow();
                break;
            case R.id.all_return:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
            case R.id.llt_change_username:  //点击pop窗口的切换账号
                LoginActivity.start(this);
                finish();
                break;
            case R.id.llt_exit_login:  //点击pop窗口的退出登录
                finish();
                SharePrefUtil.delete(this);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.act_in, R.anim.act_out);
                SharePrefUtil.saveBoolean(this, SharePrefUtil.KEY.FIRST_LOGIN, true);
                break;
            case R.id.llt_gongneng:
                Intent intent1 = new Intent(this, Function.class);
                intent1.putExtra("function", 0);
                startActivity(intent1);
                overridePendingTransition(R.anim.act_in, R.anim.act_out);
                break;
            case R.id.llt_about_my:
                Intent intent2 = new Intent(this, Function.class);
                intent2.putExtra("function", 1);
                startActivity(intent2);
                overridePendingTransition(R.anim.act_in, R.anim.act_out);
                break;
            case R.id.llt_check_version:                  //检查更新版本
                manager.checkVersionUpdate(this,this);//检查更新
                break;

        }
    }


    private void showPopwindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PopView = inflater.inflate(R.layout.exit_pop, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        PopupWindow window = new PopupWindow(PopView,
                WindowManager.LayoutParams.MATCH_PARENT,
                Global.dp2px(130, SettingActivity.this));
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(llt,
                Gravity.BOTTOM, 0, 0);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        PopView.findViewById(R.id.llt_change_username).setOnClickListener(this);
        PopView.findViewById(R.id.llt_exit_login).setOnClickListener(this);
    }


    /**
     * 获取本地版本号
     */

    public void getNativeVersion() {

        try {
            Version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
//            VersionName=getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 获取服务器的版本号
     */
    public void getServiceVersion(){
        Map<String, String> map = new HashMap<String, String>();
//        map.put("verid",getNativeVersion()+"");
//        map.put("vername",)
    }

    @Override
    public void gotoDown() {
    }

    @Override
    public void cancel(String msg) {
        Logger.msg(TAG,"用户取消更新");
        if(msg!=null){
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.unRegister();
    }
}
