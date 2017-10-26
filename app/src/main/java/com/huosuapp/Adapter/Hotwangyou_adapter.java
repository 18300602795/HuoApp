package com.huosuapp.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huosuapp.Bean.HotWangyouBean;
import com.huosuapp.text.R;

public class Hotwangyou_adapter extends BaseAdapter {

	Typeface typeFace ;
	private HotWangyouBean.DataBean databean;
	public Hotwangyou_adapter(HotWangyouBean.DataBean databean, Context context) {
		this.databean=databean;
		typeFace= Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
	}


	@Override
	public int getCount() {
		return 8;
		//return 3;
	}

	@Override
	public Object getItem(int position) {
		return databean.getGame_list().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null==convertView){
			convertView=View.inflate(parent.getContext(), R.layout.home_item,null);
			ViewHold vh = new ViewHold();
			vh.tv_download=(Button) convertView.findViewById(R.id.tv_download);
			vh.iv_game_icon=(ImageView) convertView.findViewById(R.id.iv_game_icon);
			vh.tv_game_name=(TextView) convertView.findViewById(R.id.tv_game_name);
//			vh.tv_count_size=(TextView) convertView.findViewById(R.id.tv_count_size);
			vh.tv_game_desc=(TextView)convertView.findViewById(R.id.tv_game_desc);
			convertView.setTag(vh);
		}
		ViewHold vh = (ViewHold) convertView.getTag();
		HotWangyouBean.DataBean.GameListBean  gamelistbean=databean.getGame_list().get(position);
//		vh.tv_count_size.setBackgroundResource((position==0||position==1)?R.drawable.red_big:R.drawable.red_yellow);
		vh.tv_game_name.setText(gamelistbean.getGamename());
		vh.tv_game_name.setTypeface(typeFace);
		return convertView;
	}

	static class ViewHold{
		ImageView iv_game_icon;
		TextView tv_game_name,tv_game_desc;
		Button tv_download;
		;
	}
}
