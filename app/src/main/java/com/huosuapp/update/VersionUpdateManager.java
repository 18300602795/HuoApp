package com.huosuapp.update;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.huosuapp.Bean.HasNewsBean;
import com.huosuapp.MyApplication;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by 刘红亮 on 2016/4/1.
 * 版本更新管理
 */
public class VersionUpdateManager{
    private Activity mContext;
    private int versionCode;
    private String versionName;
    private VersionUpdateListener listener;
    long requestTime;

    public VersionUpdateManager() {
        EventBus.getDefault().register(this);
    }

    public void checkVersionUpdate(Activity mContext, VersionUpdateListener listener) {
        requestTime=System.currentTimeMillis();
        this.mContext = mContext;
        this.listener = listener;
        getNativeVersion();
        Map<String, String> map = new HashMap<String, String>();
        map.put("verid",versionCode+"");
        map.put("vername",versionName+"");
        map.put("clientid", "49");
        map.put("appid", "100");
        map.put("agent", "");
        map.put("from", "3");
        String url = StringUtils.getUrlContainAppid(Constants.URL_HAS_NEW_VERSION,map);
        Logger.msg("版本更新",url);
        OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=  response.body().string().trim();
                Logger.msg("获取是否有新版本更新",res);

                parHasNes(res);
            }
        });
//        OkHttpUtils.getString();
//        HttpParams params = Urls.getParams(true);
//        params.put("versionNo", ABAppUtil.getAppVersionCode() + "");
//        NetRequest.request().setParams(params).setFlag("checkVersion").post(Urls.checkVersion, this);
    }
    private HasNewsBean hasNewsBean;
    private void parHasNes(String res) {
        try {
            JSONObject jsonObject=new JSONObject(res);
            int code = jsonObject.getInt("code");
            if(code==404){
                hasNewsBean=new HasNewsBean();
                hasNewsBean.setCode(code);
                hasNewsBean.setMsg("已是最新版本无需更新!");
                EventBus.getDefault().post(hasNewsBean);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (res.length()>28&&res.length()<50){
            return;
        }
        try {
            hasNewsBean= JsonUtil.getJsonUtil().json2Bean(res,HasNewsBean.class);
            EventBus.getDefault().post(hasNewsBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void httpResult(final HasNewsBean hasNewsBean){
        if(hasNewsBean.getCode()==404){
            listener.cancel(hasNewsBean.getMsg());
            return;
        }
        if (hasNewsBean==null||hasNewsBean.getData()==null){
            listener.cancel(null);
            return;
        }else{
            new UpdateVersionDialog().showDialog(mContext,true, "有新版本发布了，是否需要下载更新？", new UpdateVersionDialog.ConfirmDialogListener() {
                @Override
                public void ok() {
                    Intent intent = new Intent(mContext, UpdateVersionService.class);
                    intent.putExtra("url",hasNewsBean.getData().getNewurl());
//                    intent.putExtra("url","http://183.61.13.174/imtt.dd.qq.com/16891/2A76B7A9A8E841F0D8C1E74AD65FCB3F.apk?mkey=57d696be4979a782&f=8f5d&c=0&fsname=com.tencent.mobileqq_6.5.3_398.apk&csr=4d5s&p=.apk");
                    mContext.startService(intent);
                    Toast.makeText(mContext, "开始下载,请在下载完成后确认安装！",Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.gotoDown();
                    }
                }
                @Override
                public void cancel() {
                    if (listener != null) {
                        listener.cancel(null);
                    }
                }
            });
        }
    }
    /**
     * 获取本地版本号
     */

    public void getNativeVersion() {
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            versionName=mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            Logger.msg("versionName",versionName);
            Logger.msg("versionCode",versionCode + "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//    @Override
//    public void onHttpResult(String data, int errorNo, String[] flag, String errorMsg,Integer requestId) {
//        if (NetResult.isSuccess(mContext, data, errorNo, errorMsg,requestId)) {
//            if ("checkVersion".equals(flag[0])) {
//                final BaseBean<VersionEvent> updateVersionEvent = JSON.parseObject(data, new TypeReference<BaseBean<VersionEvent>>(){});
//                if (updateVersionEvent.getData() != null) {
//                    new UpdateVersionDialog().showDialog(mContext, updateVersionEvent.getData().getIsConstraint() != 1, updateVersionEvent.getData().getNote(), new UpdateVersionDialog.ConfirmDialogListener() {
//                        @Override
//                        public void ok() {
//                            Intent intent = new Intent(mContext, UpdateVersionService.class);
//                                    intent.putExtra("url", Urls.getDownloadUrl(updateVersionEvent.getData().getUrl()));
//                            mContext.startService(intent);
//
//                            Toast.makeText(mContext, "开始下载,请在下载完成后确认安装！",Toast.LENGTH_SHORT).show();
//                            if (listener != null) {
//                                listener.gotoDown();
//                            }
//                        }
//                        @Override
//                        public void cancel() {
//                            if (listener != null) {
//                                listener.cancel(null);
//                            }
//                        }
//                    });
//                } else {
////                    T.s(mContext, updateVersionEvent.getMsg());
//                    if (listener != null) {
//                        listener.cancel(updateVersionEvent.getMsg());
//                    }
//                }
//            }
//        }else{
//            if (listener != null) {
//                listener.cancel(null);
//            }
//        }
//    }

    public interface VersionUpdateListener {
        /**
         * 获取到更新去下载去了
         */
        void gotoDown();

        /**
         * 无更新或者用户取消了更新
         */
        void cancel(String msg);
    }
    public void unRegister(){
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
