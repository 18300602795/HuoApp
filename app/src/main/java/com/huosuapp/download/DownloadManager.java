package com.huosuapp.download;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.huosuapp.Bean.DownStateChange;
import com.huosuapp.MyApplication;
import com.huosuapp.Util.ApkUtils;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.SharePrefUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wyouflf
 * Date: 13-11-10
 * Time: 下午8:10
 */
public class DownloadManager {

    private List<DownloadInfo> downloadInfoList;
    private  String appid;
    private  String url;
    private  String fileName;
    private String target;
    private String imageurl;
    private  String baoming;
    private  RequestCallBack<File> callback;

    private int maxDownloadThread = 10;

    private Context mContext;
    private DbUtils db;

    /*package*/
    DownloadManager(Context appContext) {
        ColumnConverterFactory.registerColumnConverter(HttpHandler.State.class,
                new HttpHandlerStateConverter());
        mContext = appContext;
        db = DbUtils.create(mContext);
        try {
            downloadInfoList = db.findAll(Selector.from(DownloadInfo.class));
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
        if (downloadInfoList == null) {
            downloadInfoList = new ArrayList<DownloadInfo>();
        }
    }

    public int getDownloadInfoListCount() {
        return downloadInfoList.size();
    }

    public DownloadInfo getDownloadInfo(int index) {
        return downloadInfoList.get(index);
    }

    /**
     * 根据url地址获得downloadinfo
     * @param url
     * @return
     */
    public DownloadInfo getDownloadInfo(String url){
        for (DownloadInfo downinfo :downloadInfoList) {
            if(downinfo.getDownloadUrl().equals(url)){
                return downinfo;
            }
        }
        return null;
    }


    /**
     * 根据filename名字获得downloadinfo
     */
    public DownloadInfo getDownloadfilename(String filename){
        for (DownloadInfo downinfo :downloadInfoList) {
            if(downinfo.getDownloadUrl().equals(filename)){
                return downinfo;
            }
        }
        return null;
    }

    /**
     * 通过应用id获取对应的下载信息
     * @param appId 应用id
     * @return
     */
    public DownloadInfo getDownloadInfoByAppId(String appId) {
    	for (DownloadInfo downloadInfo : downloadInfoList) {
			if (appId.equals(downloadInfo.getAppId())) {
				return downloadInfo;
			}
		}
    	return null;
    }


    /**
     * 根据游戏包名返回一个downloadInfo
     * @param packagename
     * @return
     */
    public DownloadInfo getDownloadInfoByPackagename(String packagename){
        for (DownloadInfo downinfo : downloadInfoList) {
            if(downinfo.getBaoming().equals(packagename)){
                return downinfo;
            }
        }
        return null;
    }
    public DownloadInfo getDownloadInfoByPname(String packageName){
        for (DownloadInfo downinfo : downloadInfoList) {
            if(downinfo.getPackageName().equals(packageName)){
                return downinfo;
            }
        }
        return null;
    }




    private Toast toast = null;
    /** 不会一直重复重复重复重复的提醒了 */
    protected void showToast(String msg, int length) {
        if (toast == null) {
            toast = Toast.makeText(mContext, msg, length);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }


    public void addNewDownload(String appId, String url, String fileName, String target,
                               boolean autoResume, boolean autoRename,String imageurl, String baoming,
                               final RequestCallBack<File> callback) throws DbException {

        this.appid = appId;
        this.url = url;
        this.fileName = fileName;
        this.target = target;
        this.imageurl = imageurl;
        this.baoming = baoming;
        this.callback = callback;
        final DownloadInfo downloadInfo = new DownloadInfo();
        /**
         * 自己添加的
         */
        downloadInfo.setImageurl(imageurl);//头像下载地址
        downloadInfo.setBaoming(baoming);//游戏包名
        downloadInfo.setTitle(fileName);

        downloadInfo.setAppId(appId);

        String agent=SharePrefUtil.getString(mContext,SharePrefUtil.KEY.AGENT,"");
//        if (!agent.equals("")){
            //先去掉最后一个斜杠后面的字符
            int lastOne=url.lastIndexOf("/");
            url=url.substring(0, lastOne);
            //去掉倒数第二斜杠后面的字符
            int lastTwo=url.lastIndexOf("/");
            String gameID=url.substring(lastTwo+1);
            url=url.substring(0, lastTwo);
            //拿到最后下划线后面的渠道id
//            int i=agent.lastIndexOf("_");
//            String agentID=agent.substring(i+1);
            //拼接成新的
            url=url+"/"+gameID+"/"+gameID+"_"+ MyApplication.uid+".apk";
            Logger.msg("下载的地址：", url);
//        }else{
////            url=url+"abc";
//            Logger.msg("下载的地址：", url);
//        }
        downloadInfo.setDownloadUrl(url);
        Log.e("-----------", "addNewDownload: "+url);

        downloadInfo.setAutoRename(autoRename);
        downloadInfo.setAutoResume(autoResume);
        downloadInfo.setFileName(fileName);
        downloadInfo.setFileSavePath(target);
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(url, target, autoResume, autoRename, new ManagerCallBack(downloadInfo, callback));
        downloadInfo.setHandler(handler);
        downloadInfo.setState(handler.getState());
        downloadInfoList.add(downloadInfo);
        db.saveBindingId(downloadInfo);
    }

    public void resumeDownload(int index, final RequestCallBack<File> callback) throws DbException {
        final DownloadInfo downloadInfo = downloadInfoList.get(index);
        resumeDownload(downloadInfo, callback);
    }

    public void resumeDownload(DownloadInfo downloadInfo, final RequestCallBack<File> callback) throws DbException {
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(
                downloadInfo.getDownloadUrl(),
                downloadInfo.getFileSavePath(),
                downloadInfo.isAutoResume(),
                downloadInfo.isAutoRename(),
                new ManagerCallBack(downloadInfo, callback));
        downloadInfo.setHandler(handler);
        downloadInfo.setState(handler.getState());
        db.saveOrUpdate(downloadInfo);
    }

    public void removeDownload(int index) throws DbException {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        removeDownload(downloadInfo);
    }

    public void removeDownload(DownloadInfo downloadInfo) throws DbException {
        HttpHandler<File> handler = downloadInfo.getHandler();
        if (handler != null && !handler.isCancelled()) {
            handler.cancel();
        }
        downloadInfoList.remove(downloadInfo);
        db.delete(downloadInfo);
        
        // 把apk文件删除
        String apkFilePath = downloadInfo.getFileSavePath();
        File apkFile = new File(apkFilePath);
        if (apkFile.exists()) {
        	apkFile.delete();
		}
    }

    public void stopDownload(int index) throws DbException {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        stopDownload(downloadInfo);
    }

    public void stopDownload(DownloadInfo downloadInfo) throws DbException {
        HttpHandler<File> handler = downloadInfo.getHandler();
        if (handler != null && !handler.isCancelled()) {
            handler.cancel();
        } else {
            downloadInfo.setState(HttpHandler.State.CANCELLED);
        }
        db.saveOrUpdate(downloadInfo);
    }

    public void stopAllDownload() throws DbException {
        for (DownloadInfo downloadInfo : downloadInfoList) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null && !handler.isCancelled()) {
                handler.cancel();
            } else {
                downloadInfo.setState(HttpHandler.State.CANCELLED);
            }
        }
        db.saveOrUpdateAll(downloadInfoList);
    }

    public void backupDownloadInfoList() throws DbException {
        for (DownloadInfo downloadInfo : downloadInfoList) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
        }
        db.saveOrUpdateAll(downloadInfoList);
    }

    public int getMaxDownloadThread() {
        return maxDownloadThread;
    }

    public void setMaxDownloadThread(int maxDownloadThread) {
        this.maxDownloadThread = maxDownloadThread;
    }

    public class ManagerCallBack extends RequestCallBack<File> {
        private DownloadInfo downloadInfo;
        private RequestCallBack<File> baseCallBack;

        public RequestCallBack<File> getBaseCallBack() {
            return baseCallBack;
        }

        public void setBaseCallBack(RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
        }

        private ManagerCallBack(DownloadInfo downloadInfo, RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
            this.downloadInfo = downloadInfo;
        }

        @Override
        public Object getUserTag() {
            if (baseCallBack == null) return null;
            return baseCallBack.getUserTag();
        }

        @Override
        public void setUserTag(Object userTag) {
            if (baseCallBack == null) return;
            baseCallBack.setUserTag(userTag);
        }

        @Override
        public void onStart() {
            Logger.msg("开始下载","");
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onStart();
            }
            try {
                EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancelled() {
            Logger.msg("取消下载","");
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onCancelled();
            }
            try {
                EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            Logger.msg("下载中","");
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            downloadInfo.setFileLength(total);
            downloadInfo.setProgress(current);
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onLoading(total, current, isUploading);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            Logger.msg("下载成功","");
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                String packageName=ApkUtils.getPackageNameByApkFile(MyApplication.getContext(),downloadInfo.getFileSavePath());
                downloadInfo.setPackageName(packageName);
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onSuccess(responseInfo);
            }
            try {
                EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            Logger.msg("下载失败","error：" + error + "   msg：" + msg);
            if (msg.equals("maybe the file has downloaded completely")){
                try {
                    addNewDownload(appid, url, fileName, target, false, false, imageurl, baoming, callback);
                } catch (DbException e) {

                }
                return;
            }
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onFailure(error, msg);
            }
            try {
                EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
    public void setInstallSuccess(DownloadInfo downloadInfo){
        downloadInfo.setIsInstallSuccess(1);
        try {
            db.saveOrUpdate(downloadInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private class HttpHandlerStateConverter implements ColumnConverter<HttpHandler.State> {

        @Override
        public HttpHandler.State getFieldValue(Cursor cursor, int index) {
            return HttpHandler.State.valueOf(cursor.getInt(index));
        }

        @Override
        public HttpHandler.State getFieldValue(String fieldStringValue) {
            if (fieldStringValue == null) return null;
            return HttpHandler.State.valueOf(fieldStringValue);
        }

        @Override
        public Object fieldValue2ColumnValue(HttpHandler.State fieldValue) {
            return fieldValue.value();
        }

        @Override
        public ColumnDbType getColumnDbType() {
            return ColumnDbType.INTEGER;
        }
    }
}
