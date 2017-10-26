package com.huosuapp.Ui;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.Adapter.Mainadapter;
import com.huosuapp.AppManager;
import com.huosuapp.Fragment.Fenleifragment;
import com.huosuapp.Fragment.Homefragment1;
import com.huosuapp.Fragment.Pojiefragment;
import com.huosuapp.Fragment.giftHomefragment;
import com.huosuapp.Fragment.informationfragment;
import com.huosuapp.MyApplication;
import com.huosuapp.Util.ChannelUtil;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.text.R;
import com.huosuapp.update.VersionUpdateManager;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 主页面
 */
public class MainActivity extends FragmentActivity implements OnClickListener,
        VersionUpdateManager.VersionUpdateListener {
    private ArrayList<Fragment> list;//装载着viewpager里面的Fragment
    private ViewPager mpPager; //Viewpager
    private TextView tv_home, tv_stand, tv_gift, tv_information, tv_online;
    private int index;
    private Mainadapter mMainadapter;
    private ImageView img_setting, iv_search, img;
    private EditText edt_seach;
    VersionUpdateManager manager = new VersionUpdateManager();
    private ImageView mSplashImage;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //软键盘不挤压窗口：
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        list = new ArrayList<>();
        list.add(new Homefragment1());
        list.add(new Pojiefragment());
        list.add(new Fenleifragment());
        list.add(new giftHomefragment());
        list.add(new informationfragment());
        setContentView(R.layout.activity_main);
        initUiView();
        AppManager.getAppManager().addActivity(this);
        mMainadapter = new Mainadapter(getSupportFragmentManager(), list);
        mpPager.setAdapter(mMainadapter);
        // 让屏幕的左右两边可以最多存在7个Fragment对象
        mpPager.setOffscreenPageLimit(list.size());
        /**
         * 监听Viewpager滑动改变
         */
        mpPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                index = position;
                initColor();
                switch (position) {
                    case 0:
                        tv_home.setTextColor(Color.parseColor("#1a9df5"));
                        break;
                    case 1:
                        tv_online.setTextColor(Color.parseColor("#1a9df5"));
                        break;
                    case 2:
                        tv_stand.setTextColor(Color.parseColor("#1a9df5"));
                        break;
                    case 3:
                        tv_gift.setTextColor(Color.parseColor("#1a9df5"));
                        break;
                    case 4:
                        tv_information.setTextColor(Color.parseColor("#1a9df5"));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });
        manager.checkVersionUpdate(this, this);//检查更新
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSplashImage.animate().setDuration(500)
                    .translationY(-mSplashImage.getHeight())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mSplashImage.setVisibility(View.GONE);
                        }
                    });
        }
    };

    /**
     * 初始化控件
     */
    private void initUiView() {
        mpPager = (ViewPager) findViewById(R.id.view_pager);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_stand = (TextView) findViewById(R.id.tv_stand);
        tv_gift = (TextView) findViewById(R.id.tv_gift);
        img_setting = (ImageView) findViewById(R.id.iv_setting);
        tv_information = (TextView) findViewById(R.id.tv_information);
        tv_online = (TextView) findViewById(R.id.tv_online);
        img = (ImageView) findViewById(R.id.iv_logo);
        edt_seach = (EditText) findViewById(R.id.et_search);//点击了搜索框
        edt_seach.setInputType(InputType.TYPE_NULL);  //// 禁止输入（不弹出输入法）
        iv_search = (ImageView) findViewById(R.id.iv_search);
        mSplashImage = (ImageView) findViewById(R.id.main_splash);

        initlistener();
        OnfocusChangListener();
        initType();//设置字体样式
        inithome();//默认是选择第一项的
    }


    /**
     * 控件初始化监听
     */
    private void initlistener() {
        edt_seach.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent1 = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.inanimation, R.anim.outanimation);
                }
                return false;
            }
        });
        tv_home.setOnClickListener(this);
        tv_stand.setOnClickListener(this);
        tv_gift.setOnClickListener(this);
        tv_information.setOnClickListener(this);
        tv_online.setOnClickListener(this);
        img.setOnClickListener(this);
        img_setting.setOnClickListener(this);
        iv_search.setOnClickListener(this);
    }

    /**
     * 给搜索框失去焦点时候做的操作
     */
    public void OnfocusChangListener() {
        iv_search.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                if (!hasFocus) {
                    return;
                }
                //进入搜索页面
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.inanimation, R.anim.outanimation);
            }
        });
    }

    /**
     * 当标题被选中的是时候切换不同的页面
     */
    @Override
    public void onClick(View view) {
        //initColor();
        switch (view.getId()) {
            case R.id.tv_home:
                mpPager.setCurrentItem(0);
                break;
            case R.id.tv_online:
                mpPager.setCurrentItem(1);
                break;
            case R.id.tv_stand:
                mpPager.setCurrentItem(2);
                break;
            case R.id.tv_gift:
                mpPager.setCurrentItem(3);
                break;
            case R.id.tv_information:
                mpPager.setCurrentItem(4);
                break;
            case R.id.iv_logo: //点击了login进去个人中心去
                //这里需要判断他是不是是不是第一次登录的，如果是进去登录界面，不是的话去到个人中心去
                Intent intent = new Intent();
                if (SharePrefUtil.getBoolean(getApplicationContext(), SharePrefUtil.KEY.FIRST_LOGIN, true)) {
                    intent.setClass(this, LoginActivity.class);
                } else {
                    intent.setClass(this, UerContentActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.act_in, R.anim.act_out);
                break;
            case R.id.iv_setting: //点击了login进去下载页面
                Intent intents = new Intent(this, DownloadActivity.class);
                startActivity(intents);
                overridePendingTransition(R.anim.act_in, R.anim.act_out);
                break;
            case R.id.iv_search:
            case R.id.et_search://点击进去搜索页
                Intent intent1 = new Intent(this, SearchActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.inanimation, R.anim.outanimation);
                break;
            default:
                break;
        }

    }

    /**
     * 当滚动的时候初始化字体的颜色
     */
    public void initColor() {
        tv_home.setTextColor(Color.parseColor("#464646"));
        tv_stand.setTextColor(Color.parseColor("#464646"));
        tv_gift.setTextColor(Color.parseColor("#464646"));
        tv_information.setTextColor(Color.parseColor("#464646"));
        tv_online.setTextColor(Color.parseColor("#464646"));
    }

    /**
     * 设置字体的样式
     */
    public void initType() {
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
        tv_home.setTypeface(typeFace);
        tv_stand.setTypeface(typeFace);
        tv_gift.setTypeface(typeFace);
        tv_information.setTypeface(typeFace);
        tv_online.setTypeface(typeFace);
    }

    /**
     * 一进去初始化时第一项首页
     */
    public void inithome() {
        tv_home.setTextColor(Color.parseColor("#56A7E0"));
        mpPager.setCurrentItem(index);

        //出事化一些东西
        String cachePath= MyApplication.apkdownload_path+"CacheTable";
        File file=new File(cachePath);
        if (!file.exists()){
            file.mkdirs();
        }
        String channel = ChannelUtil.getChannel(this,
                ChannelUtil.AGENT_FILE);
        if (!StringUtils.isEmpty(channel)) {
            SharePrefUtil.saveString(this,SharePrefUtil.KEY.AGENT,channel);
            Log.e("--------------", "onCreate: "+channel);
        }
        Log.e("--------------", "onCreate: 空的"+channel);
    }

    /**
     * 点击2次退出程序
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void gotoDown() {
        manager.unRegister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    @Override
    public void cancel(String msg) {
        manager.unRegister();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermission(Constants.WRITE_READ_EXTERNAL_CODE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        mHandler.sendEmptyMessageDelayed(100, 2300);
    }

    /**
     * 请求权限
     */
    public void requestPermission(int code, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, code);
    }

    /**
     * 权限检查
     */
    public boolean hasPermission(String... permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.WRITE_READ_EXTERNAL_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "要权限才能下载哦", Toast.LENGTH_LONG).show();
            }
        }
    }
}
