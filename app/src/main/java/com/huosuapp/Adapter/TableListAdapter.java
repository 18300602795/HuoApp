package com.huosuapp.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huosuapp.Bean.DownCountChange;
import com.huosuapp.Bean.DownLoadErrorBean;
import com.huosuapp.Bean.DownLoadSucceed;
import com.huosuapp.Bean.DownStateChange;
import com.huosuapp.Bean.KaifubiaoBean;
import com.huosuapp.MyApplication;
import com.huosuapp.Ui.GameDetailActivity;
import com.huosuapp.Util.ApkUtils;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.StringUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public class TableListAdapter extends BaseAdapter {
    private List<KaifubiaoBean> mBeanList = new ArrayList<>();
    private Activity activity;
    private DownLoadErrorBean downLoadErrorBean;
    private DownLoadSucceed downLoadSucceed;

    public TableListAdapter(List<KaifubiaoBean> mBeanList, Activity activity) {
        this.mBeanList = mBeanList;
        this.activity = activity;
    }

    public void addData(List<KaifubiaoBean> beanList) {
        mBeanList.addAll(beanList);
        notifyDataSetChanged();
    }

    public List<KaifubiaoBean> getDateList(){
        if (mBeanList == null){
            mBeanList = new ArrayList<>();
        }
        return mBeanList;
    }

    /**
     * 启动游戏详情界面
     */
    private void startContentActivity(KaifubiaoBean tableBean, TableListHolder holder) {
        Intent intent = new Intent(activity, GameDetailActivity.class);
        intent.putExtra("gameID", Integer.valueOf(tableBean.gamedata.id));
        holder.icon.getDrawingCache(true);
        Global.drawable = holder.icon.getDrawable();
        //如果5.0以上使用
        if (android.os.Build.VERSION.SDK_INT > 20) {
            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    activity, holder.icon,
                    activity.getResources().getString(R.string.share_img)).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    public int getCount() {
        return mBeanList == null ? 0 : mBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeanList == null ? null : mBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TableListHolder holder;
        if (convertView == null) {
            holder = new TableListHolder();
            convertView = View.inflate(parent.getContext(), R.layout.table_recycle_layout, null);
            holder.mCardView= (CardView) convertView.findViewById(R.id.table_recycle_card);
            holder.area = (TextView) convertView.findViewById(R.id.table_recycle_area);
            holder.data = (TextView) convertView.findViewById(R.id.table_recycle_data);
            holder.gameName = (TextView) convertView.findViewById(R.id.table_recycle_game_name);
            holder.icon = (ImageView) convertView.findViewById(R.id.table_recycle_icon);
            holder.state = (Button) convertView.findViewById(R.id.table_recycle_state);
            convertView.setTag(holder);
        } else {
            holder = (TableListHolder) convertView.getTag();
        }
        final KaifubiaoBean tableBean = mBeanList.get(position);
        refreshUi(holder.state,position);
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String data = sdf.format(new Date(Long.valueOf(tableBean.D + "000")));
        holder.data.setText(tableBean.A + "  " + data);
        holder.gameName.setText(tableBean.B);
        holder.area.setText(tableBean.C);
        if (!tableBean.gamedata.icon.equals("")) {
            Glide.with(activity)
                    .load(tableBean.gamedata.icon).placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .thumbnail(0.2f)
                    .into(holder.icon);

            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        Intent intent3 = new Intent(TableActivity.this, GameDetailActivity.class);
//                        intent3.putExtra("gameID", tableBean.getGameId());
//                        startActivity(intent3);
//                    startContentActivity(tableBean, holder);
                    Logger.msg("111","开始下载");
                    getDownloadData(Integer.valueOf(tableBean.gamedata.id), holder.state, position);
                }
            });

            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        Intent intent3 = new Intent(TableActivity.this, GameDetailActivity.class);
//                        intent3.putExtra("gameID", tableBean.getGameId());
//                        startActivity(intent3);
                    startContentActivity(tableBean, holder);
                }
            });

        } else {
            holder.icon.setImageResource(R.drawable.ic_launcher);
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity, "敬请期待", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }

    class TableListHolder {
        public CardView mCardView;
        public TextView gameName;
        public TextView data;
        public TextView area;
        public ImageView icon;
        public Button state;
    }

    /**
     * 请求网络
     */
    private void getDownloadData(final int gamid, final Button v, final int position) {
        Logger.msg("111", "开始下载");
        Map<String, String> map = new HashMap<>();
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
        Logger.msg("下载的详细详细", url);
        OkHttpUtils.getString(url, false, new Callback() {
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
                return;
            }
            //执行下载操作
            if (mBeanList == null) {
                return;
            }
            if(downLoadSucceed.getData()!=null){
                EventBus.getDefault().post(new DownCountChange(gameID,downLoadSucceed.getData().getDowncnt()));
            }
            final DownloadInfo downloadInfo = DownloadService.getDownloadManager(activity).getDownloadInfoByAppId(mBeanList.get(position).gamedata.id + "");
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
                        DownloadService.getDownloadManager(activity).stopDownload(downloadInfo);
                    } catch (DbException e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                    break;
                case CANCELLED:
                case FAILURE:
                    try {
                        DownloadService.getDownloadManager(activity).resumeDownload(downloadInfo, new RequestCallBack<File>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<File> responseInfo) {
                                        refreshUi( v, position);
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        refreshUi(v, position);
                                    }

                                    @Override
                                    public void onStart() {
                                        refreshUi( v, position);
                                    }

                                    @Override
                                    public void onLoading(long total, long current, boolean isUploading) {
                                        refreshUi( v, position);
                                    }

                                    @Override
                                    public void onCancelled() {
                                        refreshUi( v, position);
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
        }
    }

    private void addNewDownload(final int position, final DownloadInfo downloadInfo, final Button btn) {
        Logger.msg("新添加了下载任务","132");
        try {
            //  Logger.msg("下载的游戏名字", game_list.get(position).getGamename());
            //执行下载
            String target = MyApplication.apkdownload_path + mBeanList.get(position).gamedata.id + ".apk";
            String appUrl = downLoadSucceed.getData().getUrl();
            DownloadService.getDownloadManager(activity).addNewDownload(
                    mBeanList.get(position).gamedata.id + "",
                    appUrl,
                    mBeanList.get(position).B,
                    target,
                    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                    false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                    mBeanList.get(position).gamedata.icon + "",
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
                            refreshUi( btn, position);

                        }

                        @Override
                        public void onCancelled() {
                            refreshUi( btn, position);

                        }
                    }
            );
            Logger.msg("新添加了下载任务1","132");
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
            Global.showToast("出现异常", 0);
        }
    }


    private void refreshUi( Button btn, int position) {
        DownloadInfo downloadInfo = DownloadService.getDownloadManager(activity).getDownloadInfoByAppId(mBeanList.get(position).gamedata.id + "");
//        Logger.msg("downloadInfo这里走了", downloadInfo + "");
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
                    Toast.makeText(activity, "到这er步了", Toast.LENGTH_SHORT).show();
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
}
