package com.huosuapp.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.huosuapp.Bean.DownStateChange;
import com.huosuapp.Bean.SpeedBean;
import com.huosuapp.Util.ApkUtils;
import com.huosuapp.Util.DimensionUtil;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.ImgUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.download.DownloadInfo;
import com.huosuapp.download.DownloadService;
import com.huosuapp.text.R;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载中的列表
 */
public class download_textqueue_fragment extends Basefragment {
    private com.huosuapp.download.DownloadManager downloadManager;
    private MyDownloadListAdapter mydownload_adapter;
    private Context ctx;
    private String ATG = "图片地址";
    private List<DownloadInfo> unInstallList;
    @Override
    public void parUI(Message mes) {

    }

    private Handler handler=new Handler(){
        private AbsListView.LayoutParams lp;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UI_SLIDER:// 实现下拉
                    View view = (View) msg.obj;
                    lp = (AbsListView.LayoutParams) view.getLayoutParams();
                    lp.height = height;
                    view.setLayoutParams(lp);
                    break;
                case UI_SLIDER_LAST:
                    View view1 = (View) msg.obj;
                    height = DimensionUtil.dip2px(getActivity(), 80);
                    boolean isSlider = (Boolean) view1.getTag(R.string.view_tag);
                    if ((isSlider) == false) {
                        lp.height = height;
                    }
                    view1.setLayoutParams(lp);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getActivity().getApplicationContext();
        IntentFilter filter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        getActivity().registerReceiver(receiver_download, filter);
        downloadManager = DownloadService.getDownloadManager(ctx);
        getUnInstallApk();


    }
    public void getUnInstallApk(){
        unInstallList=new ArrayList<>();
        if(downloadManager==null) return;
        for(int i=0;i<downloadManager.getDownloadInfoListCount();i++){
            DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
            if(downloadInfo!=null){
                String packageNameByApkFile = ApkUtils.getPackageNameByApkFile(getContext().getApplicationContext(), downloadInfo.getFileSavePath());
                boolean installApp = ApkUtils.isInstallApp(getContext().getApplicationContext(), packageNameByApkFile);
                if(!installApp){
                    downloadInfo.setPackageName(packageNameByApkFile);
                    unInstallList.add(downloadInfo);
                }else if(downloadInfo.getIsInstallSuccess()==0){//安装过了，但不是从这个盒子安装的，显示，准备覆盖安装
                    downloadInfo.setPackageName(packageNameByApkFile);
                    unInstallList.add(downloadInfo);
                }
            }
        }
    }
    /**
     * 根据游戏包名返回一个downloadInfo
     * @param packagename
     * @return
     */
    public DownloadInfo getDownloadInfoByPackagename(String packagename){
        for (DownloadInfo downinfo : unInstallList) {
            if(downinfo.getBaoming().equals(packagename)){
                return downinfo;
            }
        }
        return null;
    }
    /**
     * 注册广播接收者
     *
     * @return
     */

    private BroadcastReceiver receiver_download = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getStringExtra("androidurl");
            //Logger.msg(TAG, "___:"+packageName+"安装成功！收到消息了");
            Log.e("-------", "安装成功！收到消息了: " +packageName);
            if (unInstallList != null) {
                DownloadInfo downloadinfo = getDownloadInfoByPackagename(packageName);
                if (null != downloadinfo) {
                    try {
                        unInstallList.remove(downloadinfo);
                        mydownload_adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };

    /**
     * 下载列队适配器
     */
    private class MyDownloadListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MyDownloadListAdapter() {
            inflater = getActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return unInstallList.size();
        }

        @Override
        public Object getItem(int position) {
            return unInstallList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHold_download vh;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.download_item_text, null);
                vh = new ViewHold_download(unInstallList.get(position), convertView);
                convertView.setTag(vh);

            } else {
                vh = (ViewHold_download) convertView.getTag();
                vh.update(unInstallList.get(position));
            }
//            ImgUtil.getBitmapUtils(ctx).display(vh.iv_game_icon, unInstallList.get(position).getImageurl());

            ImgUtil.loadImage(unInstallList.get(position).getImageurl(),vh.iv_game_icon);
            HttpHandler<File> handler = unInstallList.get(position).getHandler();
            if (handler != null) {
                RequestCallBack callBack = handler.getRequestCallBack();
                if (callBack instanceof com.huosuapp.download.DownloadManager.ManagerCallBack) {
                    com.huosuapp.download.DownloadManager.ManagerCallBack managerCallBack = (com.huosuapp.download.DownloadManager.ManagerCallBack) callBack;
                    managerCallBack.setBaseCallBack(new DownloadRequestCallBack());
                }
                callBack.setUserTag(new WeakReference<ViewHold_download>(vh));
            }
            vh.refresh();
            boolean mark = convertView.getTag(R.string.view_tag) == null ? false : (Boolean) convertView.getTag(R.string.view_tag);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DimensionUtil.dip2px(ctx, mark ? 132 : 80)));
            convertView.setTag(R.string.view_tag, mark);
            return convertView;
        }
    }


    public class ViewHold_download implements View.OnClickListener {

        ImageView iv_game_icon;
        TextView tv_game_name, tv_game_size, tv_net_speed, tv_download;
        ProgressBar progressBar1;
        TableLayout tl_gift, tl_gift_delete;
        private DownloadInfo downloadInfo;

        public ViewHold_download(DownloadInfo donwloadinfo, View convertView) {
            this.downloadInfo = donwloadinfo;

            iv_game_icon = (ImageView) convertView.findViewById(R.id.iv_game_icon);
            tv_game_name = (TextView) convertView.findViewById(R.id.tv_game_name);
            tv_download = (TextView) convertView.findViewById(R.id.tv_download);
            tv_game_size = (TextView) convertView.findViewById(R.id.tv_game_size);
            tv_net_speed = (TextView) convertView.findViewById(R.id.tv_net_speed);
            tl_gift = (TableLayout) convertView.findViewById(R.id.tl_gift);
            tl_gift_delete = (TableLayout) convertView.findViewById(R.id.tl_gift_delete);
            progressBar1 = (ProgressBar) convertView.findViewById(R.id.progressBar1);
        }

        /**
         * 刷新下载进度
         */
        public void refresh() {

            //Logger.msg(TAG, "在刷新下载进度吗？");
            HttpHandler.State state = downloadInfo.getState();
            Logger.msg("下载列队的state",state+"");
            switch (state) {
                case WAITING:
                case STARTED:
                case LOADING:
                    tv_download.setText("暂停");
                    tv_game_size.setVisibility(View.VISIBLE);
                    progressBar1.setVisibility(View.VISIBLE);
                    tv_net_speed.setVisibility(View.VISIBLE);
                    break;
                case CANCELLED:
                    tv_download.setText("继续");
                    tv_game_size.setVisibility(View.VISIBLE);
                    progressBar1.setVisibility(View.VISIBLE);
                    tv_net_speed.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    tv_game_size.setVisibility(View.INVISIBLE);
                    progressBar1.setVisibility(View.INVISIBLE);
                    tv_net_speed.setVisibility(View.INVISIBLE);
                    downloadInfo.isDownloadComplete = true;
                    tv_download.setText("安装");//执行安装
                    break;
                case FAILURE:
                    tv_download.setText("重试");
                    break;
                default:
                    break;
            }
          //
            tv_download.setOnClickListener(this);
            tl_gift_delete.setOnClickListener(this);
            tv_game_name.setText(downloadInfo.getFileName());
            tv_game_size.setText(ApkUtils.getFormatSize(downloadInfo.getFileLength()));//设置文件大小
            tv_net_speed.setText(ApkUtils.getFormatSize(downloadInfo.getProgress() - downloadInfo.getProgress()) + "/s");

            if (downloadInfo.getFileLength() > 0) {
                progressBar1.setProgress((int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength()));
                downloadInfo.setProgress(downloadInfo.getProgress());
            } else {
                progressBar1.setProgress(0);
            }
        }


        public void refreshs() {
            // Logger.msg(TAG, "在刷新下载进度吗？");
//            ImgUtil.getBitmapUtils(ctx).display(iv_game_icon, downloadInfo.getImageurl());
            ImgUtil.loadImage(downloadInfo.getImageurl(),iv_game_icon);

            HttpHandler.State state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                case LOADING:
                    tv_download.setText("暂停");
                    tv_game_size.setVisibility(View.VISIBLE);
                    progressBar1.setVisibility(View.VISIBLE);
                    tv_net_speed.setVisibility(View.VISIBLE);
                    break;
                case CANCELLED:
                    tv_download.setText("继续");
                    tv_game_size.setVisibility(View.VISIBLE);
                    progressBar1.setVisibility(View.VISIBLE);
                    tv_net_speed.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    tv_game_size.setVisibility(View.INVISIBLE);
                    progressBar1.setVisibility(View.INVISIBLE);
                    tv_net_speed.setVisibility(View.INVISIBLE);
                    downloadInfo.isDownloadComplete = true;
                    tv_download.setText("安装");//执行安装
                    EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
                    ApkUtils.install(downloadInfo);
//                    Intent intent_install = new Intent();
//                    intent_install.setAction(Intent.ACTION_VIEW);
//                    File file = new File(MyApplication.apkdownload_path + downloadInfo.getTitle() + ".apk");
//
//                    intent_install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                    startActivity(intent_install);
                    break;
                case FAILURE:
                    tv_download.setText("重试");
                    EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
                    break;
                default:
                    break;
            }
        /*    ImageCache.getBitmap(downloadInfo.getImageurl(),ctx, new ImageCache.ImageCallBack() {
                @Override
                public void LoadImageFromIntenet(Bitmap bm) {
                    if(null!=bm){
                        iv_game_icon.setImageBitmap(bm);
                    }
                }
                @Override
                public void LoadImageBefore(Bitmap bm) {
                    iv_game_icon.setImageBitmap(bm);
                }
            });*/

            Logger.msg(ATG, downloadInfo.getImageurl());
            tv_game_name.setText(downloadInfo.getFileName());
            tv_download.setOnClickListener(this);
            tl_gift_delete.setOnClickListener(this);
            long fileLength = downloadInfo.getFileLength();
            if(fileLength<=0){
                tv_game_size.setText("未知大小");//设置文件大小
            }else{
                tv_game_size.setText(ApkUtils.getFormatSize(downloadInfo.getFileLength()));//设置文件大小
            }
            if(tv_net_speed.getTag()!=null){
                SpeedBean tag = (SpeedBean) tv_net_speed.getTag();
                long chaTime = (System.currentTimeMillis() - tag.getCurrentTime())/1000;
                if(chaTime>0){
                    tv_net_speed.setText(ApkUtils.getFormatSize((downloadInfo.getProgress() - tag.getCurrentSize())/chaTime) + "/s");
                }
            }
            tv_net_speed.setTag(new SpeedBean(downloadInfo.getProgress(),System.currentTimeMillis()));
            if (downloadInfo.getFileLength() > 0) {
                progressBar1.setProgress((int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength()));
                downloadInfo.setProgress(downloadInfo.getProgress());
            } else {
                progressBar1.setProgress(0);
            }
        }

        public void update(DownloadInfo downloadinfo) {
            this.downloadInfo = downloadinfo;
            refresh();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_download:
                    HttpHandler.State state = downloadInfo.getState();
                    switch (state) {
                        case WAITING:
                        case STARTED:
                        case LOADING:
                            try {
                                downloadManager.stopDownload(downloadInfo);
                                ((TextView) v).setText("开始");
                            } catch (DbException e) {
                                LogUtils.e(e.getMessage(), e);
                            }
                            break;
                        case CANCELLED:
                        case FAILURE:
                            try {
                                downloadManager.resumeDownload(downloadInfo, new DownloadRequestCallBack());
                                ((TextView) v).setText("暂停");
                            } catch (DbException e) {
                                LogUtils.e(e.getMessage(), e);
                            }
                            mydownload_adapter.notifyDataSetChanged();
                            break;
                        case SUCCESS:
//                            Intent intent_install = new Intent();
//                            intent_install.setAction(Intent.ACTION_VIEW);
//                            File file = new File(MyApplication.apkdownload_path + downloadInfo.getTitle() + ".apk");
//                            //Logger.msg(TAG, "下载完成后的路径：" + file.getPath());
//                            intent_install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                            startActivity(intent_install);
                            ApkUtils.install(downloadInfo);
                            break;
                        default:
                            break;
                    }
                    break;
                case R.id.tl_gift_delete://删除下载任务
                    SharePrefUtil.saveBoolean(Global.getContext(),SharePrefUtil.KEY.ISDELETE,false);
                    try {
                        ApkUtils.deleteDownloadApk(ctx, downloadInfo.getFileName());//delete file apk from sdcard!
                        downloadManager.removeDownload(downloadInfo);
                        unInstallList.remove(downloadInfo);
                        mydownload_adapter.notifyDataSetChanged();
                    } catch (DbException e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                    break;
                default:
                    break;
            }
            EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
        }
    }

    private class DownloadRequestCallBack extends RequestCallBack<File> {

        @SuppressWarnings("unchecked")
        private void refreshListItem() {
            // Logger.msg(TAG,"userTag是为null吗？"+(userTag==null)+"");
            if (userTag == null) return;
            WeakReference<ViewHold_download> tag = (WeakReference<ViewHold_download>) userTag;
            ViewHold_download holder = tag.get();
            //  Logger.msg(TAG," holder是为null吗？"+(holder==null)+"");
            if (holder != null) {
                holder.refreshs();
            }
        }

        @Override
        public void onStart() {
            refreshListItem();
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            refreshListItem();
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            refreshListItem();
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            refreshListItem();
        }

        @Override
        public void onCancelled() {
            refreshListItem();
        }
    }


    @Override
    public View getContentView() {
        return null;
    }

    @Override
    public int getContentViewLayoutId() {

        return R.layout.download_queue;
    }

    private ListView downListView;

    @Override
    public void initView() {
        height = DimensionUtil.dip2px(getActivity(), 80);
        desity = (int) DimensionUtil.density(getActivity());

        downListView = findView(R.id.lv_download_history);
        View view_header = new View(getActivity());
        view_header.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, DimensionUtil.dip2px(
                getActivity(), 4)));
        downListView.addHeaderView(view_header);// 为listview设置上边距

        mydownload_adapter = new MyDownloadListAdapter();
        if (mydownload_adapter!=null){
            stateLayout.showContentView();
        }
        downListView.setAdapter(mydownload_adapter);

    }

    @Override
    public void initListener() {
        downListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSlider(view);
            }
        });
    }

    /**
     * listview点击item
     */
    private int height;// listview item的高度
    private int desity = 1;
    private static final int UI_SLIDER = 10;
    private static final int UI_SLIDER_LAST = 11;

    private void itemSlider(final View view) {
        boolean isSlider = (Boolean) view.getTag(R.string.view_tag);
        if (isSlider) {
            view.setTag(R.string.view_tag, false);
            height = view.getLayoutParams().height;
            new Thread() {
                public void run() {
                    int i = 0;
                    while (i < 9) {
                        try {
                            this.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        height -= 6 * desity;
                        Message msg = Message.obtain();
                        msg.what = i == 8 ? UI_SLIDER_LAST : UI_SLIDER;
                        msg.obj = view;
                        handler.sendMessage(msg);
                        i++;
                    }
                }

                ;
            }.start();
        } else {
            view.setTag(R.string.view_tag, true);
            new Thread() {
                public void run() {
                    int i = 0;
                    while (i < 9) {
                        try {
                            this.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        height += 6 * desity;
                        Message msg = Message.obtain();
                        msg.what = i == 8 ? UI_SLIDER_LAST : UI_SLIDER;
                        msg.obj = view;
                        handler.sendMessage(msg);
                        i++;
                    }
                }

                ;
            }.start();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public CharSequence getTitle() {
        return "下载列队";
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver_download);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }
}
