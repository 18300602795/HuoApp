package com.huosuapp.Util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.GlideModule;
import com.huosuapp.MyApplication;

import java.io.File;

/**
 * Created by Administrator on 2017/6/23.
 */

public class TableGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        String cachePath=MyApplication.apkdownload_path+"CacheTable";
        File file=new File(cachePath);
        final DiskCache diskCache = DiskLruCacheWrapper.get(file, 100*1024*1024);
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                return diskCache;
            }
        });
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
