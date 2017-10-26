package com.huosuapp.Fragment;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.Bean.ErrorMessageBean;
import com.huosuapp.Bean.Phone_register_Bean;
import com.huosuapp.Bean.UserInfo;
import com.huosuapp.Bean.validateCodeBean;
import com.huosuapp.Ui.UerContentActivity;
import com.huosuapp.Util.AuthCodeUtil;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.text.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 手机注册的fragment
 */
public class phone_register_fragment extends Basefragment implements View.OnClickListener {
    private TextView textView;
    private EditText etphone, autocode, etphone_password;
    private Button get_btn_code, btn_register;
    private UserInfo userInfo;
    private String sessionid;
    private Boolean isErroir = true;   //默认是错误信息

    @Override
    public void parUI(Message mes) {
        switch (mes.what) {
            case 100:
                if(validateCodeBean==null||validateCodeBean.getData()==null){
                    Toast.makeText(getContext(),"获取短信验证码失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                sessionid = validateCodeBean.getData().getSessionid();
                break;
            case 200:
                if(phone_register_bean==null&&errorMessageBean==null){
                    Toast.makeText(getContext(),"注册失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isErroir) {
                    if (phone_register_bean.getCode() >= 200 && phone_register_bean.getCode() <= 300) {
                        Toast.makeText(context, "恭喜你，注册成功了", Toast.LENGTH_SHORT).show();
                        //把返回来的用户名，密码存储到sp里面
                        String identifier = phone_register_bean.getData().getIdentifier();
                        String accesstoken = phone_register_bean.getData().getAccesstoken();
                        int expaire_time = phone_register_bean.getData().getExpaire_time();
                        SharePrefUtil.saveString(Global.getContext(), SharePrefUtil.KEY.IDENTIFIER, identifier);
                        SharePrefUtil.saveString(Global.getContext(), SharePrefUtil.KEY.ACCESSTOKEN, accesstoken);
                        SharePrefUtil.saveInt(Global.getContext(), SharePrefUtil.KEY.EXPAIRE_TIME, expaire_time);
                        SharePrefUtil.saveBoolean(Global.getContext(), SharePrefUtil.KEY.FIRST_LOGIN, false);  //表示用户已经登录了，以后都要保持这个状态

                        SharePrefUtil.saveString(Global.getContext(), SharePrefUtil.KEY.NICHENG, phoneusername);//保存用户的账号，（也就是昵称）
                        EnterHome();  //注册成功去到个人中心界面设置，
                    }
                }
                else {
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
        return R.layout.phone_register;
    }

    @Override
    public void initView() {
        stateLayout.showContentView();
        userInfo = new UserInfo();
        btn_register = findView(R.id.btn_register);
        etphone_password = findView(R.id.et_phone_password);
        textView = findView(R.id.tv_huosu);
        etphone = findView(R.id.et_phone);
        autocode = findView(R.id.et_auth_code);
        get_btn_code = findView(R.id.get_auto_code);
        textView.setText(String.format(getString(R.string.register_protocol),getString(R.string.app_name)));
        //textView.setText("注册即表示同意" + Html.fromHtml("<u>" + "《火树用户协议》" + "</u>"));

    }

    @Override
    public void initListener() {
        get_btn_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public CharSequence getTitle() {
        return "手机注册";
    }

    @Override
    public void onClick(View view) {
        initText();
        switch (view.getId()) {
            case R.id.get_auto_code:
                if (!isvalidate()) {
                    break;
                }
                Logger.msg("手机号是多少", phoneusername);
                changeBtnGetCode();
                Map<String, String> map = new HashMap<String, String>();
                map.put("type", 1 + "");
                String str = AuthCodeUtil.authcodeEncode(phoneusername, Constants.appkey);
                Logger.msg("加密后的手机号", str);
                map.put("mobile", str);
                getValidateCode(Constants.SMSCODE_SEND, map);
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }


    //定于变量
    private boolean tag = true;
    private int i = 60; //定于获取验证码60秒
    private String valicationCode; //客户端输入的验证码
    private String userName;//输入的手机号（账号）

    /**
     * 验证码改变
     */
    private void changeBtnGetCode() {
        new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        if (phone_register_fragment.this.getActivity() == null) {
                            break;
                        }
                        phone_register_fragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                get_btn_code.setText("获取验证码(" + i + ")");
                                get_btn_code.setClickable(false);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;
                if (phone_register_fragment.this.getActivity() != null) {
                    phone_register_fragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            get_btn_code.setText("重新发送");
                            get_btn_code.setClickable(true);
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 说明：注册
     */
    public void register() {
        // 1.首先判断输入的值是否有效
        // 2.然后判断输入的验证码是否有效（防止没有点击获取验证码自己填的错误验证码)
        // 3.最后注册
        Pattern p = Pattern.compile("([a-zA-Z0-9]{6,12})");
        if (!isvalidate()) {
            return;
        }
        if (!p.matcher(password).matches()) {
            showToast("密码只能由6至12位英文或数字组成",0);
            return;
        }
        if (TextUtils.isEmpty(yanzhengma) && yanzhengma.length() != 4) {
            showToast("请输入4位的短信验证码",0);
            return;
        }
        //去访问借口，开始注册，
        getCheckCode();
    }

    private void getCheckCode() {
        if (sessionid==null){
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
     /*   map.put(Constants.CLIENT_ID, 12 + "");
        map.put(Constants.APP_ID, 100 + "");
        map.put(Constants.FROM,3+"");*/
        map.put("type", 1 + "");
        map.put("username", AuthCodeUtil.authcodeEncode(phoneusername,Constants.appkey));
        map.put("mobile", AuthCodeUtil.authcodeEncode(phoneusername, Constants.appkey));
        Logger.msg("手机加密后", AuthCodeUtil.authcodeEncode(phoneusername, Constants.appkey));
        map.put("smscode", yanzhengma + "");
        map.put("sessionid", sessionid + "");
        map.put("password", AuthCodeUtil.authcodeEncode(password, Constants.appkey) + "");
        map.put("deviceid", "");
        OkHttpUtils.postString(Constants.USER_ADD, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string().trim();
                Logger.msg("获取出来的注册数据", result);
                parregister(result);
            }
        });
    }

    private Phone_register_Bean phone_register_bean;
    private ErrorMessageBean errorMessageBean;

    private void parregister(String result) {
        if (result.length() > 30 & result.length() < 60) {
            errorMessageBean = JsonUtil.getJsonUtil().json2Bean(result, ErrorMessageBean.class);
            isErroir = true;
        } else {
            phone_register_bean = JsonUtil.getJsonUtil().json2Bean(result, Phone_register_Bean.class);
            isErroir = false;
        }
        Message mes = Message.obtain();
        mes.what = 200;
        handler.sendMessage(mes);
    }

    /**
     * get方法拼接
     *
     * @return
     */
    public String getCompUrlFromParams(String url, Map<String, String> params) {
        String url1 = "";
        if (null != url && !"".equals(url) && null != params
                && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            for (Map.Entry<String, String> entity : params.entrySet()) {
                sb.append(entity.getKey()).append("=")
                        .append(entity.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            url1 = url + sb.toString();
        }
        return url1;
    }


    /**
     * 获取验证码
     */
    private String MSG = "yanzhengma";

    public void getValidateCode(String Url, Map<String, String> map) {
        // 定义要访问的接口和要强转的实体
        OkHttpUtils.postString(Url, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                parValidateCode(res);
            }
        });
    }

    private validateCodeBean validateCodeBean;

    private void parValidateCode(String res) {
      //  validateCodeBean = JsonUtil.json2Bean(res, validateCodeBean.class);
        validateCodeBean = JsonUtil.getJsonUtil().json2Bean(res, validateCodeBean.class);
        Message mes = Message.obtain();
        mes.what = 100;
        handler.sendMessage(mes);
    }


    /**
     * 点击获取验证码，判断手机是否是空，和手机号是否有误，返回true则是合法的，false则不合法的
     */
    String yanzhengma;
    String password;//用户输入的注册密码
    String phoneusername;//用户注册输入的手机号

    public void initText() {
        phoneusername = etphone.getText().toString().trim();
        password = etphone_password.getText().toString().trim();
        yanzhengma = autocode.getText().toString().trim();
    }

    private boolean isvalidate() {
        // 获取控件输入的值
        if (TextUtils.isEmpty(phoneusername)) {
            showToast("手机号不能为空",0);
            return false;
        }
        if (!StringUtils.isPhoneNumberValid(phoneusername)) {
            showToast("手机号有误",0);
            return false;
        }
        return true;
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
