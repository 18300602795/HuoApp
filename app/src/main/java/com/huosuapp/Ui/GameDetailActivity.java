package com.huosuapp.Ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.huosuapp.Adapter.CommentAdapter;
import com.huosuapp.Bean.CommentBean;
import com.huosuapp.Bean.CommentListBean;
import com.huosuapp.Bean.DownLoadErrorBean;
import com.huosuapp.Bean.DownLoadSucceed;
import com.huosuapp.Bean.DownStateChange;
import com.huosuapp.Bean.GameDetail;
import com.huosuapp.Dialog.CustomProgressDialog;
import com.huosuapp.MyApplication;
import com.huosuapp.Util.ApkUtils;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.ImgUtil;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.download.DownloadInfo;
import com.huosuapp.download.DownloadManager;
import com.huosuapp.download.DownloadService;
import com.huosuapp.imagedemo.ImagePagerActivity;
import com.huosuapp.text.R;
import com.huosuapp.view.TextViewExpandableAnimation;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

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

/**
 * 游戏详情页
 */

public class GameDetailActivity extends BaseActivity2 {
    private ImageAdapter imageAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar pb_downloadl;//点击了下载进度条
    private com.huosuapp.download.DownloadManager downloadManager;
    private String ATG = "获取出来的数据";

    private LinearLayout llt_gift_details;//游戏评论，礼包详情

    private CommentAdapter commentAdapter; //评论设配器
    private RelativeLayout rlt_allturn;

    private TextViewExpandableAnimation tvExpand;
    private TextView tv_title, tv_title1, tv_donwload_count, tv_size, tv_version, tv_language;
    private ImageView iv_game_icon;
    int gameID;  //传过来的游戏ID，需要在ID，来访问网络
    ExpandableTextView expandableTextView;

    @Override
    public void parUI(Message mes) {
        switch (mes.what) {
            case 1:
                GameDetail.DataBean dataBean = (GameDetail.DataBean) mes.obj;
                if (dataBean == null) {
//                    stateLayout.showFailView();
                }
                if (dataBean != null) {
//                    stateLayout.showContentView();
                }
//                tv_title.setText(dataBean.getGamename());
                tv_title1.setText(dataBean.getGamename());
                tv_donwload_count.setText("下载 : " + dataBean.getDowncnt() + " 次");
                tv_size.setText(dataBean.getSize() == "" ? "" : "大小 : " + dataBean.getSize());
                tv_version.setText(dataBean.getVername() == "" ? "版本 : 1.0" : "版本 : " + dataBean.getVername() + "");
                tv_language.setText(dataBean.getLang() == "" ? "类型 : 中文" : "类型 : " + dataBean.getLang());

                expandableTextView.setText(dataBean.getDisc());
                if (dataBean.getIcon() != null && dataBean.getIcon() != "") {
                    if (Global.drawable == null) {
                        ImgUtil.getBitmapUtils(GameDetailActivity.this).display(iv_game_icon, dataBean.getIcon() + "");
                        ImgUtil.loadImage(dataBean.getIcon() + "", iv_game_icon);
                    }
                }
                tv_download.setText(dataBean.getSize() == "" ? "" : "下载(" + dataBean.getSize() + ")");
                imageAdapter = new ImageAdapter();

                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                mRecyclerView.setAdapter(imageAdapter);
                break;
        }
    }

    public void initUI() {
        downloadManager = DownloadService.getDownloadManager(Global.getContext());

        initget();  //接收传过来的东西
        initUI1();  //初始化页面的上半部分信息

        pb_downloadl = (ProgressBar) findViewById(R.id.pb_download);
        rlt_allturn = (RelativeLayout) findViewById(R.id.ic_top);
        tv_download = (TextView) findViewById(R.id.tv_download_tip);
        mRecyclerView = (RecyclerView) findViewById(R.id.game_detail_pic);
        expandableTextView = (ExpandableTextView) findViewById(R.id.expand_text_view);

        //初始化点赞动画
        getPinglun();
        tvExpand = (TextViewExpandableAnimation) findViewById(R.id.tv_expand);
        tvExpand.setText(getString(R.string.tips) + getString(R.string.tips) + getString(R.string.tips));
        final ArrayMap<String, String> map = new ArrayMap<>();
        map.put("gameid", gameID + "");
        map.put("clientid", "49");
        map.put("appid", "100");
        map.put("agent", "");
        map.put("from", "3");
        createAnim();
    }

    private static final int pageSize = 10;
    private int page = 1;
    private int loading = 0;// 1表示加载中
    View footer = null;
    private String commentURL = Constants.URL_COMMENT_ADD;
    private String getCommentURL = Constants.URL_COMMENT_LIST;

    /**
     * 获取游戏评论列表
     */
    private void getGameCommentList(String page) {
        loading = 1;
        footer.setVisibility(View.GONE);
        Map<String, String> params = new HashMap<>();

        params.put("gameid", gameID + "");
        params.put("page", page);
        params.put("offset", pageSize + "");
        String url = StringUtils.getCompUrlFromParams(getCommentURL, params);
        OkHttpUtils.getString(url, false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Logger.msg("游戏评论", res);
                parjson2(res);
            }
        });
    }

    private void parjson1(String res) {
        if (res.length() > 28 && res.length() < 50) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                footer.setVisibility(View.VISIBLE);
            }
        });
        try {
            final CommentBean comm = JsonUtil.getJsonUtil().json2Bean(res, CommentBean.class);
            if (comm != null) {
                if ((comm.getCode() >= 400 || comm.getCode() < 200)) {
                    if (page == 1) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                footer.setVisibility(View.GONE);
                            }
                        });
                    }
                    return;
                }
            }
            list_comment.add(comm.getComment());
            if (list_comment.size() >= 10) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        footer.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        footer.setVisibility(View.GONE);
                    }
                });
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    commentAdapter = new CommentAdapter(
                            GameDetailActivity.this, list_comment,
                            myOnitemcListener);
                    commentAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {

        }
    }

    private void parjson2(String res) {
        if (res.length() > 28 && res.length() < 50) {
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                footer.setVisibility(View.VISIBLE);
            }
        });
        try {
            final CommentListBean comm = JsonUtil.getJsonUtil().json2Bean(res, CommentListBean.class);
            if (comm != null) {
                if ((comm.getCode() >= 400 || comm.getCode() < 200)) {
                    if (page == 1) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                footer.setVisibility(View.GONE);
                            }
                        });
                    }
                }
                return;
            }
            ArrayList<HashMap<String, Object>> list = comm.getCommentList();
            Log.e("listdata:", list == null ? "null" : list.toString());
            if (list != null && list.size() > 0 && list.size() < 10) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                footer.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
            list_comment.addAll(list);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loading = 0;
                    commentAdapter = new CommentAdapter(
                            GameDetailActivity.this, list_comment,
                            myOnitemcListener);
//                    pinglunListView.setAdapter(commentAdapter);
                    commentAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {

        }

    }


    private void initget() {
        gameID = getIntent().getIntExtra("gameID", 0);
    }

    private CustomProgressDialog mDialog;

    private void initUI1() {
        tv_title = (TextView) findViewById(R.id.tv_nav);
        tv_title1 = (TextView) findViewById(R.id.tv_game_name1);
        tv_donwload_count = (TextView) findViewById(R.id.tv_download_count);
        tv_size = (TextView) findViewById(R.id.tv_size);
        tv_version = (TextView) findViewById(R.id.tv_versions);
        tv_language = (TextView) findViewById(R.id.tv_language);
        iv_game_icon = (ImageView) findViewById(R.id.iv_game_icon);
        if (Global.drawable != null) {
            iv_game_icon.setImageDrawable(Global.drawable);
        }
        if (mDialog == null) {
            mDialog = new CustomProgressDialog(GameDetailActivity.this,
                    "正在加载中.....", R.drawable.donghua_frame);
        }
//        mDialog.show();
    }

    private List<HashMap<String, Object>> list_comment; // 一级评论数据
    private static final int ONE_COMMENT_CODE = -1;
    private static final int TWO_COMMENT_CODE = 2;

    private void getPinglun() {
        initList();
        initCommentData();
    }


    private void initList() {
        list_comment = new ArrayList<HashMap<String, Object>>();
    }

    private void initCommentData() {
        if (commentAdapter == null) {
            commentAdapter = new CommentAdapter(this, list_comment,
                    myOnitemcListener);
        }
        footer = getLayoutInflater().inflate(R.layout.list_footer, null);
        footer.setOnClickListener(this);
        footer.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, 64));
        footer.setVisibility(View.INVISIBLE);
    }


    private View.OnClickListener myOnitemcListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            onCreateDialog1(position);
        }
    };

    private EditText edt_reply;
    private Button btn_reply;

    protected Dialog onCreateDialog1(final int position) {
        final Dialog customDialog = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_comment, null);
        edt_reply = (EditText) mView.findViewById(R.id.edt_comments);
        btn_reply = (Button) mView.findViewById(R.id.btn_send);

        Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(mView);
        //当editText获取焦点的时候,调用显示键盘命令
        edt_reply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                customDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });


        customDialog.show();

        btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case ONE_COMMENT_CODE:
                        if (!edt_reply.getText().toString().equals("")) {
                            doComment(gameID + "", edt_reply.getText().toString(),
                                    "" + 0, "Android");
                            customDialog.dismiss();
                            edt_reply.setText("");
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        return customDialog;
    }

    /**
     * 添加游戏评论，游戏id gameid 评论内容 content 评论谁 toId 来自哪里 from 这里暂时是"Android"
     */
    public void doComment(String gameId, String content, String toId,
                          String from) {
        Map<String, String> params = new HashMap<>();
        params.put("content", content);
        params.put("toid", "" + 0);
        params.put("from", "3");
        OkHttpUtils.postString(commentURL, false, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                parjson1(res);
            }
        });
    }


    @Override
    public void initData() {
        getData();
    }

    @Override
    public int getViewID() {
        return R.layout.ttv_game_detail;
    }


    private String AFF = "GameDetailActivity";

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("gameid", gameID + "");
        Logger.msg(AFF, gameID + "");
        String url = StringUtils.getCompUrlFromParams(Constants.URL_GAME_DETAIL, map);
        Logger.msg("获取游戏详情", url);
        OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                Logger.msg(AFF, res);
                parjsonGameDetail(res);
            }
        });
    }

    public GameDetail gameDetail;

    private void parjsonGameDetail(String res) {
        if (res.length() > 28 && res.length() < 50) {
            return;
        }
        gameDetail = JsonUtil.getJsonUtil().json2Bean(res, GameDetail.class);
        if (gameDetail != null) {
            Message mes = Message.obtain();
            mes.what = 1;
            mes.obj = gameDetail.getData();
            handler.sendMessage(mes);
        }
//         getData1();
        handler.post(new Runnable() {
            @Override
            public void run() {
                refreshUi();
            }
        });
    }

    /**
     * 请求网络，获取下载链接
     */
    private void getDownloadData(final int gameid) {
        Map<String, String> map = new HashMap<>();
        map.put("verid", "dfeses7115");
        map.put("gameid", gameid + "");
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
        OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                Logger.msg("点击下载返回来的消息", res);
                pardownloadData(res, gameid);
            }
        });
    }

    /**
     * 解析返回来的下载实体
     */
    private void pardownloadData(final String res, final int gameid) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Download(res, gameid);
            }
        });
    }

    private DownLoadErrorBean downLoadErrorBean;
    private DownLoadSucceed downLoadSucceed;

    private void Download(String res, int gameid) {
        Logger.msg("获取的下载内容的长度", "" + res.length());
        if (res.length() > 28 && res.length() < 40) {
            downLoadErrorBean = JsonUtil.getJsonUtil().json2Bean(res, DownLoadErrorBean.class);
            if (downLoadErrorBean == null) {
                return;
            }

        } else {
            downLoadSucceed = JsonUtil.getJsonUtil().json2Bean(res, DownLoadSucceed.class);
            if (downLoadSucceed == null) {
                Global.showToast("连接失败", 0);
                return;
            }
            if (downLoadSucceed.getData() == null) {
                Global.showToast(downLoadSucceed.getMsg(), 0);
                return;
            }
            addNewDownload();
        }
    }

    @Override
    protected void initListener() {
        findViewById(R.id.rl_download).setOnClickListener(this);
        rlt_allturn.setOnClickListener(this);
    }


    /**
     * 本页的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_top:

                if (Build.VERSION.SDK_INT > 20) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
            case R.id.rl_download:   //点击了下载
                SharePrefUtil.saveBoolean(Global.getContext(), SharePrefUtil.KEY.ISDELETE, true);
                progressBarClick();
                break;
            case R.id.footer: //点击了游戏里礼包栏目
                getGameCommentList((++page) + "");
                break;
            default:
                break;
        }
    }


    //执行下载操作
    private void progressBarClick() {
        if (gameDetail == null) {
            return;
        }
        DownloadInfo downloadInfo = downloadManager.getDownloadInfoByAppId(gameDetail.getData().getGameid() + "");
        if (downloadInfo == null) {
            // 如果原来没有下载过，则添加一个新的下载
            if (gameDetail != null) {
                if (gameDetail.getData().getGameid() != null) {
                    getDownloadData(gameDetail.getData().getGameid());
                }
            }
            return;
        }
        HttpHandler.State state = downloadInfo.getState();
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
                    downloadManager.resumeDownload(downloadInfo, downloadStateListener);
                } catch (DbException e) {
                    LogUtils.e(e.getMessage(), e);
                }
                break;
            case SUCCESS:
                Logger.msg("开始安装", "");
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
            ApkUtils.deleteDownloadApk(this, downloadInfo.getFileName());//delete file apk from sdcard!
            downloadManager.removeDownload(downloadInfo);
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
    }


    private void addNewDownload() {
        try {
            if (downLoadErrorBean != null) {
                showToast(downLoadErrorBean.getMsg(), 0);
                return;
            }
            if (downLoadSucceed == null) {
                Global.showToast("连接失败", 0);
                return;
            }
            if (downLoadSucceed.getData() == null) {
                Global.showToast(downLoadSucceed.getMsg(), 0);
                return;
            }
            String target = MyApplication.apkdownload_path + gameDetail.getData().getGamename() + ".apk";
            String appUrl = downLoadSucceed.getData().getUrl();
            Logger.msg("下载地址", appUrl);
            downloadManager.addNewDownload(
                    gameDetail.getData().getGameid() + "",
                    appUrl,
                    gameDetail.getData().getGamename(),
                    target,
                    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                    false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                    gameDetail.getData().getIcon(),  //传入的是图片地址，因为服务器暂时没有，这里先写死、
                    null,
                    downloadStateListener);
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
    }


    private TextView tv_download;
    private DownloadStateListener downloadStateListener = new DownloadStateListener();

    public class DownloadStateListener extends RequestCallBack<File> {
        /**
         * 开始下载
         */
        @Override
        public void onStart() {
            secondRefreshUi();
        }

        /**
         * 正在下载
         */
        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            secondRefreshUi();
        }

        /**
         * 下载成功
         */
        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            secondRefreshUi();
        }

        /**
         * 下载失败
         */
        @Override
        public void onFailure(HttpException e, String s) {
            secondRefreshUi();
        }

        /**
         * 下载取消
         */
        @Override
        public void onCancelled() {
            secondRefreshUi();
        }
    }

    private void refreshUi() {
        if (gameDetail == null) {
            return;
        }
        DownloadInfo downloadInfo = downloadManager.getDownloadInfoByAppId(gameDetail.getData().getGameid() + "");
        if (downloadInfo == null) {
            //说明没有下载过
            tv_download.setText("下载(" + gameDetail.getData().getSize() + ")");
            return;
        }
        HttpHandler.State state = downloadInfo.getState();
        Logger.msg("状态2", state + "");
        Logger.msg(ATG, (state == null ? 0 : 1) + "");
        switch (state) {
            case WAITING:
                tv_download.setText("等待");
                Logger.msg(ATG, "等待");
                break;
            case STARTED:
            case LOADING:
            case CANCELLED:
                // 如果是开始状态或者正在下载的状态，或者暂停的状态，都需要显示当前的进度
                // iv_download.setVisibility(View.INVISIBLE);
                if (downloadInfo.getFileLength() > 0) {
                    int pesent = (int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength());
                    pb_downloadl.setProgress(pesent);
                    tv_download.setText(pesent + "%");
                } else {
                    pb_downloadl.setProgress(0);
                }
                if (state == HttpHandler.State.CANCELLED) {
                    tv_download.setText("暂停");
                    //  iv_download.setVisibility(View.VISIBLE);
                }
                if (state == HttpHandler.State.LOADING && downloadInfo.getHandler() != null) {
                    RequestCallBack<File> requestCallBack = downloadInfo.getHandler().getRequestCallBack();
                    if (requestCallBack != null && requestCallBack instanceof DownloadManager.ManagerCallBack) {
                        DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) requestCallBack;
                        managerCallBack.setBaseCallBack(downloadStateListener);
                    }
                }
                break;
            case SUCCESS:
                pb_downloadl.setProgress(100);
                tv_download.setText("安装");
                break;
            case FAILURE:
                tv_download.setText("重试");
                break;
            default:
                break;
        }
    }

    private void secondRefreshUi() {
        if (gameDetail == null) {
            return;
        }
        DownloadInfo downloadInfo = downloadManager.getDownloadInfoByAppId(gameDetail.getData().getGameid() + "");
        if (downloadInfo == null) {
            //说明没有下载过
            tv_download.setText("下载(" + gameDetail.getData().getSize() + ")");
            return;
        }
        HttpHandler.State state = downloadInfo.getState();
        Logger.msg("状态2", state + "");
        Logger.msg(ATG, (state == null ? 0 : 1) + "");
        switch (state) {
            case WAITING:
                tv_download.setText("等待");
                break;
            case STARTED:
            case LOADING:
            case CANCELLED:
                // 如果是开始状态或者正在下载的状态，或者暂停的状态，都需要显示当前的进度
                // iv_download.setVisibility(View.INVISIBLE);
                Logger.msg("开始下载", downloadInfo.getFileLength() + "");
                if (downloadInfo.getFileLength() > 0) {
                    int pesent = (int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength());
                    Logger.msg(ATG, pesent + "");
                    pb_downloadl.setProgress(pesent);
                    tv_download.setText(pesent + "%");
                } else {
                    pb_downloadl.setProgress(0);
                }
                if (state == HttpHandler.State.CANCELLED) {
                    tv_download.setText("暂停");
                }
                break;
            case SUCCESS:
                pb_downloadl.setProgress(100);
                tv_download.setText("安装");

                break;
            case FAILURE:
                tv_download.setText("重试");
                break;
            default:
                break;
        }
    }

    /**
     * 游戏截图页面显示
     *
     * @author janecer
     */
    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {
        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(GameDetailActivity.this);
            //设置布局图片以105*150显示 （简单解释——设置数字不一样，图片的显示大小不一样）
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(Global.dp2px
                    (150, GameDetailActivity.this), Global.dp2px(230, GameDetailActivity.this));
            params.rightMargin = Global.dp2px(10, GameDetailActivity.this);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return new ImageHolder(imageView);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, final int position) {
            String imgViewUrl = gameDetail.getData().getImage().get(position);
            //监听图片加载

            Glide.with(GameDetailActivity.this).load(imgViewUrl).
                    crossFade(1000).//淡入淡出,注意:如果设置了这个,则必须要去掉asBitmap
                    placeholder(R.drawable.icon_vertical).thumbnail(0.1f).
                    into(new GlideDrawableImageViewTarget(holder.mImageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
//                    if (mDialog.isShowing()){
//                        mDialog.dismiss();
//                    }
                        }
                    });
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> url = (ArrayList<String>) gameDetail.getData().getImage();
                    Intent intent = new Intent(GameDetailActivity.this, ImagePagerActivity.class);
                    intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, url);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    GameDetailActivity.this.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return gameDetail.getData().getImage() == null ? 3 : gameDetail.getData().getImage().size();
        }
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ImageHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView;
        }
    }

    //开场动画
    private void createAnim() {
        mRecyclerView.setAlpha(0);
        mRecyclerView.setTranslationY(100);
        rlt_allturn.setScaleX(0);

        mRecyclerView.animate()
                .setDuration(800)
                .translationY(0)
                .alpha(1);
        rlt_allturn.animate().setDuration(800).scaleX(1);
    }

    @Override
    protected void onDestroy() {
        Global.drawable = null;
        EventBus.getDefault().post(new DownStateChange(gameID));
        super.onDestroy();

    }
}
