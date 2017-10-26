package com.huosuapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huosuapp.text.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class CommentAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ViewHolder viewHolder;
	private List<HashMap<String, Object>> list_comment; // 一级评论数据
	private Context context;
	private OnClickListener myOnitemcListener;

	public CommentAdapter(Context context,
			List<HashMap<String, Object>> list_comment,
			OnClickListener myOnitemcListener) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list_comment = new ArrayList<HashMap<String, Object>>();
		this.myOnitemcListener = myOnitemcListener;
		this.list_comment.addAll(list_comment);

	}

	public void clearList() {
		this.list_comment.clear();
	}

	public void updateList(List<HashMap<String, Object>> list_comment) {
		this.list_comment.addAll(list_comment);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_comment.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_comment.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			// convertView = inflater.inflate(R.layout.item_comment, null);
			convertView = View.inflate(parent.getContext(),
					R.layout.item_comment, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_user_photo = (ImageView) convertView
					.findViewById(R.id.iv_user_photo);
			viewHolder.tv_user_name = (TextView) convertView
					.findViewById(R.id.tv_user_name);
			viewHolder.tv_user_comment = (TextView) convertView
					.findViewById(R.id.tv_user_comment);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String author = (String) list_comment.get(position).get("author");
		viewHolder.tv_user_name.setText(author == null ? "匿名" : author);
		viewHolder.tv_user_comment.setText(list_comment.get(position)
				.get("content").toString());
		HashMap<String, Object> map = list_comment.get(position);
		long t = 0;
		if(map.containsKey("create_time")){
			t = (Long) map.get("create_time");
		}else{
			t = System.currentTimeMillis();
		}
		
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yy-MM-dd HH:mm");
		String timeFormat = sdf2.format(new Date(t));

		viewHolder.time.setText(timeFormat);
		return convertView;
	}

	public class ViewHolder {
		private ImageView iv_user_photo; // 品论者 头像
		private TextView tv_user_name; // 品论者 昵称
		private TextView tv_user_comment; // 品论者 一级品论内容
		private TextView time; // 品论者 一级品论内容

	}
}
