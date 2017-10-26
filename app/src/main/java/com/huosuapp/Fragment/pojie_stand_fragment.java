package com.huosuapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.huosuapp.Adapter.Pojiedanji_adapter;
import com.huosuapp.Bean.BreakStandBean;
import com.huosuapp.Ui.GameDetailActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 分类中的单机界面的fragment
 */
public class pojie_stand_fragment extends Basefragment {
	private ArrayList<String> list = new ArrayList<>();
	private XListView ls;
	private Pojiedanji_adapter pojiedanji_adapter;
	private int currentPage=1;
	@Override
	public void parUI(Message mes) {
		if (mes.what==3){
			BreakStandBean.DataBean dataBean= (BreakStandBean.DataBean) mes.obj;
			if (dataBean==null){
				stateLayout.showFailView();
			}
			stateLayout.showContentView();
			pojiedanji_adapter.getDataList().addAll(dataBean.getGame_list());
			pojiedanji_adapter.notifyDataSetChanged();
		}
	}


	@Override
	public View getContentView() {
		
		return null;
	}

	@Override
	public int getContentViewLayoutId() {
		
		return R.layout.pojie_stand_fragment;
	}

	@Override
	public void initView() {
		ls=findView(R.id.lv_pojie_stand);
		if (pojiedanji_adapter==null){
			pojiedanji_adapter=new Pojiedanji_adapter(null,context);
			ls.setAdapter(pojiedanji_adapter);
			ls.mFooterView.setState(XFooterView.STATE_LOADING);
		}
		stateLayout.showContentView();

	}

	@Override
	public void initListener() {
		ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				BreakStandBean.DataBean.GameListBean databean= (BreakStandBean.DataBean.GameListBean) adapterView.getItemAtPosition(position);
				if(databean!=null){
					enterIntoGameDetails(databean.getGameid());
				}
			}
		});


		/**
		 * 加载更多监听
		 */
		ls.setXListViewListener(new XListView.IXListViewListener() {
			@Override
			public void onRefresh() {
				getPoJieStand(1);
			}

			@Override
			public void onLoadMore() {
				getPoJieStand(currentPage+1);
			}
		});
		
	}

	/**
	 * 进入首页列表的详情
	 * @param gameID
	 */
	public void enterIntoGameDetails(int gameID){
		Intent intent3 = new Intent(context, GameDetailActivity.class);
		intent3.putExtra("gameID",gameID);
		context.startActivity(intent3);
		((Activity) context).overridePendingTransition(R.anim.act_in, R.anim.act_out);
	}

	@Override
	public void initData() {
		getPoJieStand(1);
		
	}

	private String ATT="长度是多少";
	private void getPoJieStand(final int type) {
		Map<String, String> map = new HashMap<String, String>();
			map.put("category",1+"");
			map.put("classify", 1 + "");
			map.put("page",type+"");
			map.put("offset", 15 + "");
		String url= StringUtils.getUrlContainAppid(Constants.URL_GAME_LIST,map);
		Logger.msg("pojie_stand_fragment","url="+url);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET,
				url,
				new RequestCallBack<String>(){
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						parjson3(responseInfo.result, type);
						Logger.msg("pojie_stand_fragment","res="+responseInfo.result);
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						stateLayout.showContentView();
						ls.stopRefresh();
						ls.stopLoadMore();
					}
				});
//		OkHttpUtils.getString( url, false, new Callback() {
//			@Override
//			public void onFailure(Request request, IOException e) {
//				//Logger.msg(ATT, "访问失败");
//			}
//			@Override
//			public void onResponse(Response response) throws IOException {
//				String res = response.body().string().trim();
//				Logger.msg(ATT, res.length()+"");
//				Logger.msg("pojie_stand_fragment","res="+res);
//				parjson3(res,type);
//			}
//		});


	}

	private void parjson3(String res, int type) {
		ls.stopRefresh();
		ls.stopLoadMore();
		if(type==1){
			ls.setRefreshTime(Utils.getTime());
		}
		stateLayout.showContentView();
		//homeBeanList = JsonUtil.json2Bean(res, BreakOnlineBean.class);
		BreakStandBean homeBeanList=JsonUtil.getJsonUtil().json2Bean(res,BreakStandBean.class);
		if (homeBeanList!=null) {
			BreakStandBean.DataBean databean = homeBeanList.getData();
			if(databean!=null&&databean.getGame_list()!=null){
				if(type==1){
					pojiedanji_adapter.getDataList().clear();
				}
				pojiedanji_adapter.getDataList().addAll(databean.getGame_list());
				pojiedanji_adapter.notifyDataSetChanged();
				currentPage=type;
				if (databean.getGame_list().size() < 15){
					Logger.msg("","没有更多数据");
					ls.mFooterView.setState(XFooterView.STATE_NORMAL);
					ls.mFooterView.mHintView.setText("没有更多数据");
				}
			}else {
				Logger.msg("","没有更多数据");
				ls.mFooterView.mHintView.setText("没有更多数据");
			}
			if(pojiedanji_adapter.getDataList().isEmpty()){
				stateLayout.showEmptyView();
			}
		}
	}

	public CharSequence getTitle() {
		return "单机";
	}

}
