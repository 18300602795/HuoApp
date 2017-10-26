package com.huosuapp;

import android.app.Activity;
import android.content.Context;

import com.huosuapp.Util.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @类名称：AppManager
 * @类描述： 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @创建人：liuhongliang
 */
public class AppManager {
	private final static String TAG=AppManager.class.getSimpleName();
	private static List<Activity> activityStack = new LinkedList<Activity>();
	private static AppManager instance = new AppManager();

	private AppManager() {
	}

	public static List<Activity> getActivityStack() {
		return activityStack;
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = null;
		try {
			Logger.msg("","当前Activity总数量=" + activityStack.size());
			if (activityStack.size() > 0) {
				activity = activityStack.get(activityStack.size() - 1);
				Logger.msg("","当前的acitivity=" + activity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		if (activityStack.size() > 0) {
			Activity activity = activityStack.get(activityStack.size() - 1);
			if (activity != null) {
				removeActivity(activity);
				activity.finish();
			}
		}
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			removeActivity(activity);
			activity.finish();
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (int i=activityStack.size()-1;i>=0;i--) {
			Activity activity = activityStack.get(i);
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i=activityStack.size()-1;i>=0;i--) {
			Activity activity = activityStack.get(i);
			finishActivity(activity);
		}
	}

	/**
	 * @Title: finishOtherActivity
	 * @说 明:结束除传如的Activity外其他的Activity
	 * @参 数: @param mActivity 不结束的activity
	 */
	public void finishOtherActivity(Activity mActivity) {
		for (int i=activityStack.size()-1;i>=0;i--) {
			if (!activityStack.get(i).getClass().equals(mActivity.getClass())) {
				 finishActivity(activityStack.get(i));
			}
		}
	}
	/**
	 * 结束除指定类名的Activity
	 */
	public void finishOtherActivity(Class<?> cls) {
		for (int i=activityStack.size()-1;i>=0;i--) {
			Activity activity = activityStack.get(i);
			if (!activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}
	/**
	 * 从列表中移除插件activity，并没有finish
	 * @param pluginActivity
	 */
	public void removeActivity(Activity pluginActivity){
		boolean isremove = activityStack.remove(pluginActivity);
		Logger.msg(TAG, "removeActivity:" + pluginActivity + " --> " + isremove);
	}

	/**
	 * 关闭这个activity上面的所有activity,并返回这个activity
	 * @param activityName
	 * @return
     */
	public Activity finishThisUpActivity(String activityName){
		for (int i=activityStack.size()-1;i>=0;i--) {
			Activity activity = activityStack.get(i);
			if (!activity.getClass().getName().equals(activityName)) {
				finishActivity(activity);
			}else{
				return activity;
			}
		}
		return null;
	}
	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		//友盟退出统计
//		MobclickAgent.onKillProcess(context);
		try {
			finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);

		} catch (Exception e) {
            e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "AppManager [activityStack.size()="+activityStack.size();
	}

}