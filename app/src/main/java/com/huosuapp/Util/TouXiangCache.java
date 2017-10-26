package com.huosuapp.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 2016/8/10.
 */
public class TouXiangCache {
    //  保存图片
    public static void saveMyBitmap(Bitmap mBitmap, String pic_pathload)  {
        File f = new File( "storage/sdcard0/"+pic_pathload);

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   //根据路径得到一张图片
    public static  Bitmap getphoto(String pic_pathload){
        Bitmap bitmap = BitmapFactory.decodeFile(pic_pathload);
        return bitmap;
    }
}
