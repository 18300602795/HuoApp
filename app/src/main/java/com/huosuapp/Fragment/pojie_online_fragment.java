package com.huosuapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.View;

import com.huosuapp.Adapter.Pojiewangyou_adapter;
import com.huosuapp.Bean.BreakOnlineBean;
import com.huosuapp.Ui.GameDetailActivity;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.text.R;
import com.huosuapp.widget.XFooterView;
import com.huosuapp.widget.XListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 分类中的网游界面的fragment
 */
public class pojie_online_fragment extends Basefragment {
    private ArrayList<String> list = new ArrayList<String>();
    private XListView ls;
    private Pojiewangyou_adapter pojiewangyou_adapter;
    private int currentPage=1;
    @Override
    public void parUI(Message mes) {
        if (mes.what == 2) {
            BreakOnlineBean.DataBean dataBean= (BreakOnlineBean.DataBean) mes.obj;
            if (dataBean==null){
                stateLayout.showFailView();
            }
            if (dataBean!=null) {
                stateLayout.showContentView();
                pojiewangyou_adapter.getDataList().addAll(dataBean.getGame_list());
                pojiewangyou_adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public View getContentView() {
        return null;
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.pojie_online_fragment;
    }

    @Override
    public void initView() {
        stateLayout.showContentView();
        ls = findView(R.id.lv_pojie_online);
        ls.setPullRefreshEnable(true);
        ls.setPullLoadEnable(true);
        ls.setAutoLoadEnable(true);
        if (pojiewangyou_adapter==null) {
            pojiewangyou_adapter = new Pojiewangyou_adapter(null, context);
            ls.setAdapter(pojiewangyou_adapter);
            ls.mFooterView.setState(XFooterView.STATE_LOADING);
        }
    }

    @Override
    public void initListener() {
//        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                BreakOnlineBean.DataBean.GameListBean databean = (BreakOnlineBean.DataBean.GameListBean) adapterView.getItemAtPosition(position);
//                if(databean!=null){
//                    enterIntoGameDetails(databean.getGameid());
//                }
//
//            }
//        });

        /**
         * 加载更多监听
         */
        ls.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getData(1);
            }

            @Override
            public void onLoadMore() {
                getData(currentPage+1);
            }
        });
//        ls.setOnLoadingMoreListener(new LoadMoreListView.OnLoadingMoreListener() {
//            @Override
//            public void onLoadingMore(LoadMoreListView listView, int scrollState) {
//                getData(TYPE_LOADING_MORE);
//            }
//            @Override
//            public void onClickLoadErroView(LoadMoreListView listView) {
//                getData(TYPE_LOADING_MORE);
//            }
//        });


    }

    /**
     * 进入首页列表的游戏详情
     *
     * @param gameID
     */
    public void enterIntoGameDetails(int gameID) {
        Intent intent3 = new Intent(context, GameDetailActivity.class);
        intent3.putExtra("gameID", gameID);
        context.startActivity(intent3);
        ((Activity) context).overridePendingTransition(R.anim.act_in, R.anim.act_out);
    }

    @Override
    public void initData() {
        getData(1);

    }

    private void getData(int type) {
        getPoJieOnline(type);
    }

    private String ATT = "Pojiewangyou_adapter";

    private void getPoJieOnline(final int type) {
        Map<String, String> map = new HashMap<String, String>();
      /*  map.put(Constants.CLIENT_ID, 12 + "");
        map.put(Constants.APP_ID, 100 + "");
        map.put(Constants.FROM,3+"");
        map.put("agent", "");*/
        map.put("category", 2 + "");
        map.put("classify", 1 + "");
        map.put("page",type+"");
        map.put("status",2 + "");
        map.put("offset", 15 + "");
        String url = StringUtils.getUrlContainAppid(Constants.URL_GAME_LIST,map);
        Logger.msg("pojie_online_fragment","url="+url);
        HttpUtils http = new HttpUtils();http.configCurrentHttpCacheExpiry(0*1000);
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>(){
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parjson3(responseInfo.result, type);
                        Logger.msg("pojie_online_fragment","res="+responseInfo.result);
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        stateLayout.showContentView();
                        ls.stopRefresh();
                        ls.stopLoadMore();
                    }
                });
//        OkHttpUtils.getString(url, false,new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Logger.msg(ATT, "访问失败");
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                String res = response.body().string().trim();
//                 Logger.msg(ATT, res);
//                Logger.msg("pojie_online_fragment","res="+res);
//                parjson3(res, type);
//            }
//        });

    }

    private BreakOnlineBean homeBeanList;
    private String getTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }
    private void parjson3(String res, int type) {
        ls.stopRefresh();
        ls.stopLoadMore();
        if(type==1){
            ls.setRefreshTime(getTime());
        }
        stateLayout.showContentView();
        //homeBeanList = JsonUtil.json2Bean(res, BreakOnlineBean.class);
        homeBeanList=JsonUtil.getJsonUtil().json2Bean(res,BreakOnlineBean.class);
        if (homeBeanList!=null) {
            BreakOnlineBean.DataBean databean = homeBeanList.getData();
            if(databean!=null&&databean.getGame_list()!=null){
                if(type==1){
                    pojiewangyou_adapter.getDataList().clear();
                }
                pojiewangyou_adapter.getDataList().addAll(databean.getGame_list());
                pojiewangyou_adapter.notifyDataSetChanged();
                currentPage=type;
                if (databean.getGame_list().size() < 15){
                    Logger.msg("","没有更多数据");
                    ls.mFooterView.setState(XFooterView.STATE_NORMAL);
                    ls.mFooterView.mHintView.setText("没有更多数据");
                }
            }else {
                Logger.msg("","没有更多数据");
                ls.mFooterView.mHintView.setText("没有更多数据");
            }
            if(pojiewangyou_adapter.getDataList().isEmpty()){
                stateLayout.showEmptyView();
            }
        }
    }

    public CharSequence getTitle() {
        return "网游";
    }


}
