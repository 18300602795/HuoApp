package com.huosuapp.Ui;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.Adapter.FenleiList_adapter;
import com.huosuapp.Bean.JueSeBean;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.Util.Utils;
import com.huosuapp.text.R;
import com.huosuapp.widget.XFooterView;
import com.huosuapp.widget.XListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/8/9.
 */
public class FenleiGameDetailsActivity extends BaseActivity2 {
    private TextView textView;
    private RelativeLayout all_retrun;
    private XListView listView;
    private FenleiList_adapter fenleiList_adapter;
    private int currentPage = 1;
    /**
     * 初始化数据的请求类型
     */
    protected static final int TYPE_INIT_DATA = 0;
    /**
     * 加载更多数据的请求类型
     */
    protected static final int TYPE_LOADING_MORE = 1;
    private View layoutNoData;

    int typeId;
    String name;

    @Override
    public void parUI(Message mes) {
        if (mes.what == 11) {
            if (fenleiList_adapter == null) {
                fenleiList_adapter = new FenleiList_adapter(null, this);
                listView.setAdapter(fenleiList_adapter);
            }
            JueSeBean.DataBean databean = (JueSeBean.DataBean) mes.obj;
            if (databean == null) {
//                stateLayout.showFailView();
            }
            if (databean != null) {
//                stateLayout.showContentView();
                layoutNoData.setVisibility(View.GONE);
                fenleiList_adapter.getDataList().addAll(databean.getGame_list());
                fenleiList_adapter.notifyDataSetChanged();
            }

        } else if (mes.what == 404) {
//            stateLayout.showEmptyView();
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initListener() {
        all_retrun.setOnClickListener(this);
        //列表点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //进入到游戏详情
                JueSeBean.DataBean.GameListBean gameListBean = (JueSeBean.DataBean.GameListBean) adapterView.getItemAtPosition(position);
                if (gameListBean != null) {
                    int gameid = gameListBean.getGameid();
                    enterIntoGameDetails(gameid);
                }
            }
        });

        /**
         * 加载更多监听
         */
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getData(1);
            }

            @Override
            public void onLoadMore() {
                Logger.msg("开始加载更多", currentPage + "");
                getData(currentPage + 1);
            }
        });
    }


    /**
     * 进入首页列表的详情
     *
     * @param gameID
     */
    public void enterIntoGameDetails(int gameID) {
        Intent intent3 = new Intent(this, GameDetailActivity.class);
        intent3.putExtra("gameID", gameID);
        startActivity(intent3);
        overridePendingTransition(R.anim.act_in, R.anim.act_out);
    }

    @Override
    public void initUI() {
        textView = (TextView) findViewById(R.id.tv_nav);
        name = getIntent().getStringExtra("textTile");
        typeId = getIntent().getIntExtra("typeid", 0);
        //  Url=initTitle();
        all_retrun = (RelativeLayout) findViewById(R.id.all_return);
        listView = (XListView) findViewById(R.id.lv_fenleidetails);
        layoutNoData = findViewById(R.id.layout_noData);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(true);
        listView.setAutoLoadEnable(true);
        textView.setText(name);
//        stateLayout.showContentView();
//        layoutNoData.setVisibility(View.VISIBLE);
        fenleiList_adapter = new FenleiList_adapter(null, this);
        listView.setAdapter(fenleiList_adapter);
        createAnim();
    }

    @Override
    public void initData() {
        getData(1);
    }

    //    private String AAA="FenleiGameDetailsActivity";
    private void getData(int type) {
        listView.mFooterView.setState(XFooterView.STATE_LOADING);
        getFenleiGame(type);
    }

    private void getFenleiGame(final int type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", typeId + "");
        map.put("page", type + "");
        map.put("offset", 15 + "");
        String url = StringUtils.getUrlContainAppid(Constants.URL_GAME_LIST, map);
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(0 * 1000);
        Logger.msg("开始获取数据", "");
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Logger.msg("游戏分类数据", responseInfo.result);
                        parjson(responseInfo.result, type);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
//                        stateLayout.showContentView();
                        Logger.msg("数据获取失败", msg);
                        if (fenleiList_adapter.getDataList().isEmpty()) {
                            layoutNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
//        OkHttpUtils.getString(url, false, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Logger.msg("数据获取失败", e.toString());
//                if (fenleiList_adapter.getDataList().isEmpty()) {
//                    layoutNoData.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String res = response.body().string().trim();
//                Logger.msg("游戏分类数据", res);
//                parjson(res, type);
//            }
//        });
    }


    private JueSeBean homeBeanList;

    private void parjson(String res, int type) {
        //homeBeanList = JsonUtil.json2Bean(res, JueSeBean.class);
        listView.stopRefresh();
        listView.stopLoadMore();
        if (type == 1) {
            listView.setRefreshTime(Utils.getTime());
        }
        layoutNoData.setVisibility(View.GONE);
//        stateLayout.showContentView();
        //homeBeanList = JsonUtil.json2Bean(res, BreakOnlineBean.class);

        homeBeanList = JsonUtil.getJsonUtil().json2Bean(res, JueSeBean.class);
        if (homeBeanList != null) {
            JueSeBean.DataBean databean = homeBeanList.getData();
            if (databean != null && databean.getGame_list() != null && databean.getGame_list().size() > 0) {
                if (type == 1) {
                    fenleiList_adapter.getDataList().clear();
                }
                fenleiList_adapter.getDataList().addAll(databean.getGame_list());
                fenleiList_adapter.notifyDataSetChanged();
                currentPage = type;
                if (databean.getGame_list().size() < 15) {
                    listView.mFooterView.setState(XFooterView.STATE_NORMAL);
                    listView.mFooterView.mHintView.setText("没有更多数据");
                }
            } else {
                Logger.msg("", "没有更多数据");
                listView.mFooterView.mHintView.setText("没有更多数据");
            }
        }
        if (fenleiList_adapter.getDataList().isEmpty()) {
//                stateLayout.showEmptyView();
            layoutNoData.setVisibility(View.VISIBLE);
        } else {
            layoutNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public int getViewID() {
        return R.layout.fenleidetails_main;
    }

    //开场动画
    private void createAnim() {
        listView.setAlpha(0);
        listView.setTranslationY(100);
        all_retrun.setScaleX(0);

        listView.animate()
                .setDuration(1000)
                .translationY(0)
                .alpha(1);
        all_retrun.animate().setDuration(1000).scaleX(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_return:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
        }
    }
}
