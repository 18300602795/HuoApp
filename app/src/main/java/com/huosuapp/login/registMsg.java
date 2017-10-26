package com.huosuapp.login;

import android.content.Context;

import com.huosuapp.Bean.HotBeanRefresh1;
import com.huosuapp.Bean.validateCodeBean;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;


import java.io.IOException;
import java.net.URL;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by admin on 2016/8/29.
 */
public class registMsg {

    private static final  String MSG="registMsg";

    /**
     * 获取手机验证码，并返回 发送短信的session,不需要缓存
      */
    public  void getValidateCode(Context appContext, String Url,
                                  Map<String, String> map) {
        // 定义要访问的接口和要强转的实体
        OkHttpUtils.postString(Url, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                Logger.msg(MSG,res);
                parjson(res);
            }
        });

    };

    private void parjson(String res) {
        validateCodeBean validateCodeBean = JsonUtil.getJsonUtil().json2Bean(res, validateCodeBean.class);
        validateCodeBean.getData().getSessionid();

    }

}
