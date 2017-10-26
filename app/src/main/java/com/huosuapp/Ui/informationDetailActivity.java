package com.huosuapp.Ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.Dialog.CustomProgressDialog;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Logger;
import com.huosuapp.text.R;

/**
 * Created by admin on 2016/8/6.
 */
public class informationDetailActivity extends BaseActivity {
    private ImageButton imageButton;//分享
    private RelativeLayout rlt_allrerurn;

    private WebView webView;
    private ProgressBar download_ProgressBar;
    private WebSettings webSettings;
    private int webID; //传过来的WebViewID
    private int gameid;//传过来的gameid。
    private String gameTitle, gameIcon;//传过来的标题和图片
    private TextView tv_download;
    private  ProgressBar progressBar1;
    @Override
    public void parUI(Message mes) {

    }

    @Override
    protected void initListener() {
        download_ProgressBar.setOnClickListener(this);
        rlt_allrerurn.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        gameTitle = getIntent().getStringExtra("gameTitle");
        gameIcon = getIntent().getStringExtra("gameicon");
        gameid = getIntent().getIntExtra("gameid", 0);
        Logger.msg("资讯详情游戏ID",gameid+"");
        webID = getIntent().getIntExtra("id", 0);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        webView = (WebView) findViewById(R.id.lv_information_webview);
        download_ProgressBar = (ProgressBar) findViewById(R.id.download_progressBar1);
        rlt_allrerurn = (RelativeLayout) findViewById(R.id.rl_nav);
        tv_download = (TextView) findViewById(R.id.tv_download_tip);
        // imageButton= (ImageButton) findViewById(R.id.ib_share);

        //初始化点赞动画
        // animation= AnimationUtils.loadAnimation(this,R.anim.nn);
        //getPinglun();
    }


 /*   private List<HashMap<String, Object>> list_comment; // 一级评论数据
    private static final int ONE_COMMENT_CODE = -1;
    private static final int TWO_COMMENT_CODE = 2;
    private LinearLayout llt_pinglun;  //点击评论
*/
 /*   private void getPinglun() {
    *//*    listView_pinglun= (ListView) findViewById(R.id.lv_comments);
        llt_pinglun= (LinearLayout) findViewById(R.id.llt_pinglun);
        llt_pinglun.setOnClickListener(this);
        initList();
        initCommentData();*//*
    }
*//**/

 /*   private void initList() {
        list_comment=new ArrayList<HashMap<String, Object>>();
    }

    private void initCommentData() {
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("content","火速牛B");
        list_comment.add(map);
        if (commentAdapter==null){
            commentAdapter=new InformationCommentAdapter(this,list_comment,myOnitemcListener);
        }
        listView_pinglun.setAdapter(commentAdapter);
    }
*/

/*
    private View.OnClickListener myOnitemcListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int position = (Integer) v.getTag();
            System.out.println("-----------" + position);
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
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        customDialog.setContentView(mView);
        customDialog.show();

        btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case ONE_COMMENT_CODE:
                        if (!edt_reply.getText().toString().equals("")) {
                            HashMap<String, Object> comment = new HashMap<String, Object>();
                            comment.put("content", edt_reply.getText().toString());
                            list_comment.add(comment);
                            commentAdapter.clearList();
                            commentAdapter.updateList(list_comment
                            );
                            commentAdapter.notifyDataSetChanged();
                            customDialog.dismiss();
                            edt_reply.setText("");
                        }
                        break;
                    default:
                        if (!edt_reply.getText().toString().equals("")) {
                            HashMap<String, Object> comment = new HashMap<String, Object>();
                            comment.put("content", edt_reply.getText().toString());
                            commentAdapter.clearList();
                            commentAdapter.updateList(list_comment
                            );
                            commentAdapter.notifyDataSetChanged();
                            customDialog.dismiss();
                            edt_reply.setText("");
                        }
                        break;
                }
            }
        });
        return customDialog;
    }*/


    @Override
    public void initData() {
        //初始化webView
        stateLayout.showContentView();
        getWebViewData();

        //请求网络获取链接
//        getDownLoadData();
    }

//    private void getDownLoadData() {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put(Constants.CLIENT_ID, 12 + "");
//        map.put(Constants.APP_ID, 100 + "");
//        map.put("agent", "");
//        map.put(Constants.FROM, 3 + "");
//        map.put("verid", "dfeses7115");
//        map.put("gameid", gameid + "");
//        map.put("openudid", "");
//        map.put("deviceid", "241D66B4-B8B1-48DD-849F-1254DE37A22F");
//        map.put("devicetype", "");
//        map.put("idfa", "");
//        map.put("idfv", "");
//        map.put("mac", "");
//        map.put("resolution", "1024*768");
//        map.put("network", "WIFI");
//        map.put("userua", "");
//        String url = StringUtils.getCompUrlFromParams(Constants.GAME_DOWN, map);
//
//        OkHttpUtils.getString(url, false, new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                String res = response.body().string().trim();
////                Logger.msg("点击下载返回来的消息", res);
//                pardownloadData(res, gameid);
//            }
//        });
//    }

//    private void pardownloadData(final String res, final int gameid) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Download(res, gameid);
//            }
//        });
//    }

//    private DownLoadErrorBean downLoadErrorBean;
//    private DownLoadSucceed downLoadSucceed;

//    private void Download(String res, int gameid) {
//        if (res.length() > 28 && res.length() < 40) {
//            downLoadErrorBean = JsonUtil.getJsonUtil().json2Bean(res, DownLoadErrorBean.class);
//        } else {
//            downLoadSucceed = JsonUtil.getJsonUtil().json2Bean(res, DownLoadSucceed.class);
//            Logger.msg("成功解析出来了", downLoadSucceed.getData().getUrl());
//        }
//    }

    CustomProgressDialog dialog;

    private void getWebViewData() {
        //WebView加载web资源
        // String path = "http://sdk.huosdk.com/api/public/v1/news/webdetail/"+gameid+"?appid=1&agent=&clientid=1";
//        String path = Constants.NEWS_WEBDETAIL_ID + webID + "?appid=1&agent=&clientid=1";
        String path = Constants.NEWS_WEBDETAIL_ID + webID + "?clientid="+Constants.clientid+"&appid="+Constants.appid+"&agent="+Constants.agent;

        webSettings = webView.getSettings();
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放

        webSettings.setLoadWithOverviewMode(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // //支持通过JS打开新窗口

        webView.requestFocusFromTouch();

        webSettings.setSupportZoom(true);  //支持缩放

        webView.loadUrl(path);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (dialog == null) {
                    dialog = new CustomProgressDialog(informationDetailActivity.this,
                            "正在加载中.....", R.drawable.donghua_frame);
                }
//                dialog.show();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
            }
        });


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100){
                    progressBar1.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    progressBar1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar1.setProgress(newProgress);//设置进度值
                }
            }
        });

    }


    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();//goBack()表示返回WebView的上一页面
            return true;
        }
        finish();
        return false;
    }


    @Override
    public int getViewID() {
        return R.layout.informationdetail_main;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
          /*  case R.id.ib_share:  //弹出分享窗口
                showSharePop();
                //Toast.makeText(this,"点击了分享",Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.rl_nav:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
          /*  case R.id.llt_pinglun:
                onCreateDialog1(ONE_COMMENT_CODE);

                break;
            case R.id.iv_zan://点击了赞：
                tv_zan.setText((Integer.parseInt(tv_zan.getText().toString()) + 1) + "");
                tv_one.setVisibility(View.VISIBLE);
                view.setBackgroundResource(R.drawable.btn_dianzan);
                view.setEnabled(false);
                tv_one.startAnimation(animation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_one.setVisibility(View.GONE);
                    }
                },500);
                break;*/
            case R.id.download_progressBar1:   //点击了下载
//                progressBarClick();
                break;
        }
    }

//    private void progressBarClick() {
//        if (TextUtils.isEmpty(gameid + "")) {
//            return;
//        }
//        DownloadInfo downloadInfo = DownloadService.getDownloadManager(this).getDownloadInfoByAppId(gameid + "");
//        Logger.msg("资讯详情游戏ID", gameid + "");
//        if (downloadInfo == null) {
//            addNewDownload();
//        }
//    }
//
//    private void addNewDownload() {
//        try {
//            if (downLoadErrorBean != null) {
//                showToast(downLoadErrorBean.getMsg(), 0);
//                Logger.msg("点击下载错误信息", downLoadErrorBean.getMsg());
//                return;
//            }
//            if (downLoadSucceed == null) {
//                return;
//            }
//            String target = MyApplication.apkdownload_path + gameTitle + ".apk";
//            String appUrl = downLoadSucceed.getData().getUrl();
//            Logger.msg("下载地址", appUrl);
//            DownloadService.getDownloadManager(this).addNewDownload(
//                    gameid + "",
//                    appUrl,
//                    gameTitle,
//                    target,
//                    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
//                    false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
//                    gameIcon,  //传入的是图片地址，因为服务器暂时没有，这里先写死、
//                    null,
//                    downloadStateListener);
//        } catch (DbException e) {
//            LogUtils.e(e.getMessage(), e);
//        }
//    }

//    private DownloadStateListener downloadStateListener = new DownloadStateListener();
//    public class DownloadStateListener extends RequestCallBack<File> {
//        /**
//         * 开始下载
//         */
//        @Override
//        public void onStart() {
//            refreshUi();
//        }
//        /**
//         * 正在下载
//         */
//        @Override
//        public void onLoading(long total, long current, boolean isUploading) {
//            refreshUi();
//        }
//
//        /**
//         * 下载成功
//         */
//        @Override
//        public void onSuccess(ResponseInfo<File> responseInfo) {
//            refreshUi();
//        }
//
//        /**
//         * 下载失败
//         */
//        @Override
//        public void onFailure(HttpException e, String s) {
//            refreshUi();
//        }
//
//        /**
//         * 下载取消
//         */
//        @Override
//        public void onCancelled() {
//            refreshUi();
//        }
//    }

//    private String ATG="informationDetailActivity";
//    private void refreshUi() {
//        if (TextUtils.isEmpty(gameid+"")|gameid==0){
//            return;
//        }
//        DownloadInfo downloadInfo = DownloadService.getDownloadManager(this).getDownloadInfoByAppId(gameid+"");
//        if (downloadInfo==null){
//            //说明没有下载过
//            tv_download.setText("下载");
//            return;
//        }
//        HttpHandler.State state = downloadInfo.getState();
//        Logger.msg("状态2",state+"");
//        Logger.msg(ATG, (state == null ? 0 : 1) + "");
//        switch (state) {
//            case WAITING:
//                tv_download.setText("等待");
//                Logger.msg(ATG, "等待");
//                break;
//            case STARTED:
//            case LOADING:
//            case CANCELLED:
//                // 如果是开始状态或者正在下载的状态，或者暂停的状态，都需要显示当前的进度
//                // iv_download.setVisibility(View.INVISIBLE);
//                if (downloadInfo.getFileLength() > 0) {
//                    int pesent = (int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength());
//                    Logger.msg(ATG, pesent + "");
//                    download_ProgressBar.setProgress(pesent);
//                    tv_download.setText(pesent + "%");
//                } else {
//                    download_ProgressBar.setProgress(0);
//                }
//                if (state == HttpHandler.State.CANCELLED) {
//                    tv_download.setText("暂停");
//                    //  iv_download.setVisibility(View.VISIBLE);
//                }
//                break;
//            case SUCCESS:
//                download_ProgressBar.setProgress(100);
//                tv_download.setText("安装");
//                break;
//            case FAILURE:
//                tv_download.setText("重试");
//                break;
//            default:
//                break;
//        }
//    }


//    private void showSharePop() {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View PopView = inflater.inflate(R.layout.share_pop, null);
//        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
//        PopupWindow window = new PopupWindow(PopView,
//                WindowManager.LayoutParams.MATCH_PARENT,
//                Global.dp2px(150, informationDetailActivity.this));
//        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
//        window.setFocusable(true);
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        window.setBackgroundDrawable(dw);
//        window.setAnimationStyle(R.style.mypopwindow_anim_style);
//        window.showAtLocation(imageButton,
//                Gravity.BOTTOM, 0, 0);
//        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//
//            }
//        });
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.setVisibility(View.GONE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        webSettings.setJavaScriptEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webSettings.setJavaScriptEnabled(true);
    }
}
