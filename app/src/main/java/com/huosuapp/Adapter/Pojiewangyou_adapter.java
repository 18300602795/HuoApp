package com.huosuapp.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.Bean.BreakOnlineBean;
import com.huosuapp.Bean.DownCountChange;
import com.huosuapp.Bean.DownLoadErrorBean;
import com.huosuapp.Bean.DownLoadSucceed;
import com.huosuapp.Bean.DownStateChange;
import com.huosuapp.MyApplication;
import com.huosuapp.Ui.GameDetailActivity;
import com.huosuapp.Util.ApkUtils;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.ImgUtil;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.Util.getTypeUtils;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Pojiewangyou_adapter extends BaseAdapter {
    private Activity context;
    private Typeface typeFace;
    private List<BreakOnlineBean.DataBean.GameListBean> game_list;
    private DownLoadErrorBean downLoadErrorBean;
    private DownLoadSucceed downLoadSucceed;

    public Pojiewangyou_adapter(List<BreakOnlineBean.DataBean.GameListBean> game_list, Activity context) {
        this.game_list = game_list;
        this.context = context;
        typeFace = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
    }


    /**
     * 如果原来没有数据，则返回一个空集合，如果有则返回原来的有数据的集合
     */
    public List<BreakOnlineBean.DataBean.GameListBean> getDataList() {
        if (game_list == null) {
            game_list = new ArrayList<BreakOnlineBean.DataBean.GameListBean>();
        }
        return game_list;
    }


    @Override
    public int getCount() {
        return game_list == null ? 0 : game_list.size();
        //return 3;
    }

    @Override
    public Object getItem(int position) {
        return game_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(parent.getContext(), R.layout.home_item, null);
            ViewHold vh = new ViewHold();
            vh.mItemLayout= (LinearLayout) convertView.findViewById(R.id.home_rv_layout);
            vh.tv_download = (Button) convertView.findViewById(R.id.tv_download);
            vh.iv_game_icon = (ImageView) convertView.findViewById(R.id.iv_game_icon);
            vh.tv_game_name = (TextView) convertView.findViewById(R.id.tv_game_name);
//            vh.tv_count_size = (DownCountText) convertView.findViewById(R.id.tv_count_size);
            vh.tv_game_desc = (TextView) convertView.findViewById(R.id.tv_game_desc);
            vh.tv_game_type1= (TextView) convertView.findViewById(R.id.list_item_type1);
            vh.tv_game_type2= (TextView) convertView.findViewById(R.id.list_item_type2);
            vh.tv_game_count= (TextView) convertView.findViewById(R.id.list_item_count);

            convertView.setTag(vh);
        }
        final ViewHold vh = (ViewHold) convertView.getTag();
        final BreakOnlineBean.DataBean.GameListBean gamelistbean = game_list.get(position);
        //刷新之前是否是下载过的状态
        refreshUi(vh.tv_download, position);
        if (gamelistbean.getDowncnt()+""!= null) {
            vh.tv_game_count.setText(gamelistbean.getDowncnt() + "次下载");
        }

        String[] types=getTypeUtils.getType(gamelistbean.getType());

        if (TextUtils.isEmpty(types[1])){
            vh.tv_game_type1.setText(types[0]);
            vh.tv_game_type2.setVisibility(View.GONE);
        }else{
            vh.tv_game_type1.setText(types[0]);
            vh.tv_game_type2.setVisibility(View.VISIBLE);
            vh.tv_game_type1.setText(types[1]);
        }

//        vh.tv_download.setGameId(gamelistbean.getGameid(),gamelistbean.getGamename(),gamelistbean.getIcon());
        if (gamelistbean.getIcon() != null && gamelistbean.getIcon() != "") {
            ImgUtil.loadImage(parent.getContext(),gamelistbean.getIcon(),R.drawable.ic_launcher,vh.iv_game_icon);
        }
        if (gamelistbean.getOneword() != null && gamelistbean.getOneword() != "") {
            vh.tv_game_desc.setText(gamelistbean.getOneword());
        }
        /**
         *	点击了下载
         */
        vh.tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行下载的操作
                getDownloadData(gamelistbean.getGameid(), vh.tv_download, position);
            }
        });

        vh.tv_game_name.setText(gamelistbean.getGamename());
        vh.tv_game_name.setTypeface(typeFace);
        vh.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.msg("开始跳转详情页面", "");
                startContentActivity(gamelistbean, vh);
            }
        });
        return convertView;
    }

    /**
     * 启动游戏详情界面
     */
    private void startContentActivity(BreakOnlineBean.DataBean.GameListBean gameListBean, Pojiewangyou_adapter.ViewHold holder) {
        Intent intent=new Intent(context,GameDetailActivity.class);
        intent.putExtra("gameID",gameListBean.getGameid());
        holder.iv_game_icon.getDrawingCache(true);
        Global.drawable=holder.iv_game_icon.getDrawable();
        //如果5.0以上使用
        if (android.os.Build.VERSION.SDK_INT > 20) {
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    context, holder.iv_game_icon,
                    context.getResources().getString(R.string.share_img)).toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    static class ViewHold {
        LinearLayout mItemLayout;
        ImageView iv_game_icon;
        TextView tv_game_name, tv_game_desc;
//        DownCountText tv_count_size;
        Button tv_download;
        TextView tv_game_type1;
        TextView tv_game_type2;
        TextView tv_game_count;
    }

    /**
     * 请求网络
     */
    private void getDownloadData(final int gamid, final Button v, final int position) {
        Map<String, String> map = new HashMap<String, String>();
     /*   map.put(Constants.CLIENT_ID, 12 + "");
        map.put(Constants.APP_ID, 100 + "");
        map.put("agent", "");
        map.put(Constants.FROM, 3 + "");*/
        map.put("verid", "dfeses7115");
        map.put("gameid", gamid + "");
        map.put("openudid", "");
        map.put("deviceid", "241D66B4-B8B1-48DD-849F-1254DE37A22F");
        map.put("devicetype", "");
        map.put("idfa", "");
        map.put("idfv", "");
        map.put("mac", "");
        map.put("resolution", "1024*768");
        map.put("network", "WIFI");
        map.put("userua", "");
        String url = StringUtils.getCompUrlFromParams(Constants.GAME_DOWN, map);

        OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                Logger.msg("点击下载返回来的消息", res);
                pardownloadData(res, v, gamid, position);
            }
        });
    }

    private void pardownloadData(final String res, final Button v, final int gamid, final int postion) {
        Global.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Download(res, v, gamid, postion);
            }
        });
    }

    /**
     * 下载的操作
     */
    public void Download(String res, final Button v, int gameID, final int position) {// final View v 由   View v改成
        //如果返回来是未找到地址的，就走这个判断语句
        if (res.length() > 28 && res.length() < 40) {
            // downLoadErrorBean = JsonUtil.json2Bean(res, DownLoadErrorBean.class);
            downLoadErrorBean = JsonUtil.getJsonUtil().json2Bean(res, DownLoadErrorBean.class);
            Logger.msg("到这里了吗", "111111111");
            if (downLoadErrorBean != null) {
                Global.showToast(downLoadErrorBean.getMsg(), 0);
                return;
            }
        } else {
            downLoadSucceed = JsonUtil.getJsonUtil().json2Bean(res, DownLoadSucceed.class);
            if (downLoadSucceed == null) {
                Global.showToast("连接失败", 0);
                return;
            }
            if(downLoadSucceed.getData()==null){
                Global.showToast(downLoadSucceed.getMsg(), 0);
                return;
            }
            //执行下载操作
            if (game_list == null) {
                return;
            }
            if(downLoadSucceed.getData()!=null){
                EventBus.getDefault().post(new DownCountChange(gameID,downLoadSucceed.getData().getDowncnt()));
            }else {
                Global.showToast(downLoadSucceed.getMsg(), 0);
                return;
            }
            final DownloadInfo downloadInfo = DownloadService.getDownloadManager(context).getDownloadInfoByAppId(game_list.get(position).getGameid() + "");
            if (downloadInfo == null) {
                addNewDownload(position, downloadInfo, v);
                return;
            }
            HttpHandler.State state = downloadInfo.getState();
            Logger.msg("点击网游适配器的状态", state + "");
            switch (state) {
                case WAITING:
                case STARTED:
                case LOADING:
                    try {
                        DownloadService.getDownloadManager(context).stopDownload(downloadInfo);
                    } catch (DbException e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                    break;
                case CANCELLED:
                case FAILURE:
                    try {
                        DownloadService.getDownloadManager(context).resumeDownload(downloadInfo, new RequestCallBack<File>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<File> responseInfo) {
                                        refreshUi(v, position);
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        refreshUi(v, position);
                                    }

                                    @Override
                                    public void onStart() {
                                        refreshUi(v, position);
                                    }

                                    @Override
                                    public void onLoading(long total, long current, boolean isUploading) {
                                        refreshUi(v, position);
                                    }

                                    @Override
                                    public void onCancelled() {
                                        refreshUi(v, position);
                                    }
                                }
                        );
                    } catch (DbException e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                    break;
                case SUCCESS:
                    ApkUtils.install(downloadInfo);
                    break;
                default:
                    break;

            }
            if (state != HttpHandler.State.LOADING) {
                EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
            }
        }
    }

    private void addNewDownload(final int position, final DownloadInfo downloadInfo, final Button btn) {
        Logger.msg("新添加了下载任务", "132");
        try {
            //  Logger.msg("下载的游戏名字", game_list.get(position).getGamename());
            //执行下载
            String target = MyApplication.apkdownload_path + game_list.get(position).getGamename() + ".apk";
            String appUrl = downLoadSucceed.getData().getUrl();
            DownloadService.getDownloadManager(context).addNewDownload(
                    game_list.get(position).getGameid() + "",
                    appUrl,
                    game_list.get(position).getGamename(),
                    target,
                    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                    false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                    game_list.get(position).getIcon() + "",
                    null,
                    new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            // Toast.makeText(context,"1",Toast.LENGTH_SHORT).show();
                            refreshUi(btn, position);
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            refreshUi(btn, position);
                        }

                        @Override
                        public void onStart() {
                            refreshUi(btn, position);
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            refreshUi(btn, position);

                        }

                        @Override
                        public void onCancelled() {
                            refreshUi(btn, position);

                        }
                    }
            );
            Logger.msg("新添加了下载任务1", "132");
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
            Global.showToast("出现异常", 0);
        }
    }


    private void refreshUi(Button btn, int position) {
        DownloadInfo downloadInfo = DownloadService.getDownloadManager(context).getDownloadInfoByAppId(game_list.get(position).getGameid() + "");
        Logger.msg("downloadInfo这里走了", downloadInfo + "");
        if (downloadInfo == null) {
            btn.setText("下载");
            return;
        }
        HttpHandler.State state = downloadInfo.getState();
        Logger.msg("state状态22222", state + "");
        if (state != null) {
            switch (state) {
                case WAITING:
                    btn.setText("等待");
                    Toast.makeText(context, "到这er步了", Toast.LENGTH_SHORT).show();
                    break;
                case STARTED:
                case LOADING:
                case CANCELLED:
                    btn.setText("下载中");
                    if (state == HttpHandler.State.CANCELLED) {
                        btn.setText("暂停");
                    }
                    if (state != HttpHandler.State.LOADING) {
                        EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
                    }
                    break;
                case SUCCESS:
                    btn.setText("安装");
                    EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
                    break;
                case FAILURE:
                    btn.setText("重试");
                    EventBus.getDefault().post(new DownStateChange(Integer.parseInt(downloadInfo.getAppId())));
                    break;
                default:
                    break;
            }
        }
    }



    /*else {  //根据包名打开此文件，不过这里的包名是空的。
                ApkUtils.openAppByPackageName(context, game_list.get(position).getGamename() + "");
            }*/
}



