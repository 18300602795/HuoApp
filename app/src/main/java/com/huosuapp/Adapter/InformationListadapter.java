package com.huosuapp.Adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huosuapp.Bean.InformationBean;
import com.huosuapp.Util.ImgUtil;
import com.huosuapp.text.R;

import java.util.List;

public class InformationListadapter extends BaseAdapter{
	private Fragment mFragment;
	private List<InformationBean.DataBean.NewsListBean> news_list;
	public InformationListadapter(List<InformationBean.DataBean.NewsListBean> news_list, Fragment fragment) {
		this.news_list=news_list;
		mFragment=fragment;
	}

	@Override
	public int getCount() {
		return news_list.size();
	}

	@Override
	public Object getItem(int position) {
		return news_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null){
			ViewHold ch = new ViewHold();
			convertView =View.inflate(parent.getContext(), R.layout.information_item ,null);
			ch.tv_title= (TextView) convertView.findViewById(R.id.tv_infor_title);
			ch.tv_data= (TextView) convertView.findViewById(R.id.tv_infot_data);
			ch.im1= (ImageView) convertView.findViewById(R.id.iv_iv_1);
			convertView.setTag(ch);
		}
		ViewHold vh= (ViewHold) convertView.getTag();
		vh.tv_title.setText(news_list.get(position).getTitle());
		vh.tv_data.setText(news_list.get(position).getPudate());

		if(news_list.get(position).getImg()!=null){
			ImgUtil.loadImage(news_list.get(position).getImg(),R.drawable.icon_horizontal
					,vh.im1,mFragment);
		}
		return convertView;
	}

	static class ViewHold{
		TextView tv_title,tv_data;
		ImageView im1;
	}

}
