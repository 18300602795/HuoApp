package com.huosuapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.Dialog.CustomProgressDialog;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.NetworkImpl;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.pay.AndroidJSInterfaceForWeb;
import com.huosuapp.text.R;

import java.util.HashMap;

/**
 * Created by admin on 2016/8/31.
 */
public class HelpActivity extends BaseActivity {
    private WebView webView;
    private TextView tv_title;
    private RelativeLayout all_return;
    private int type;
    String title;
    String url;
    private WebSettings webSettings;
    String LunbotuUrl;// 获取轮播图的加载图片
    HashMap<String, String> header = new HashMap<String, String>();

    @Override
    protected void initListener() {
        all_return.setOnClickListener(this);
    }

    @Override
    public int getViewID() {
        return R.layout.help_activity;
    }

    @Override
    public void initUI() {
        stateLayout.showContentView();
        tv_title = (TextView) findViewById(R.id.tv_nav1);
        all_return = (RelativeLayout) findViewById(R.id.rlt_allreturn);
        type = getIntent().getIntExtra("help", 0);
        LunbotuUrl = getIntent().getStringExtra("lunbotuURL");
        setTitle(type);
        webView = (WebView) findViewById(R.id.help_webView);

    }

    public void setTitle(int type) {
        String clientid = Constants.clientid;
        String appid = Constants.appid;
        String agent = Constants.agent;
        String from = Constants.from;
        switch (type) {
            case 0:
                tv_title.setText("客服中心");
                url = Constants.URL_BASE_FOR_KEFU+"/float.php/Mobile/Help/index";
                break;
            case 1:
                tv_title.setText("密码修改");
                url = Constants.BASE+"/float.php/Mobile/Password/uppwd";
                break;
            case 2:
                tv_title.setText("密保邮箱");
                url = Constants.BASE+"/float.php/Mobile/Security/email";
                break;
            case 3:
                tv_title.setText("密保手机");
                url = Constants.BASE+"/float.php/Mobile/Security/mobile";
                break;
            case 4:
                tv_title.setText("钱包");
                url = Constants.BASE+"/float.php/Mobile/Wallet/charge";
                break;
            case 5:
                tv_title.setText("充值记录");
                url = Constants.BASE+"/float.php/Mobile/Wallet/charge_detail";
//                url = Constants.URL_BASE
//                        + "/user/wallet/add_list?clientid="+Constants.clientid+"&appid="+Constants.appid+"&agent="+Constants.agent;;
                break;
            case 6:
                tv_title.setText("消费记录");
                url = Constants.BASE+"/float.php/Mobile/Wallet/pay_detail";
                break;
            case 7:
                url = LunbotuUrl;
                all_return.setVisibility(View.GONE);
                break;
            case 8:
                url = LunbotuUrl;
                all_return.setVisibility(View.GONE);
                break;
            case 9:
                tv_title.setText("忘记密码");
//                http://www.9quyx.com/float.php/Mobile/Forgetpwd/index
                url = Constants.BASE+"/float.php/Mobile/Forgetpwd/index";
//                url = Constants.URL_BASE + "/user/passwd/find?clientid="+Constants.clientid+"&appid="+Constants.appid+"&agent="+Constants.agent;
                break;
        }
        url = url + "?clientid=" + clientid + "&appid=" + appid + "&agent="
                + agent + "&from=" + from;
    }

    @Override
    public void initData() {
        // 初始化webView
        getWebViewData();
        //  initWB(webView);

    }

    private CustomProgressDialog dialog;

    private void getWebViewData() {
        // WebView加载web资源
        webSettings = webView.getSettings();
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放

        webSettings.setLoadWithOverviewMode(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // //支持通过JS打开新窗口

        webView.requestFocusFromTouch();

        webSettings.setSupportZoom(true); // 支持缩放

        header.put("timestamp", OkHttpUtils.Gettimestamp() + "");
        String token = OkHttpUtils.gethstoken();
        header.put("hs-token", token + "");
//        Log.e("helpActivity","token="+token);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.contains("wpa")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (dialog == null) {
                    dialog = new CustomProgressDialog(HelpActivity.this,
                            "正在加载中.....", R.drawable.donghua_frame);
                }
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(dialog!=null){
                    dialog.dismiss();
                }

            }
        });
        webView.clearCache(true);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url,header);
    }

    private void initWB(final WebView wv){
        webSettings=webView.getSettings();
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.contains("wpa")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setDefaultTextEncodingName("UTF-8");
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        AndroidJSInterfaceForWeb js = new AndroidJSInterfaceForWeb();
        js.ctx = this;
        wv.addJavascriptInterface(js, "android");

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (dialog == null) {
                    dialog = new CustomProgressDialog(HelpActivity.this,
                            "正在加载中.....", R.drawable.donghua_frame);
                }
                dialog.show();

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.contains("wpa")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
                webviewCompat(wv);

            }

        });
        wv.setWebChromeClient(new WebChromeClient());
        webviewCompat(wv);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除
        header.put("timestamp", OkHttpUtils.Gettimestamp() + "");
        String token = OkHttpUtils.gethstoken();
        header.put("hs-token", token + "");
        wv.loadUrl(url, header);
    }
    private void webviewCompat(WebView mWebView) {
        if (NetworkImpl.isNetWorkConneted(mWebView.getContext())) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebView.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();// goBack()表示返回WebView的上一页面
            return true;
        }
        finish();
        return false;
    }

    private void showSharePop() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View PopView = inflater.inflate(R.layout.share_pop, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        PopupWindow window = new PopupWindow(PopView,
                WindowManager.LayoutParams.MATCH_PARENT, Global.dp2px(150,
                HelpActivity.this));
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(webView, Gravity.BOTTOM, 0, 0);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

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

    @Override
    public void parUI(Message mes) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlt_allreturn:
                finish();
                overridePendingTransition(R.anim.act_back_out_in,
                        R.anim.act_back_in_out);
                break;
        }
    }
}
