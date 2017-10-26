package com.huosuapp.Fragment;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.Bean.ErrorMessageBean;
import com.huosuapp.Bean.User_register_Bean;
import com.huosuapp.Ui.UerContentActivity;
import com.huosuapp.Util.AuthCodeUtil;
import com.huosuapp.Util.Code;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.NetworkImpl;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.text.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 用户名注册的fragment
 */
public class username_register extends Basefragment implements View.OnClickListener {
    private EditText et_username;
    private EditText et_password;
    private EditText et_affirm;
    private EditText et_yanzhengma;
    private Button btn_register;//点击注册
    private ImageView iv_showCode;//显示验证码的图片
    private String realCode;
    private Boolean isErroir = true;   //默认是错误信息

    @Override
    public void parUI(Message mes) {
        switch (mes.what) {
            case 11:
                if (!isErroir) {
                    if (user_register_bean.getCode() >= 200 && user_register_bean.getCode() <= 300) {
                        Toast.makeText(context, "恭喜你，注册成功了", Toast.LENGTH_SHORT).show();
                        //把返回来的用户名，密码存储到sp里面
                        String identifier = user_register_bean.getData().getIdentifier();
                        String accesstoken = user_register_bean.getData().getAccesstoken();
                        int expaire_time = user_register_bean.getData().getExpaire_time();
                        SharePrefUtil.saveString(Global.getContext(), SharePrefUtil.KEY.IDENTIFIER, identifier);
                        SharePrefUtil.saveString(Global.getContext(), SharePrefUtil.KEY.ACCESSTOKEN, accesstoken);
                        SharePrefUtil.saveInt(Global.getContext(), SharePrefUtil.KEY.EXPAIRE_TIME, expaire_time);
                        SharePrefUtil.saveBoolean(Global.getContext(), SharePrefUtil.KEY.FIRST_LOGIN, false);  //表示用户已经登录了，以后都要保持这个状态
                        SharePrefUtil.saveString(Global.getContext(), SharePrefUtil.KEY.NICHENG, username);//保存用户的账号，（也就是昵称）

                        EnterHome();  //注册成功去到个人中心界面设置，
                    }
                } else {
                    showToast(errorMessageBean.getMsg(),0);
                }
                break;
        }
    }

    @Override
    public View getContentView() {
        return null;
    }
    @Override
    public int getContentViewLayoutId() {
        return R.layout.uasename_registered;
    }

    @Override
    public void initView() {
        stateLayout.showContentView();
        et_username = findView(R.id.et_reg_usname);
        et_password = findView(R.id.et_reg_uspwd);
        et_affirm = findView(R.id.et_repeat_uspsd);
        et_yanzhengma = findView(R.id.et_yanzhengma);
        btn_register = findView(R.id.btn_register1);
        iv_showCode = findView(R.id.iv_showCode);
        //将验证码用图片的形式显示出来
        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
        TextView tv_register_protocol=findView(R.id.tv_register_protocol);
        tv_register_protocol.setText(String.format(getString(R.string.register_protocol),getString(R.string.app_name)));
    }

    @Override
    public void initListener() {
        btn_register.setOnClickListener(this);
        iv_showCode.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public CharSequence getTitle() {
        return "用户名注册";
    }

    private String username, password, affirm_passwrd;
    private String yanzhengma;//输入的验证码

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register1:
                username = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                affirm_passwrd = et_affirm.getText().toString().trim();
                yanzhengma = et_yanzhengma.getText().toString().trim();
                if (!isvalidate()) {
                    break;
                }
                register();
                break;
            case R.id.iv_showCode:
                iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
        }
    }

    private boolean isvalidate() {

        if (!NetworkImpl.isNetWorkConneted(getActivity())) {
            //如果网络不可用的直接提示用户
            showToast("\"网络连接错误，请检查当前网络状态！",0);
            return false;
        }
        if (TextUtils.isEmpty(username)) {
            showToast("请输入账号",0);
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码",0);
            return false;
        }
        if (TextUtils.isEmpty(affirm_passwrd)) {
            showToast("请输入确认密码",0);
            return false;
        }

        if (!yanzhengma.equals(realCode)) {
            showToast("请输入正确的验证码",0);
            return false;
        }

        //  Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");
        Pattern p = Pattern.compile("([a-zA-Z0-9]{6,12})");
        if (!p.matcher(username).matches()) {
            showToast( "账号只能由6至12位英文或数字组成",0);
            return false;
        }
        if (!p.matcher(password).matches()) {
            showToast( "密码只能由6至12位英文或数字组成",0);
            return false;
        }

        if (!password.equals(affirm_passwrd)) {
            showToast( "两次密码必须一致",0);
            return false;
        }
        return true;
    }

    /**
     * 执行登陆传参数
     */
    private void register() {
        loginRemoteService(username, password);
    }

    private void loginRemoteService(String username, String et_password) {

        Map<String, String> map = new HashMap<String, String>();
  /*      map.put(Constants.CLIENT_ID, 12 + "");
        map.put(Constants.APP_ID, 100 + "");
        map.put("agent", "");*/
        map.put("type", 2 + "");
        map.put("username", AuthCodeUtil.authcodeEncode(username, Constants.appkey));
        map.put("password", AuthCodeUtil.authcodeEncode(et_password, Constants.appkey));
        //map.put(Constants.FROM, 3 + "");
        map.put("deviceid", "");

        OkHttpUtils.postString(Constants.USER_ADD, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                Logger.msg("用户名注册返回来的信息", res);
                if (res != null) {
                    parjsonUserRegister(res);
                }
            }
        });
    }

    private User_register_Bean user_register_bean;
    private ErrorMessageBean errorMessageBean;

    private void parjsonUserRegister(String res) {
        if (res.length() > 30 && res.length() < 60) {
           // errorMessageBean = JsonUtil.json2Bean(res, ErrorMessageBean.class);
            errorMessageBean = JsonUtil.getJsonUtil().json2Bean(res, ErrorMessageBean.class);
            isErroir = true;
        } else {
            //user_register_bean = JsonUtil.json2Bean(res, User_register_Bean.class);
            user_register_bean = JsonUtil.getJsonUtil().json2Bean(res, User_register_Bean.class);
            isErroir = false;
        }
        Message mes = Message.obtain();
        mes.what = 11;
        handler.sendMessage(mes);

    }

    /**
     * 登录成功后进去到个人中心
     */
    public void EnterHome() {
        Intent intent = new Intent(getActivity(), UerContentActivity.class);
        getActivity().finish();
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.act_in, R.anim.act_out);
    }


}
