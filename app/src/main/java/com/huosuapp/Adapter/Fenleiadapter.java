package com.huosuapp.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huosuapp.Bean.FenleiBean;
import com.huosuapp.text.R;

import java.util.List;

public class Fenleiadapter extends BaseAdapter {
		/*private int[] drwable= {R.drawable.online_stand1,R.drawable.online_stand2,R.drawable.online_stand3,R.drawable.online_stand4
	,R.drawable.online_stand5,R.drawable.online_standa6
	};*/

	private List<FenleiBean.DataBean> data;

	public Fenleiadapter(List<FenleiBean.DataBean> data) {
		this.data=data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null){
			convertView = View.inflate(parent.getContext(), R.layout.fenlei_item,null);
			ViewHold vh =new ViewHold();
			vh.tv_game_name= (TextView) convertView.findViewById(R.id.tv_fenlei);
			convertView.setTag(vh);
		}
		ViewHold vh = (ViewHold) convertView.getTag();
		vh.tv_game_name.setText(data.get(position).getTypename());
		return convertView;
	}


	static class ViewHold{
		//ImageView iv_game_icon;
		TextView tv_game_name;
		;
	}
	

}
