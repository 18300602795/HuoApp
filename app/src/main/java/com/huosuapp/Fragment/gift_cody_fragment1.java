package com.huosuapp.Fragment;

import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.huosuapp.Adapter.gift_Usercode_Listadapter;
import com.huosuapp.Bean.UserGiftCodeBean;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.text.R;
import com.huosuapp.view.LoadMoreListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;
import java.util.Map;

import static com.huosuapp.Util.OkHttpUtils.Gettimestamp;
import static com.huosuapp.Util.OkHttpUtils.gethstoken;

public class gift_cody_fragment1 extends Basefragment {
	private gift_Usercode_Listadapter mgiGiftListadapter;
	private LoadMoreListView ls;

	@Override
	public void parUI(Message mes) {
		switch (mes.what){
			case 1:
				if (userGiftCodeBean!=null){
					mgiGiftListadapter.getDataList().addAll(userGiftCodeBean.getData().getGift_list());
					mgiGiftListadapter.notifyDataSetChanged();
					stateLayout.showContentView();
				}
				break;
		}
	}



	@Override
	public View getContentView() {
		
		return null;
	}

	@Override
	public int getContentViewLayoutId() {
		
		return R.layout.gift_code_fragment1;
	}

	@Override
	public void initView() {

		stateLayout.showContentView();
		mgiGiftListadapter= new gift_Usercode_Listadapter(null, context);
		ls=findView(R.id.lv_gift_code);
		ls.setAdapter(mgiGiftListadapter);
		
	}

	@Override
	public void initListener() {
		
		
	}

	@Override
	public void initData() {
		getData();
		
	}

	private void getData() {
		Map<String, String> map = new HashMap<String, String>();
	/*	map.put(Constants.CLIENT_ID, 12 + "");
		map.put(Constants.APP_ID, 100 + "");
		map.put(Constants.FROM,3+"");
		map.put("agent","");*/
		String url = StringUtils.getUrlContainAppid(Constants.GIFY_LIST,map);
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addHeader("hs-token", gethstoken());
		params.addHeader("timestamp", Gettimestamp() + "");
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

	private UserGiftCodeBean userGiftCodeBean;
	private void parjson(String res) {
		userGiftCodeBean= JsonUtil.getJsonUtil().json2Bean(res,UserGiftCodeBean.class);
		if (userGiftCodeBean==null){
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getContext(),"联网错误",Toast.LENGTH_SHORT).show();
				}
			});
			return;
		}
		if(userGiftCodeBean.getData()==null){
			handler.post(new Runnable() {
				@Override
				public void run() {
					String msg = userGiftCodeBean.getMsg();
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
		return "已领取礼包";
	}


	
}
