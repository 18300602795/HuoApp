package com.huosuapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.huosuapp.Adapter.InformationListadapter;
import com.huosuapp.Bean.InformationBean;
import com.huosuapp.Ui.informationDetailActivity;
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
import java.util.List;
import java.util.Map;

/**
 * 资讯界面的fragment
 */
public class informationfragment extends Basefragment {
	private XListView listview;
	InformationListadapter textInformationListadapter;
	private int currentPage=1;
	private List<InformationBean.DataBean.NewsListBean> datas=new ArrayList<>();
	private ProgressBar mProgressBar;
	@Override
	public void parUI(Message mes) {

	}
	@Override
	public View getContentView() {
		return null;
	}
	@Override
	public int getContentViewLayoutId() {
		return R.layout.informationfragment;
	}
	@Override
	public void initView() {
		listview=findView(R.id.lv_information);
		mProgressBar = findView(R.id.information_progress);
		View head = View.inflate(context,R.layout.information_listview_head,null);
		listview.addHeaderView(head);
		stateLayout.showContentView();
		listview.setAutoLoadEnable(true);
		textInformationListadapter=new InformationListadapter(datas,this);
		listview.setAdapter(textInformationListadapter);
		listview.mFooterView.setState(XFooterView.STATE_LOADING);
	}

	@Override
	protected void loadData() {
		//加载过则跳出
		if (mHasLoadedOnce||!isVisible){
			return;
		}
		getData(1);
		mHasLoadedOnce=true;
	}


	@Override
	public void initListener() {
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				InformationBean.DataBean.NewsListBean newsListBean	= (InformationBean.DataBean.NewsListBean) adapterView.getItemAtPosition(position);
				if (newsListBean!=null) {
					int id = newsListBean.getId();
					int gameid = newsListBean.getGameid();
					String gameTitle = newsListBean.getTitle();
					String gameICcon = newsListBean.getImg();
					EnterInformationDetial(id, gameid, gameTitle, gameICcon);
				}
			}
		});
		listview.setXListViewListener(new XListView.IXListViewListener() {
			@Override
			public void onRefresh() {
				getData(1);
			}

			@Override
			public void onLoadMore() {
				Logger.msg("开始加载更多","");
				getData(currentPage+1);
			}
		});
	}

	private void EnterInformationDetial(int type,int gameID,String gameTitle,String gameicon) {
		Intent intent1=new Intent(context,informationDetailActivity.class);
		intent1.putExtra("id",type);
		intent1.putExtra("gameid",gameID);
		intent1.putExtra("gameTitle",gameTitle);
		intent1.putExtra("gameicon",gameicon);
		startActivity(intent1);
		((Activity)context).overridePendingTransition(R.anim.inanimation,R.anim.outanimation);
	}

	@Override
	public void initData() {

		
	}
	String AAA="informationfragment";
	private void getData(final int requestPage) {
		Map<String, String> map = new HashMap<String, String>();
	/*	map.put(Constants.CLIENT_ID, 12 + "");
		map.put(Constants.APP_ID, 100 + "");
		map.put(Constants.FROM,3+"");*/
		map.put("catalog",0+"");
	//	map.put("gameid",2+"");
		map.put("offset", 15 + "");
		map.put("page",requestPage+"");
		String url = StringUtils.getUrlContainAppid(Constants.NEWS_LIST,map);
//		Logger.msg(AAA,"url="+url);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET,
				url,
				new RequestCallBack<String>(){

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						parjson(responseInfo.result,requestPage);
//						mProgressBar.setVisibility(View.GONE);
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						stateLayout.showContentView();
						listview.stopRefresh();
						listview.stopLoadMore();
//						mProgressBar.setVisibility(View.GONE);
					}
				});
	}

	private InformationBean informationBean;
	private void parjson(String res,int requestPage) {
		Logger.msg("获取到数据数据",res);
		listview.stopRefresh();
		listview.stopLoadMore();
		if(requestPage==1){
			listview.setRefreshTime(Utils.getTime());
		}
		stateLayout.showContentView();
		//homeBeanList = JsonUtil.json2Bean(res, BreakOnlineBean.class);
		informationBean= JsonUtil.getJsonUtil().json2Bean(res,InformationBean.class);
		if (informationBean!=null) {
			InformationBean.DataBean databean = informationBean.getData();
			if(databean!=null&&databean.getNews_list()!=null && databean.getNews_list().size() > 0){
				if(requestPage==1){
					datas.clear();
				}
				datas.addAll(databean.getNews_list());
				textInformationListadapter.notifyDataSetChanged();
				currentPage=requestPage;
//				if (databean.getNews_list().size() < 10){
//					Logger.msg("","没有更多数据");
//					listview.mFooterView.setState(XFooterView.STATE_NORMAL);
//					listview.mFooterView.mHintView.setText("没有更多数据");
//				}
			}else {
				Logger.msg("","没有更多数据");
				listview.mFooterView.mHintView.setText("没有更多数据");
			}
			if(datas.isEmpty()){
				stateLayout.showEmptyView();
			}
		}else {
			Logger.msg("","没有更多数据");
			listview.mFooterView.mHintView.setText("没有更多数据");
		}
	}

	@Override
	public CharSequence getTitle() {
		return null;
	}

}
