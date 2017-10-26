package com.huosuapp.imagedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huosuapp.text.R;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Photos> paths;
	private boolean localtion;// true是本地图片 false 是网络图片
	private FinalBitmap finalBit;

	private AddImage add;

	public ImageAdapter(Context context, ArrayList<Photos> paths, boolean localtion) {
		this.context = context;
		this.paths = paths;
		this.localtion = localtion;
		finalBit = FinalBitmap.create(context);
	}

	public void setAddImage(AddImage add) {
		this.add = add;
	}


	public void setData(ArrayList<Photos> paths) {
		this.paths = paths;
	}

	@Override
	public int getCount() {
		return paths == null ? 0 : paths.size();
	}

	@Override
	public Object getItem(int position) {
		return paths == null ? null : paths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.activity_index_gallery_item, parent, false);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.id_index_gallery_item_image);
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if(position < getCount()){
			Photos ph = (Photos) paths.get(position);
			finalBit.display(holder.imageView, ph.max);
		}
		
		return convertView;
	}

	private class Holder {
		public ImageView imageView;
	}

	/**
	 * 加载图片接口
	 * 
	 * @author jiangyue
	 * 
	 */
	public interface AddImage {
		public void addImage(ImageView view, String path);
	}
}
