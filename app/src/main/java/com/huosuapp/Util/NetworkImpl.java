package com.huosuapp.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * author janecer
 * 2014年4月10日下午3:29:24
 */
public class NetworkImpl {
   
	
	/*public static HttpClient getHttpClient(Context ctx){
		
		if(!isNetWorkConneted(ctx)){
			
			return null;
		}
		HttpClient client=null;
		if(isCmwapType(ctx)){
			HttpParams params=new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params,30*1000);//设置链接超时时间
			HttpConnectionParams.setSoTimeout(params, 30*1000);//设置请求超时时间
			HttpConnectionParams.setSocketBufferSize(params,100*1024);
			HttpClientParams.setRedirecting(params, true);
			HttpHost httphost=new HttpHost("10.0.0.172",80);
			params.setParameter(ConnRouteParams.DEFAULT_PROXY, httphost);//设置代理
			client=new DefaultHttpClient(params);
		}else{
			client=new DefaultHttpClient();
			HttpParams params=client.getParams();
			params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,30*1000);
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT,30*1000);
		}
		return client;
	}*/
	
	/**
	 * 网络是否是连接状态
	 * @return true表示可能，false网络不可用
	 */
	public static boolean isNetWorkConneted(Context ctx){
		ConnectivityManager cmr= (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo= cmr.getActiveNetworkInfo();
        return null!=networkinfo&&networkinfo.isConnectedOrConnecting();
	}
	
	/**
	 * 是否使用代理上网
	 * @param ctx
	 * @return
	 */
	private static boolean isCmwapType(Context ctx){
		ConnectivityManager cmr= (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo= cmr.getActiveNetworkInfo();
		String nettype=netinfo.getExtraInfo();
		if(null==nettype){
			return false;
		}
		return "cmwap".equalsIgnoreCase(nettype)||"3gwap".equalsIgnoreCase(nettype)||"uniwap".equalsIgnoreCase(nettype);
	}
}
