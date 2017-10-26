package com.huosuapp.Fragment;

import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.huosuapp.Adapter.UserKeyCodeListadapter;
import com.huosuapp.Bean.KeyCodeBean;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.text.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.huosuapp.Util.OkHttpUtils.Gettimestamp;
import static com.huosuapp.Util.OkHttpUtils.gethstoken;

public class key_cody_fragment1 extends Basefragment {
	private ListView lv_key_cody;
	private ArrayList<String> list  = new ArrayList<String>();
	private UserKeyCodeListadapter mGift_key_Listadapter;
	private KeyCodeBean keyCodeBean;

	@Override
	public void parUI(Message mes) {
		switch (mes.what) {
			case 1:
				if (keyCodeBean == null) {
					return;
				}
				stateLayout.showContentView();
				mGift_key_Listadapter.getDataList().addAll(keyCodeBean.getData().getGift_list());
				mGift_key_Listadapter.notifyDataSetChanged();
				lv_key_cody.setAdapter(mGift_key_Listadapter);
				break;
		}
	}
	@Override
	public View getContentView() {
		
		return null;
	}

	@Override
	public int getContentViewLayoutId() {
		
		return R.layout.key_cpde_fragment1;
	}

	@Override
	public void initView() {
		stateLayout.showContentView();
		mGift_key_Listadapter= new UserKeyCodeListadapter(null, context,this);
		lv_key_cody=findView(R.id.lv_key_cody_);
		lv_key_cody.setAdapter(mGift_key_Listadapter);
		
	}

	@Override
	public void initListener() {
		
		
	}

	@Override
	public void initData() {
		getData();
		
	}
	public void getData() {
		Map<String, String> map = new HashMap<String, String>();
	/*	map.put(Constants.CLIENT_ID, 12 + "");
		map.put(Constants.APP_ID, 100 + "");
		map.put(Constants.FROM,3+"");
		map.put("agent","");*/
		String url = StringUtils.getUrlContainAppid(Constants.USER_CDKEY_LIST,map);
		Logger.msg("key_code_fragmentUrl1",url);
		RequestParams params = new RequestParams();
		params.addHeader("hs-token", gethstoken());
		params.addHeader("timestamp", Gettimestamp() + "");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET,
				url,params,
				new RequestCallBack<String>(){
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						parjson(responseInfo.result);
						Logger.msg("用户礼包礼包的数据===",responseInfo.result);
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						stateLayout.showContentView();
					}
				});
//		OkHttpUtils.getString(url, false, new Callback() {
//			@Override
//			public void onFailure(Request request, IOException e) {
//
//			}
//
//			@Override
//			public void onResponse(Response response) throws IOException {
//				String res= response.body().string().trim();
//				Logger.msg("用户礼包礼包的数据===",res);
//				parjson(res);
//			}
//		});
	}

	private void parjson(String res) {
		keyCodeBean= JsonUtil.getJsonUtil().json2Bean(res,KeyCodeBean.class);
		if (keyCodeBean==null){
			return;
		}
		if (keyCodeBean==null){
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getContext(),"联网错误",Toast.LENGTH_SHORT).show();
					stateLayout.showEmptyView();
				}
			});
			return;
		}
		if(keyCodeBean.getData()==null){
			handler.post(new Runnable() {
				@Override
				public void run() {
						String msg = keyCodeBean.getMsg();
						Toast.makeText(getContext(),msg==null?"联网错误":msg,Toast.LENGTH_SHORT).show();
						stateLayout.showEmptyView();
					}
			});
			return;
		}
		Message mes = Message.obtain();
		mes.what=1;
		handler.sendMessage(mes);
	}
	public CharSequence getTitle() {
		return "激活码";
	}


}
