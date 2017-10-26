package com.huosuapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.Bean.ErrorMessageBean;
import com.huosuapp.Bean.Login_Bean;
import com.huosuapp.MyApplication;
import com.huosuapp.Util.AuthCodeUtil;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.NetworkImpl;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.text.BuildConfig;
import com.huosuapp.text.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 登录界面 2016/7/12.
 */
public class LoginActivity extends BaseActivity {
    private EditText user_name, user_password;
    private TextView forget_password, user_register;
    private ImageButton img_return;
    private Button btn;//点击了登录
    private String username = null;
    private String password = null;
    private Boolean isErroir = true;   //默认是错误信息
    private Intent intent= new Intent();
    @Override
    public void parUI(Message mes) {
        switch (mes.what) {
            case 9:
                if(login_bean==null&&errorMessageBean==null){
                    showToast("网络连接失败",0);
                    return;
                }
                if (!isErroir) {
                    if (login_bean.getCode() >= 200 && login_bean.getCode() <= 250) {
                        Toast.makeText(this, "欢迎" + username + "回来!", Toast.LENGTH_SHORT).show();
                        //用返回来数据保存下来
                        //把返回来的用户名，密码存储到sp里面
                        String identifier = login_bean.getData().getIdentifier();
                        String accesstoken = login_bean.getData().getAccesstoken();
                        int expaire_time = login_bean.getData().getExpaire_time();
                        SharePrefUtil.saveString(Global.getContext(), SharePrefUtil.KEY.IDENTIFIER, identifier);
                        SharePrefUtil.saveString(Global.getContext(), SharePrefUtil.KEY.ACCESSTOKEN, accesstoken);
                        SharePrefUtil.saveInt(Global.getContext(), SharePrefUtil.KEY.EXPAIRE_TIME, expaire_time);
                        SharePrefUtil.saveBoolean(Global.getContext(), SharePrefUtil.KEY.FIRST_LOGIN, false);  //表示用户已经登录了，以后都要保持这个状态
                        SharePrefUtil.saveString(Global.getContext(), SharePrefUtil.KEY.NICHENG, username);//保存用户的账号，（也就是昵称）
                        if (MyApplication.isPinglunLogin){
                            finish();
                            overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                        }else{
                            EnterHome();
                        }
                    }
                }else{
                    showToast(errorMessageBean.getMsg(),0);
                }
                break;
        }
    }

    @Override
    public void initUI() {
        stateLayout.showContentView();

        user_name = (EditText) findViewById(R.id.et_login_username);
        if(!BuildConfig.useMessage){
            user_name.setHint("请输入用户名");
        }
        user_password = (EditText) findViewById(R.id.et_login_password);
        btn = (Button) findViewById(R.id.btn_login);
        img_return = (ImageButton) findViewById(R.id.iv_login_return);
        forget_password = (TextView) findViewById(R.id.tv_forgot_password);
        user_register = (TextView) findViewById(R.id.tv_user_register);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getViewID() {
        return R.layout.ttw_user_rpwd;
    }

    public void initText() {
        username = user_name.getText().toString().trim();
        password = user_password.getText().toString().trim();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forgot_password:
                intent.setClass(this, HelpActivity.class);
                intent.putExtra("help", 9);
                startActivity(intent);
                overridePendingTransition(R.anim.act_in, R.anim.act_out);
                break;
            case R.id.tv_user_register:
                finish();
                intent.setClass(this,RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.act_in, R.anim.act_out);
                break;
            case R.id.iv_login_return:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
            case R.id.btn_login:
                initText();
                actionLogin();
                break;
        }
}

    private void actionLogin() {

        if (!NetworkImpl.isNetWorkConneted(this)) {
            //如果网络不可用的直接提示用户
            showToast( "网络连接错误，请检查当前网络状态！",0);
            return;
        }
        if (TextUtils.isEmpty(username)) {
            showToast( "请输入账号",0);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码",0);
            return;
        }
        Pattern pat = Pattern.compile("([a-zA-Z0-9]{6,12})");
        if (!pat.matcher(username).matches()) {
            showToast("账号只能由6至12位英文或数字组成",0);
            return;
        }
        if (!pat.matcher(password).matches()) {
            showToast("密码只能由6至12位英文或数字组成",0);
            return;
        }

        /**
         * 执行登陆传参数
         */
        loginRemoteService(username, password);

    }

    private void loginRemoteService(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", AuthCodeUtil.authcodeEncode(username, Constants.appkey));
        map.put("password", AuthCodeUtil.authcodeEncode(password, Constants.appkey));

        OkHttpUtils.postString(Constants.USER_LOGIN, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                parjsonLogin(res);
            }
        });

    }

    private Login_Bean login_bean;
    private ErrorMessageBean errorMessageBean;

    private void parjsonLogin(String res) {
        if (res.length() > 30 && res.length() < 60) {
            errorMessageBean = JsonUtil.getJsonUtil().json2Bean(res, ErrorMessageBean.class);
            isErroir = true;
        } else {
            login_bean = JsonUtil.getJsonUtil().json2Bean(res, Login_Bean.class);
            isErroir = false;
        }
        Message mes = Message.obtain();
        mes.what = 9;
        handler.sendMessage(mes);
    }


    @Override
    protected void initListener() {
        forget_password.setOnClickListener(this);
        user_register.setOnClickListener(this);
        img_return.setOnClickListener(this);
        btn.setOnClickListener(this);
        user_name.setOnClickListener(this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }
    /**
     * 登录成功后进去到首页
     */
    public void EnterHome() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.act_in, R.anim.act_out);
    }

}
