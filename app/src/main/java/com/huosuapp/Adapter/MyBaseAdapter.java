package com.huosuapp.Adapter;

import java.util.ArrayList;


import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.huosuapp.MyApplication;

/**
 * 其它的Adapter应该继承这个基类
 * @author 
 *
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

	/** 数据列表 */
	protected ArrayList<T> dataList;
	protected  Typeface typeFace =Typeface.createFromAsset(MyApplication.getContext().getAssets(),"Roboto-Medium.ttf");
	
	public MyBaseAdapter(ArrayList<T> dataList) {
		this.dataList = dataList;
	}
	
	/** 如果原来没有数据，则返回一个空集合，如果有则返回原来的有数据的集合 */
	public ArrayList<T> getDataList() {
		if (dataList == null) {
			dataList = new ArrayList<T>();
		}
		return dataList;
	}

	@Override
	public int getCount() {
		return dataList == null ? 0 : dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 生成一个ListView的item界面
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object holder;
		if (convertView == null) {
			// 如果convertView是null，则填充出一个View，并且用一个ViewHolder把它里面的控件引用保存起来
			convertView = View.inflate(parent.getContext(), getItemLayoutId(position), null);
			holder = createViewHolder(convertView, position);
			convertView.setTag(holder);
		} else {
			holder = convertView.getTag();
		}
		
		// 取出position对应的数据
		T data = dataList.get(position);
		
		// 把数据显示到Holder里面的控件中
		showData(holder, data, position);
		return convertView;
	}
	
	/** 返回一个用于显示ListView的item的布局id */
	public abstract int getItemLayoutId(int position);
	
	/** 
	 * 创建一个ViewHolder对象，并保存item里面的控件引用 
	 * @param position 
	 * @param convertView item的那个view
	 */
	public abstract Object createViewHolder(View convertView, int position);
	
	/** 
	 * 把数据显示到Holder里面的控件中 
	 * @param position 
	 * @param data 
	 * @param holder 
	 */
	public abstract void showData(Object viewHolder, T data, int position);
	

}
