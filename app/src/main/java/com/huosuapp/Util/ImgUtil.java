package com.huosuapp.Util;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.huosuapp.MyApplication;
import com.huosuapp.text.R;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by admin on 2016/8/17.
 */
public class ImgUtil {

    private ImgUtil() {

    }

    private static BitmapUtils bitmapUtils;

    /**
     * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
     *
     * @param appContext application context
     * @return
     */
    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new GlideBitmapUtil(appContext);
        }
        return bitmapUtils;
    }

    private static RequestManager mManager;

    public static void loadImage(String url, ImageView imageView) {
        if (mManager == null) {
            mManager = Glide.with(MyApplication.getContext());
        }
        mManager.load(url).placeholder(R.drawable.ic_launcher).into(imageView);
    }

    public static void loadImage(Context context, String url, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).into(iv);
    }
    public static void loadImage( String url, int emptyImg, ImageView iv,Fragment context) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).into(iv);
    }
}