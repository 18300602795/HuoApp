package com.huosuapp.Fragment;

import android.os.Message;
import android.view.View;

import com.huosuapp.Adapter.KeyCodeListadapter;
import com.huosuapp.Bean.HiddenJiHuoCode;
import com.huosuapp.Bean.KeyCodeBean;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.Util.Utils;
import com.huosuapp.text.R;
import com.huosuapp.widget.XFooterView;
import com.huosuapp.widget.XListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class key_cody_fragment extends Basefragment {
	private XListView lv_key_cody;
	private ArrayList<String> list  = new ArrayList<String>();
	private KeyCodeListadapter mGift_key_Listadapter;
	private KeyCodeBean keyCodeBean;
	/** 礼包列表刷新的 */
	protected static final int UPDATE_CODE = 3;
	private int currentPage=1;

	@Override
	public void parUI(Message mes) {
		switch (mes.what) {
			case 1:
				if (keyCodeBean==null){
					return;
				}
				stateLayout.showContentView();
				mGift_key_Listadapter.getDataList().addAll(keyCodeBean.getData().getGift_list());
				mGift_key_Listadapter.notifyDataSetChanged();
				lv_key_cody.setAdapter(mGift_key_Listadapter);
				break;
			case 2:
				if (keyCodeBean==null){
					stateLayout.showFailView();
				}
				if(keyCodeBean==null||keyCodeBean.getData()==null||keyCodeBean.getData().getGift_list()==null){
					mGift_key_Listadapter.getDataList().clear();
				}else{
					mGift_key_Listadapter.setDataList(keyCodeBean.getData().getGift_list());
				}
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
		
		return R.layout.key_cpde_fragment;
	}

	@Override
	public void initView() {
		lv_key_cody=findView(R.id.lv_key_cody_);
		mGift_key_Listadapter=new KeyCodeListadapter(null, context,this);
		lv_key_cody.setAdapter(mGift_key_Listadapter);
		lv_key_cody.mFooterView.setState(XFooterView.STATE_LOADING);
		stateLayout.showEmptyView();
		lv_key_cody.setXListViewListener(new XListView.IXListViewListener() {
			@Override
			public void onRefresh() {
				getCodData(1);
			}

			@Override
			public void onLoadMore() {
				getCodData(currentPage+1);
			}
		});
		
	}

	@Override
	public void initListener() {
		
		
	}

	@Override
	public void initData() {
		getCodData(1);
		
	}
	public void getCodData(final int type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", type + "");
		map.put("offset", 15 + "");
		String url = StringUtils.getUrlContainAppid(Constants.CDKEY_LIST, map);
		Logger.msg("key_code_fragmentUrl",url);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET,
				url,
				new RequestCallBack<String>(){
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						parjson(responseInfo.result, type);
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						stateLayout.showContentView();
						lv_key_cody.stopLoadMore();
						lv_key_cody.stopRefresh();
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
//				String res = response.body().string().trim();
//				Logger.msg("key_code_fragment",res);
//				parjson(res, type);
//			}
//		});
	}


	private void parjson(String res, int type) {
		lv_key_cody.stopRefresh();
		lv_key_cody.stopLoadMore();
		if(type==1){
			lv_key_cody.setRefreshTime(Utils.getTime());
		}
		stateLayout.showContentView();
		//homeBeanList = JsonUtil.json2Bean(res, BreakOnlineBean.class);
		keyCodeBean = JsonUtil.getJsonUtil().json2Bean(res, KeyCodeBean.class);
		//如果是飞磨游戏，需要在没有激活码时隐藏激活码
		if(type==1&&getActivity().getPackageName().equals("com.feimo.huosuapp")){
			if(keyCodeBean==null||keyCodeBean.getData()==null||keyCodeBean.getData().getGift_list()==null){
				EventBus.getDefault().postSticky(new HiddenJiHuoCode());
			}
		}
		if (keyCodeBean!=null) {
			KeyCodeBean.DataBean databean = keyCodeBean.getData();
			if(databean!=null&&databean.getGift_list()!=null){
				if(type==1){
					mGift_key_Listadapter.getDataList().clear();
				}
				mGift_key_Listadapter.getDataList().addAll(databean.getGift_list());
				mGift_key_Listadapter.notifyDataSetChanged();
				currentPage=type;
			}
			if(mGift_key_Listadapter.getDataList().isEmpty()){
				stateLayout.showEmptyView();
			}
		}
	}
	public CharSequence getTitle() {
		return "激活码";
	}



}
