package com.huosuapp.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.widget.ImageView;

/**
 *
 * 将一个bitmap转化为圆形输出
 */
public class QieYuanTu {
    public static Bitmap toRoundBitmap(Bitmap bitmap){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        int r=0;
        if (width<height){
            r=width;
        }else {
            r=height;
        }
        Bitmap backgroundBitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(backgroundBitmap);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        RectF rectF=new RectF(0,0,r,r);
        canvas.drawRoundRect(rectF,r/2,r/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,null,rectF,paint);
        return backgroundBitmap;
    }

    public static ImageView setRoundConner(ImageView image, Bitmap bitmap) {
        bitmap = toRoundBitmap(bitmap);
        image.setImageBitmap(bitmap);
        return image;
    }

}
