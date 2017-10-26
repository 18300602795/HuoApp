package com.huosuapp.Util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huosuapp.Util.Constants.agent;

public class Utils {
	public static boolean isWeixinAvilible(Context context) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}

		return false;
	}
	public static String getTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
		return sdf.format(new Date());
	}
	public static Map<String, String> getAgentAndAppid(Context context) {
		Map<String, String> map = new HashMap<>();

		PackageManager pm = context.getPackageManager();
		String appid = "";
		String clientid = "";
		String agentid = "";
		String appkey = "";

		try {
			// 先通过反射获取

			// 在从清单文件中获取
			String channel = ChannelUtil.getChannel(context,
					ChannelUtil.AGENT_FILE);
			if (StringUtils.isEmpty(channel)) {
				channel = ChannelUtil.getChannel(context,
						ChannelUtil.AGENT_FILE2);
			}

			// 在从清单文件中获取
			ApplicationInfo appinfo = pm.getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = appinfo.metaData;
			if (null != bundle) {
				appid = (bundle.getInt("HS_APPID") == 0 ? -1 : bundle
						.getInt("HS_APPID")) + "";
				appkey = bundle.getString("HS_APPKEY");

				agentid = bundle.getString("HS_AGENT");

				clientid = bundle.getInt("HS_CLIENTID") + "";

				if (null != channel && !"".equals(channel)) {
					agentid = channel;
				}
				map.put("appid", appid);
				map.put("agent", agentid);
				map.put("clientid", clientid);
				map.put("appkey", appkey);
				if(!StringUtils.isEmpty(appid)){
					Constants.appid = appid;
				}
				if(!StringUtils.isEmpty(agentid)){
					agent = agentid;
				}
				if(!StringUtils.isEmpty(clientid)){
					Constants.clientid = clientid;
				}
				if(!StringUtils.isEmpty(appkey)){
					Constants.appkey = appkey;
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Logger.msg("appid", appid);
		Logger.msg("agent", agentid);
		Logger.msg("clientid", clientid);
		Logger.msg("appkey", appkey);
		return map;
	}
}
