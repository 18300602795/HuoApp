package com.huosuapp.Util;

import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.MyApplication;

/**
 * 封装一些常用操作，如：屏幕尺寸获取，主线程运行，toast
 * 
 * @author wjq 
 */
public class Global {
	
	public static Context mContext;
	
	/** 屏幕的宽度 */
	public static int mScreenWidth;
	/** 屏幕的高度 */
	public static int mScreenHeight;
	/** 屏幕密度 */
	public static float mDensity;

	public static Drawable drawable;
	
	private static Handler mHandler = new Handler();
	
	public static void init(Context context) {
		mContext = context;
		initScreenSize();
	}

	/**
	 * 获取Application类型的上下文
	 */
	public static Context getContext() {
		return MyApplication.getContext();
	}

	/** 初始化屏幕参数 */
	private static void initScreenSize() {
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		mDensity = dm.density;
	}
	
	public static int dp2px(int dp,Context context) {
		init(context);
		return (int) (mDensity * dp + 0.5f);
	}
	
	public static Handler getHandler() {
		return mHandler;
	}
	
	/** 判断当前是否在主线程运行 */
	public static boolean isMainThread() {
		return Looper.getMainLooper() == Looper.myLooper();
	}

	/**
	 * 在主线程执行
	 * @param runnable
	 */
	public static void runInUIThread(Runnable runnable) {
		if (isMainThread()) {
			runnable.run();
		} else {
			mHandler.post(runnable);
		}
	}
	
	private static Toast mToast;
	
	/**
	 * 显示文本，可以在子线程运行
	 * 
	 * @param text
	 */
	public static void showToast(final String text) {
		if (mToast  == null) {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		}
		mToast.setText(text);
		mToast.show();
	}
	
	/**
	 * 创建一个有随机颜色选择器的TextView
	 * @return
	 */
	public static TextView createRandomColorSelectorTextView(Context context) {
		TextView textView = new TextView(context);
		textView.setTextColor(Color.parseColor("#464747"));
		int padding = dp2px(12,context);
		textView.setPadding(padding, padding, padding, padding);
		textView.setGravity(Gravity.CENTER);		
		textView.setBackgroundDrawable(createRandomColorSelector( context));
		return textView;
	}
	

	/**
	 * 创建一个随机颜色的选择器
	 * @return
	 */
	
	public static Drawable createRandomColorSelector(Context context) {
		StateListDrawable stateListDrawable = new StateListDrawable();
		
		// 创建按下状态、正在状态
		int[] presssState = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
		int[] normalState = new int[]{};
		
		// 创建按下状态、正常状态使用的Drawable
		Drawable pressDrawable = createRandomColorDrawable(context);
		Drawable normalDrawable = createRandomColorDrawable(context);
		
		
		stateListDrawable.addState(presssState, pressDrawable);
		stateListDrawable.addState(normalState, normalDrawable);
		return stateListDrawable;
	}
	
	
	/** 创建一个随机颜色的Drawable */
	public static  Drawable createRandomColorDrawable(Context context) {
		GradientDrawable gradientDrawable = new GradientDrawable();
		
		// 设置形状为矩形
		gradientDrawable.setShape(GradientDrawable.RECTANGLE);	
		
		// 设置圆角
		gradientDrawable.setCornerRadius(dp2px(5,context));
		
		// 设置图形的颜色
		gradientDrawable.setColor(Global.createRandomColor());
		
		return gradientDrawable;
	}
	
	
	
	/** 创建一个50 ~ 200范围的随机颜色 */
	public static int createRandomColor() {
		Random random = new Random();
		int red = 230 + random.nextInt(20);		// 50 ~ 200
		int green = 230 + random.nextInt(20);	// 50 ~ 200
		int blue = 230 + random.nextInt(20);	// 50 ~ 200
		int color = Color.argb(255, red, green, blue);
		return color;
	}


	private static Toast toast = null;
	/** 不会一直重复重复重复重复的提醒了 */
	public static void showToast(String msg, int length) {
		if (toast == null) {
			toast = Toast.makeText(getContext(), msg, length);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
	
}
















