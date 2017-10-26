package com.huosuapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huosuapp.Bean.SearchResultBean;
import com.huosuapp.Ui.SearchActivity;
import com.huosuapp.text.R;

import java.util.List;

/**
 * Created by admin on 2016/8/26.
 */
public class SearchResultAdapter extends BaseAdapter {

    private List<SearchResultBean.DataBean.GameListBean> game_list;
    private Context context;
    public SearchResultAdapter(List<SearchResultBean.DataBean.GameListBean> game_list, SearchActivity searchActivity) {
        this.game_list=game_list;
        this.context=searchActivity;
    }

    @Override
    public int getCount() {
        return game_list.size();
    }

    @Override
    public Object getItem(int position) {
        return game_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoudle2 viewHoudle2 = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_searchresult, null);
            viewHoudle2 = new ViewHoudle2();
            viewHoudle2.result_title = (TextView) convertView.findViewById(R.id.result_title);
            convertView.setTag(viewHoudle2);
        } else {
            viewHoudle2 = (ViewHoudle2) convertView.getTag();
        }
        viewHoudle2.result_title.setText(game_list.get(position).getGamename());
        return convertView;
    }
    class ViewHoudle2 {
        TextView result_title;
    }
}
