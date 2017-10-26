package com.huosuapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huosuapp.text.R;

import java.util.List;

/**
 * Created by admin on 2016/8/26.
 */
public class SearchGVHot_Adapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public SearchGVHot_Adapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public int getCount() {
        return list==null?0:list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHoudle viewHoudle = null;
        if (view == null) {
            viewHoudle = new ViewHoudle();
            view = LayoutInflater.from(context).inflate(R.layout.item_gridview, null);
            viewHoudle.tv = (TextView) view.findViewById(R.id.tv);
            view.setTag(viewHoudle);
        } else {
            viewHoudle = (ViewHoudle) view.getTag();
        }
        viewHoudle.tv.setText(list.get(position));
        return view;
    }

    class ViewHoudle {
        TextView tv;
    }
}
