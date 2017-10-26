package com.huosuapp.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.huosuapp.text.R;

import java.util.ArrayList;

public class fenleiDetailsListadapter extends MyBaseAdapter<String> {
//	private int[] drawbles={R.drawable.home10,R.drawable.home11,R.drawable.home12,R.drawable.home13};

	public fenleiDetailsListadapter(ArrayList<String> dataList) {
		super(dataList);

	}

	@Override
	public int getItemLayoutId(int position) {

		return R.layout.fenleidetails_item;
	}

	@Override
	public Object createViewHolder(View convertView, int position) {
		    ViewHold vh = new ViewHold();
		    	vh.tv_download=(Button) convertView.findViewById(R.id.tv_download);
			   vh.iv_game_icon=(ImageView) convertView.findViewById(R.id.iv_game_icon);
//			   vh.tv_game_name=(TextView) convertView.findViewById(R.id.tv_game_name);
//			   vh.tv_count_size=(TextView) convertView.findViewById(R.id.tv_count_size);
//			   vh.tv_game_desc=(TextView)convertView.findViewById(R.id.tv_game_desc);

		return vh;
	}

	@Override
	public void showData(Object viewHolder, String data, int position) {
		ViewHold vh= (ViewHold) viewHolder;
		//测试数据
		String text = data;
//		vh.tv_count_size.setText(text);
//		vh.tv_count_size.setBackgroundResource((position==0||position==1)?R.drawable.red_big:R.drawable.red_yellow);
//		vh.iv_game_icon.setImageResource(drawbles[position]);
//		vh.tv_game_name.setTypeface(typeFace);
//		vh.tv_count_size.setTypeface(typeFace);
//		vh.tv_game_desc.setTypeface(typeFace);


	}
	static class ViewHold{
		ImageView iv_game_icon;
//		TextView tv_game_name,tv_count_size,tv_game_desc;
		Button tv_download;
		;
	}
	
/*	*//** 创建一个50 ~ 200范围的随机颜色 *//*
	public static int createRandomColor() {
		Random random = new Random();
		int red = 177 + random.nextInt(50);		// 50 ~ 200
		int green = 177 + random.nextInt(50);	// 50 ~ 200
		int blue = 177 + random.nextInt(50);	// 50 ~ 200
		int color = Color.argb(255, red, green, blue);
		return color;
	}*/
	

}
