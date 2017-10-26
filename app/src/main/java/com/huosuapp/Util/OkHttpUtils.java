package com.huosuapp.Util;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.google.gson.JsonObject;
import com.huosuapp.MyApplication;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp下载工具类
 */
public class OkHttpUtils {

	// private static final String TAG = OkHttpUtils.class.getSimpleName();
	private static final int cacheSize = 20 * 1024 * 1024; // 缓存大小20 MB
	public static final int SUCCESS = 1000;
	public static final int FAILURE = -1000;
	public static final String SUCCESS_KEY = "response";
	public static final String FAILURE_KEY = "failure";
	public static final int TIME_OUT = 5; // 请求超时 5 秒
	public static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");
	private static Cache cache; // 缓存
	private static OkHttpClient mOkHttpClient;
	private static final String TAG = "========";
	private static final String TAGS = "时间戳";

	public static void init() {
		//缓存文件夹
		File cacheFile = new File(MyApplication.getContext().getExternalCacheDir().toString(),"cache");
		cache = new Cache(cacheFile, cacheSize);
		Logger.msg(TAG, "init: "+MyApplication.getContext().getExternalCacheDir().toString());
		mOkHttpClient = new OkHttpClient.Builder()
				.addNetworkInterceptor(FORCE_CACHE_NETWORK_DATA_INTERCEPTOR)
				.connectTimeout(TIME_OUT, TimeUnit.SECONDS).cache(cache).build();
	}


	private  static final Interceptor FORCE_CACHE_NETWORK_DATA_INTERCEPTOR = new Interceptor() {
		@Override
		public Response intercept(Interceptor.Chain chain) throws IOException {

			Request request = chain.request();
			if(!isNetWorkConneted(MyApplication.getContext())){
				request = request.newBuilder()
						.cacheControl(CacheControl.FORCE_CACHE)
						.build();
//				Log.d(TAG,"no network");
			}else{
				request = request.newBuilder()
						.cacheControl(CacheControl.FORCE_NETWORK)
						.build();
			}
			Response response = chain.proceed(request);

			int maxAge = 604800;//缓存一个星期
			return response.newBuilder()
					.removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
					.removeHeader("Cache-Control")
					.header("Cache-Control", "public, max-age=" + maxAge)
					.build();
		}
	};




	/**
	 * get下载返回String
	 *
	 * @param url
	 *            传入的url
	 * @param shouldCache
	 *            是否需要缓存
	 */
	public static void getString(String url, boolean shouldCache,
			Callback callBack) {
		getdata(url, shouldCache, callBack);
	}


	/**
	 * get下载返回JsonObject
	 *
	 * @param url
	 *            传入的url
	 * @param shouldCache
	 *            是否需要缓存
	 */
	public static void getJson(String url, boolean shouldCache,
			Callback callBack) {
		getdata(url, shouldCache, callBack);
	}

	/**
	 * post下载返回String
	 *
	 * @param url
	 *            传入的url
	 * @param shouldCache
	 *            是否需要缓存
	 */
	public static void postString(String url, boolean shouldCache,
			Map<String, String> params, Callback callBack) {
		postFormEncodingdata(url, shouldCache, params, callBack);
	}

	/**
	 * post下载返回JsonObject
	 *
	 * @param url
	 *            传入的url
	 * @param shouldCache
	 *            是否需要缓存
	 */
	public static void postJson(String url, boolean shouldCache,
			Map<String, String> params, final Callback callback) {
		postFormEncodingdata(url, shouldCache, params, callback);
	}

	public static void getdata(String url, boolean shouldCache,
			Callback callback) {
		Request.Builder builder = new Request.Builder();
		if(shouldCache){
			//有网的时候缓存，没有的时候就不缓存
			if (!isNetWorkConneted(MyApplication.getContext())) {
				Logger.msg("","缓存");
				builder.cacheControl(CacheControl.FORCE_CACHE);
			}else{
				Logger.msg("","不缓存");
				builder.cacheControl(CacheControl.FORCE_NETWORK);
			}
		}

		Request request = builder.header("timestamp", Gettimestamp() + "")
				.addHeader("hs-token", gethstoken()).url(addBaseParams(url))
				.build();
		makeRequest(request, callback);
	}

	/**
	 * 给get 加基本的信息
	 * */
	public static String addBaseParams(String url) {
		if (!StringUtils.isEmpty(Constants.clientid)) {
			if (!url.contains("?")) {
				url += "?clientid=" + Constants.clientid;
			} else {
				url += "&clientid=" + Constants.clientid;
			}
		}
		if (!StringUtils.isEmpty(Constants.appid)) {

			if (!url.contains("?")) {
				url += "?appid=" + Constants.appid;
			} else {
				url += "&appid=" + Constants.appid;
			}

		}
		if (!StringUtils.isEmpty(Constants.agent) || true) {
			if (!url.contains("?")) {
				url += "?agent=" + Constants.agent;
			} else {
				url += "&agent=" + Constants.agent;
			}

		}

		if (!StringUtils.isEmpty(Constants.from)) {
			if (!url.contains("?")) {
				url += "?from=" + Constants.from;
			} else {
				url += "&from=" + Constants.from;
			}
		}
		return url;
	}



	public static void postFormEncodingdata(String url, boolean shouldCache,
			Map<String, String> params, Callback callBack) {
		FormBody.Builder fb = new FormBody.Builder();
		Logger.msg("url",url);
		if (params != null) {
			params.put(Constants.AGENT, Constants.agent);
			params.put(Constants.FROM, Constants.from);
			params.put(Constants.APP_ID, Constants.appid);
			params.put(Constants.CLIENT_ID, Constants.clientid);
		}
		for (Map.Entry e : params.entrySet()) {
			fb.add((String) e.getKey(), (String) e.getValue());
//			Log.e(TAG,"key= "+(String) e.getKey()+ "     value= "+(String) e.getValue());
		}
		RequestBody formBody = fb.build();
		Request.Builder builder = new Request.Builder();
		if (!shouldCache) {
//			builder.cacheControl(new CacheControl.Builder().maxAge(0,
//					TimeUnit.SECONDS).build());
		}
		Request request = builder.header("timestamp", Gettimestamp() + "")
				.addHeader("hs-token", gethstoken()).url(url).post(formBody).build();

		Logger.msg(TAGS, Gettimestamp() + "");
		makeRequest(request, callBack);
	}

	/**
	 * 执行网络请求
	 */
	private static void makeRequest(Request request, Callback callBack) {
		Logger.msg("执行网络请求","1");
		if (mOkHttpClient == null) {
			Logger.msg("执行网络请求","2");
			OkHttpUtils.init();
		}

		final long curTime = System.currentTimeMillis();
//		mOkHttpClient.
		mOkHttpClient.newCall(request).enqueue(callBack);
	}


	/**
	 * 获取okhttp缓存大小
	 */
//	public static long getCacheSize() {
//		long size = 0;
//		try {
//			size = cache.getSize();
//		} catch (IOException e) {
//			// e.printStackTrace();
//		}
//		return size;
//	}

	/**
	 * 清除okhttp缓存
	 */
//	public static void clearCache() {
//		if (cache != null) {
//			try {
//				cache.evictAll();
//			} catch (IOException e) {
//				// e.printStackTrace();
//			}
//		}
//	}

	/**
	 * 把Map集合转化成json字符串
	 */
	public static String mapToJson(Map<String, String> map) {
		String string = "{";
		for (Map.Entry e : map.entrySet()) {
			string += "'" + e.getKey() + "':";
			string += "'" + e.getValue() + "',";
		}
		string = string.substring(0, string.lastIndexOf(","));
		string += "}";
		return string;
	}

	/**
	 * 请求有时间头的
	 */
	public static void getStringdata(String url, boolean shouldCache,
			Map<String, String> params, final Callback callback) {
		RequestBody body = RequestBody.create(JSON, mapToJson(params));
		Request.Builder builder = new Request.Builder();
		if (!shouldCache) {
			builder.cacheControl(new CacheControl.Builder().maxAge(0,
					TimeUnit.SECONDS).build());
		}
		Request request = builder
				.addHeader(
						"timestamp",
						(getcurrent() + SharePrefUtil.getInt(
								MyApplication.getContext(),
								SharePrefUtil.KEY.TIMESTAMP, 0)) + "")
				.addHeader("hs-token", "").url(addBaseParams(url)).post(body).build();
		makeRequest(request, callback);
	}

	/**
	 * 获取当前时间戳，从1970.1.1到现在的秒数
	 */
	public static long getcurrent() {
		long i = System.currentTimeMillis() / 1000;
		return i;
	}

	/**
	 * post下载返回时间搓
	 *
	 * @param url
	 *            传入的url
	 * @param shouldCache
	 *            是否需要缓存
	 */
//	public static void postString1(String url, boolean shouldCache,
//			Map<String, String> params, Callback callBack) {
//		postFormEncodingdata1(url, shouldCache, params, callBack);
//	}
//
//	public static void postFormEncodingdata1(String url, boolean shouldCache,
//			Map<String, String> params, Callback callBack) {
//		FormEncodingBuilder fb = new FormEncodingBuilder();
////		 if(params!=null){
////		 params.put(Constants.AGENT, Constants.agent);
////		 params.put(Constants.FROM, Constants.from);
////		 params.put(Constants.APP_ID, Constants.appid);
////		 params.put(Constants.CLIENT_ID, Constants.clientid);
////		 }
//		for (Map.Entry e : params.entrySet()) {
//			fb.add((String) e.getKey(), (String) e.getValue());
//		}
//		RequestBody formBody = fb.build();
//		Request.Builder builder = new Request.Builder();
//		if (!shouldCache) {
//			builder.cacheControl(new CacheControl.Builder().maxAge(0,
//					TimeUnit.SECONDS).build());
//		}
//		Request request = builder.url(url).post(formBody).build();
//		makeRequest(request, callBack);
//	}

	/**
	 * 获取时间戳
	 */
	private static String ABB = "当前时间1";
	private static String ABBC = "保存的时间2";

	public static long Gettimestamp() {
		Logger.msg(ABB, getcurrent() + MyApplication.time + "");
		return getcurrent() + MyApplication.time;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static String gethstoken() {
		String username = SharePrefUtil.getString(Global.getContext(),
				SharePrefUtil.KEY.IDENTIFIER, "");
		Logger.msg("username", username);

		String password = SharePrefUtil.getString(Global.getContext(),
				SharePrefUtil.KEY.ACCESSTOKEN, "");
		Logger.msg("password", username);

		String usernameEcode = AuthCodeUtil.authcodeEncode(username,
				Constants.appkey);
		Logger.msg("usernameEcode", usernameEcode);

		String passwordEcode = AuthCodeUtil.authcodeEncode(password,
				Constants.appkey);
		Logger.msg("passwordEcode", passwordEcode);

		// JsonArray array = new JsonArray();
		JsonObject jsonObject = new JsonObject();
		String dest = "";
		if (usernameEcode != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(usernameEcode);
			usernameEcode = m.replaceAll("");
		}
		if (passwordEcode != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(passwordEcode);
			passwordEcode = m.replaceAll("");
		}
		jsonObject.addProperty("identify", usernameEcode);
		jsonObject.addProperty("accesstoken", passwordEcode);
		String jsonEncode = jsonObject.toString();
		String aaa = AuthCodeUtil.authcodeEncode(jsonEncode, Constants.appkey);
		String str1 = aaa.replaceAll("\n", "");
		return str1;
	}

	/**
	 * 负责请求礼包码的方法
	 */
//	public static void postGiftCode(String url, boolean shouldCache,
//			Map<String, String> params, Callback callBack) {
//		postFormGiftCodedata(url, shouldCache, params, callBack);
//	}

	public static void postFormGiftCodedata(String url, boolean shouldCache,
			Map<String, String> params, Callback callBack) {
		FormBody.Builder fb = new FormBody.Builder();
		for (Map.Entry e : params.entrySet()) {
			fb.add((String) e.getKey(), (String) e.getValue());
		}
		RequestBody formBody = fb.build();
		Request.Builder builder = new Request.Builder();
		if (!shouldCache) {
			builder.cacheControl(new CacheControl.Builder().maxAge(0,
					TimeUnit.SECONDS).build());
		}
		Request request = builder.header("timestamp", Gettimestamp() + "")
				.addHeader("hs-token", gethstoken()).url(addBaseParams(url)).post(formBody)
				.build();
		Logger.msg(TAGS, Gettimestamp() + "");
		makeRequest(request, callBack);
	}

	/**
	 * 负责请求用户礼包列表
	 */
//	public static void getUserCode(String url, boolean shouldCache,
//			Callback callBack) {
//		getUserdata(url, shouldCache, callBack);
//	}
//
//	public static void getUserdata(String url, boolean shouldCache,
//			Callback callback) {
//		Request.Builder builder = new Request.Builder();
//		if (!shouldCache) {
//			builder.cacheControl(new CacheControl.Builder().maxAge(0,
//					TimeUnit.SECONDS).build());
//		}
//		Request request = builder.header("timestamp", Gettimestamp() + "")
//				.addHeader("hs-token", gethstoken()).url(addBaseParams(url)).build();
//
//		makeRequest(request, callback);
//	}
	/**
	 * 网络是否是连接状态
	 * @return true表示可能，false网络不可用
	 */
	public static boolean isNetWorkConneted(Context ctx) {
		ConnectivityManager cmr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = cmr.getActiveNetworkInfo();
		return null != networkinfo && networkinfo.isConnectedOrConnecting();
	}
}
