package com.huosuapp.Util;

import android.nfc.Tag;
import android.util.Log;

/*
 *日志帮助类 
 */
public class Logger {
    //数字在0~4将根据数字的不同打印出不同级别的日志，在0~4之外将处于运行上线状态 控制台上不再打印日志
	private static int level=2;
	private static final  String TAG = "333";
//	public static void msg(String tag,String msg){
//		switch (level) {
//		case 0:
//			Log.v(tag, msg);
//			break;
//		case 1:
//			Log.d(tag, msg);
//			break;
//		case 2:
//			Log.i(tag, msg);
//			break;
//		case 3:
//			Log.w(tag, msg);
//			break;
//		case 4:
//			Log.e(tag, msg);
//			break;
//		default:
//				break;
//		}
//	}

	public static void msg(String tag,String msg){
		switch (level) {
			case 0:
				Log.v(TAG, tag + "：" + msg);
				break;
			case 1:
				Log.d(TAG, tag + "：" + msg);
				break;
			case 2:
				Log.i(TAG, tag + "：" + msg);
				break;
			case 3:
				Log.w(TAG, tag + "：" + msg);
				break;
			case 4:
				Log.e(TAG, tag + "：" + msg);
				break;
			default:
				break;
		}
	}
}
