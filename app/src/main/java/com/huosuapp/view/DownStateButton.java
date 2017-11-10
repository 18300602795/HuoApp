package com.huosuapp.view;

import android.Manifest;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huosuapp.Bean.DownCountChange;
import com.huosuapp.Bean.DownLoadSucceed;
import com.huosuapp.Bean.DownStateChange;
import com.huosuapp.MyApplication;
import com.huosuapp.Util.ApkUtils;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.download.DownloadInfo;
import com.huosuapp.download.DownloadManager;
import com.huosuapp.download.DownloadService;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.alipay.sdk.app.statistic.c.v;

/**
 *下载的按钮
 */
public class DownStateButton extends Button {
    private static final String TAG=DownStateButton.class.getSimpleName();
    private Integer gameId;
    private String gamename;
    private String icon;
    private Context mContext;
    DownloadManager downloadManager = DownloadService.getDownloadManager(getContext());

    public DownStateButton(Context context) {
        super(context);
    }

    public DownStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DownStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    public DownStateButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStateChange(DownStateChange downStateChange){
//        Log.e(TAG, "onStateChange: 收到回调 "+downStateChange.gameId);
        if(gameId!=null){
            if(gameId==downStateChange.gameId){
                refreshUi(gameId);
            }
        }
    }

    private void refreshUi(int gameId) {
        DownloadManager downloadManager = DownloadService.getDownloadManager(Global
                .getContext());
        DownloadInfo downloadInfo = downloadManager.getDownloadInfoByAppId(gameId+ "");
        if (downloadInfo==null){ //证明没下载过
            setText("下载");
            return;
        }
        HttpHandler.State state = downloadInfo.getState();



        if (state != null) {
            switch (state) {
                case WAITING:
                    setText("等待");
                    break;
                case STARTED:
                case LOADING:
                case CANCELLED:
                    setText("下载中");
                    if (state == HttpHandler.State.CANCELLED) {
                        setText("暂停");
                    }
                    break;
                case SUCCESS:
                    setText(getSuccessStr(downloadInfo));
                    break;
                case FAILURE:
                    setText("重试");
                    break;
                default:
                    break;
            }
        }
    }
    public String getSuccessStr(DownloadInfo downloadInfo){
        String packageNameByApkFile = ApkUtils.getPackageNameByApkFile(getContext().getApplicationContext(), downloadInfo.getFileSavePath());
        boolean installApp = ApkUtils.isInstallApp(getContext().getApplicationContext(), packageNameByApkFile);
        if(installApp&&downloadInfo.getIsInstallSuccess()==1){//从这个盒子安装的
            return "启动";
        }else{
            return "安装";
        }
    }
    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(int gameId,String gamename,String icon) {
        this.gameId = gameId;
        this.gamename=gamename;
        this.icon=icon;
        refreshUi(gameId);

    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    OnClickListener mOnClickListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
//            if (Build.VERSION.SDK_INT >=23) {
//                int i= ContextCompat.checkSelfPermission(mContext,permissions[0]);
//                if (i!= PackageManager.PERMISSION_GRANTED){
//                    mContext.requestPermissions(permissions,Constants.WRITE_READ_EXTERNAL_CODE);
//                }else{
//                    clickDown();
//                }
//            }else{
                clickDown();
//            }
        }
    };
    /**
     * 请求网络
     */
    private void getDownloadData() {
        Map<String, String> map = new HashMap<String, String>();
		/*map.put(Constants.CLIENT_ID, 12 + "");
		map.put(Constants.APP_ID, 100 + "");
		map.put("agent", "");
		map.put(Constants.FROM, 3 + "");*/
        map.put("verid", "0");
        map.put("gameid", gameId + "");
        map.put("openudid", "");
        map.put("deviceid", "241D66B4-B8B1-48DD-849F-1254DE37A22F");
        map.put("devicetype", "");
        map.put("idfa", "");
        map.put("idfv", "");
        map.put("mac", "");
        map.put("resolution", "1024*768");
        map.put("network", "WIFI");
        map.put("userua", "");
        String url = StringUtils.getUrlContainAppid(Constants.GAME_DOWN, map);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                DownLoadSucceed downLoadSucceed = null;
                try {
                    downLoadSucceed = JsonUtil.getJsonUtil().json2Bean(responseInfo.result, DownLoadSucceed.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (downLoadSucceed != null && downLoadSucceed.getData() != null) {
                    EventBus.getDefault().post(new DownCountChange(gameId, downLoadSucceed.getData().getDowncnt()));
                    addNewDownload(downLoadSucceed.getData().getUrl());
                    Logger.msg("点击下载返回来的消息", downLoadSucceed.getData().getUrl());
                } else {
                    Toast.makeText(getContext(), "获取地址失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getContext(), "获取地址失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //添加一个下载操作任务
    private void addNewDownload(String appUrl) {
        try {
            String target = MyApplication.apkdownload_path + gamename + ".apk";
//            String appUrl = downLoadSucceed.getData().getUrl();
            downloadManager.addNewDownload(gameId
                            + "", appUrl,
                    gamename, target,
                    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                    false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                    icon,
                    "", new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            DownloadInfo downloadInfoByAppId = downloadManager.getDownloadInfoByAppId(gameId + "");
                            if(downloadInfoByAppId!=null){
                                ApkUtils.install(downloadInfoByAppId);
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
//                            refreshUi(position);
                        }

                        @Override
                        public void onCancelled() {
//                            refreshUi(position);
                        }

                        @Override
                        public void onStart() {
//                            refreshUi(position);
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
//                            refreshUi(position);
                        }
                    });
            //showToast("游戏正在下载中！", 0);
            // Toast.makeText(context,"游戏正在下载中！",Toast.LENGTH_SHORT).show();
//            EventBus.getDefault().post(new DownStateChange(gameId));
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
            Toast.makeText(getContext(), "出现异常！", Toast.LENGTH_SHORT).show();
        }
    }

    private void clickDown(){
        DownloadInfo downloadInfo = downloadManager.getDownloadInfoByAppId(gameId+"");
        if (downloadInfo == null) {  //添加一个新的任务
            getDownloadData();
            return;
        }
        HttpHandler.State state = downloadInfo.getState();
        Logger.msg("点击网游适配器的状态", state + "");
        switch (state) {
            case WAITING:
            case STARTED:
            case LOADING:
                try {
                    downloadManager.stopDownload(downloadInfo);
                } catch (DbException e) {
                    LogUtils.e(e.getMessage(), e);
                }
                break;
            case CANCELLED:
            case FAILURE:
                try {
                    downloadManager.resumeDownload(downloadInfo, new RequestCallBack<File>() {
                                @Override
                                public void onSuccess(ResponseInfo<File> responseInfo) {
                                    DownloadInfo downloadInfoByAppId = downloadManager.getDownloadInfoByAppId(gameId + "");
                                    if(downloadInfoByAppId!=null){
                                        ApkUtils.install(downloadInfoByAppId);
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {

                                }

                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onLoading(long total, long current, boolean isUploading) {

                                }

                                @Override
                                public void onCancelled() {
                                }
                            }
                    );
                } catch (DbException e) {
                    LogUtils.e(e.getMessage(), e);
                }
                break;
            case SUCCESS:
                if (downloadInfo != null) {
                    if (!ApkUtils.install(downloadInfo)) {
                        deleteInfo(downloadInfo);
                    }
                }
                break;
            default:
                break;
        }
    }
    private void deleteInfo(DownloadInfo downloadInfo) {
        SharePrefUtil.saveBoolean(Global.getContext(), SharePrefUtil.KEY.ISDELETE, false);
        try {
            ApkUtils.deleteDownloadApk(mContext, downloadInfo.getFileName());//delete file apk from sdcard!
            DownloadService.getDownloadManager(mContext).removeDownload(downloadInfo);
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
    }
}
