package com.huosuapp.Fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huosuapp.Bean.DownStateChange;
import com.huosuapp.Util.ApkUtils;
import com.huosuapp.Util.DimensionUtil;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.download.DownloadInfo;
import com.huosuapp.download.DownloadService;
import com.huosuapp.text.R;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载完成列表
 */
public class DownloadInstalledFragment extends Basefragment {
    private com.huosuapp.download.DownloadManager downloadManager;
    private MyDownloadListAdapter mydownload_adapter;
    private Context ctx;
    private String ATG = "图片地址";
    private List<DownloadInfo> installList;
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
        downloadManager = DownloadService.getDownloadManager(ctx);
        getInstallApk();

    }
    public void getInstallApk(){
        installList =new ArrayList<>();
        if(downloadManager==null) return;
        for(int i=0;i<downloadManager.getDownloadInfoListCount();i++){
            DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
            if(downloadInfo!=null){
                String packageNameByApkFile = ApkUtils.getPackageNameByApkFile(getContext().getApplicationContext(), downloadInfo.getFileSavePath());
                boolean installApp = ApkUtils.isInstallApp(getContext().getApplicationContext(), packageNameByApkFile);
                if(installApp&&downloadInfo.getIsInstallSuccess()==1){//从这个盒子安装的
                    downloadInfo.setPackageName(packageNameByApkFile);
                    installList.add(downloadInfo);
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
        for (DownloadInfo downinfo : installList) {
            if(downinfo.getBaoming().equals(packagename)){
                return downinfo;
            }
        }
        return null;
    }

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
            return installList.size();
        }

        @Override
        public Object getItem(int position) {
            return installList.get(position);
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
                vh = new ViewHold_download(installList.get(position), convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHold_download) convertView.getTag();
            }

            vh.tv_game_name.setText(installList.get(position).getFileName());
            Glide.with(context).load(installList.get(position).getImageurl())
                    .placeholder(R.drawable.ic_launcher).into(vh.iv_game_icon);
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
            tv_game_size.setVisibility(View.INVISIBLE);
            progressBar1.setVisibility(View.INVISIBLE);
            tv_net_speed.setVisibility(View.INVISIBLE);
            tv_download.setText("启动");
            tv_download.setOnClickListener(this);
            tl_gift_delete.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_download:
                    boolean isOpen = ApkUtils.openAppByPackageName(getContext().getApplicationContext(), downloadInfo.getPackageName());
                    if(!isOpen){
                        Toast.makeText(getContext(),"打开失败！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.tl_gift_delete://删除下载任务
                    SharePrefUtil.saveBoolean(Global.getContext(),SharePrefUtil.KEY.ISDELETE,false);
                    try {
                        ApkUtils.deleteDownloadApk(ctx, downloadInfo.getFileName());//delete file apk from sdcard!
                        downloadManager.removeDownload(downloadInfo);
                        installList.remove(downloadInfo);
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
        return "已安装游戏";
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }
}
