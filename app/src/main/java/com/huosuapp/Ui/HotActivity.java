package com.huosuapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.Adapter.HotList_adapter;
import com.huosuapp.Adapter.Hotdanji_adapter;
import com.huosuapp.Adapter.Hotwangyou_adapter;
import com.huosuapp.Bean.HotRenmenBean;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.text.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 热门游戏列表界面
 */
public class HotActivity extends BaseActivity2 implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private final static int TYPE_SHI_XIA_RE_MEN = 1;
    private final static int TYPE_SHI_XIA_DAN_JI = 2;
    private final static int TYPE_RE_MEN_WANG_YOU = 3;
    private ListView hot_lv;//时下热门列表
    private ImageButton igb;//点击返回键进入到用户信息
    private ArrayList<String> list = new ArrayList<String>();
    private TextView tv;  //界面标题
    private RelativeLayout rrt_return;
    //    private String Url;
    private int title; //传过来的标题
    private static final String ATG = "HotActivity";
    private int focusState = 0;
    private final static int SHIXIARENMEN = 1;
    private final static int RENMENDANJIAN = 2;
    private final static int RENMENWANGYOU = 4;
    private HotList_adapter mhotlist;//时下热门适配器
    private Hotdanji_adapter hotdanji_adapter;//热门单机适配器
    private Hotwangyou_adapter hotwangyou_adapter;//热门网游适配器
    private BGARefreshLayout bgaRefreshLayout;
    private int currentPage = 1;
    private View layoutNoData;
    /**
     * 初始化数据的请求类型
     */
    protected static final int TYPE_INIT_DATA = 0;
    /**
     * 加载更多数据的请求类型
     */
    protected static final int TYPE_LOADING_MORE = 1;

    @Override
    public void parUI(Message mes) {
    }

    @Override
    public void initUI() {
        bgaRefreshLayout = (BGARefreshLayout) findViewById(R.id.bga_refresh);
        bgaRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(this, true));
        hot_lv = (ListView) findViewById(R.id.lv_hot);
        bgaRefreshLayout.setDelegate(this);
        tv = (TextView) findViewById(R.id.tv_nav);
        layoutNoData = findViewById(R.id.layout_noData);
        rrt_return = (RelativeLayout) findViewById(R.id.all_return);
        title = getIntent().getIntExtra("hot", 0);
        Logger.msg(ATG, title + "");
        initTitle();
        createAnim();
    }


    public void initTitle() {
        switch (title) {
            case 1:    //  时下热门
                tv.setText("时下热门");
                focusState = SHIXIARENMEN;
                break;
            case 2:    //  时下热门
                tv.setText("破解版");
                focusState = RENMENDANJIAN;
                break;
            case 3:    //  时下热门
                tv.setText("变态版");
                focusState = RENMENWANGYOU;
                break;

        }
    }


    @Override
    public void initData() {
        getData(1);
    }

    public void getData(final int requestPage) {
        Map<String, String> map = new HashMap<String, String>();
        if (focusState == SHIXIARENMEN) {
        }
        if (focusState == RENMENDANJIAN) {
            map.put("category", 1 + "");
        }
        if (focusState == RENMENWANGYOU) {
            map.put("category", 2 + "");
        }
        map.put("hot", 1 + "");
        map.put("page", requestPage + "");
        map.put("offset", 30 + "");
        String url = StringUtils.getUrlContainAppid(Constants.URL_GAME_LIST, map);
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(0 * 1000);
        Logger.msg("热门url", url);
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parjson2(responseInfo.result, requestPage);
                        Logger.msg("获取出时下热门结果", responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        bgaRefreshLayout.endLoadingMore();
                        bgaRefreshLayout.endRefreshing();
                        if (mhotlist == null || mhotlist.getDataList().isEmpty()) {
                            layoutNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private HotRenmenBean homeBeanList;

    private void parjson2(String res, int requestPage) {
        bgaRefreshLayout.endLoadingMore();
        bgaRefreshLayout.endRefreshing();
        homeBeanList = JsonUtil.getJsonUtil().json2Bean(res, HotRenmenBean.class);
        if (homeBeanList != null) {
            final HotRenmenBean.DataBean databean = homeBeanList.getData();
            if (databean != null) {
                if (requestPage == 1) {
                    mhotlist = new HotList_adapter(new ArrayList<HotRenmenBean.DataBean.GameListBean>(), HotActivity.this);
                    hot_lv.setAdapter(mhotlist);
                }
//                else{
//                    mhotlist.getDataList().clear();
//                }
                if (databean.getGame_list() != null) {
                    Collections.sort(databean.getGame_list(), new HotComparator());
                    mhotlist.getDataList().addAll(databean.getGame_list());
                    currentPage = requestPage;
                }
            }
        }
        if (mhotlist == null || mhotlist.getDataList().isEmpty()) {
            layoutNoData.setVisibility(View.VISIBLE);
        } else {
            layoutNoData.setVisibility(View.GONE);
        }
        if (mhotlist != null)
            mhotlist.notifyDataSetChanged();
    }

    @Override
    public int getViewID() {
        return R.layout.hot_act;
    }

    /**
     * 本页的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_return:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
            default:
                break;
        }

    }

    @Override
    protected void initListener() {
        rrt_return.setOnClickListener(this);
        hot_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                HotRenmenBean.DataBean.GameListBean gameListBean = (HotRenmenBean.DataBean.GameListBean) adapterView.getItemAtPosition(position);
                if (gameListBean != null) {
                    enterIntoGameDetails(gameListBean.getGameid());
                }

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
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        getData(1);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        getData(currentPage + 1);
//        getData(1);
        return true;
    }

    public static void start(Context context, int type) {
        Intent starter = new Intent(context, HotActivity.class);
        starter.putExtra("type", type);
        context.startActivity(starter);
    }

    public class HotComparator implements Comparator<HotRenmenBean.DataBean.GameListBean> {

        @Override
        public int compare(HotRenmenBean.DataBean.GameListBean o1, HotRenmenBean.DataBean.GameListBean o2) {
            return o1.getDowncnt() - o2.getDowncnt() < 0 ? 1 : -1;
        }
    }

    //开场动画
    private void createAnim() {
        hot_lv.setAlpha(0);
        hot_lv.setTranslationY(100);
        tv.setScaleX(0);

        hot_lv.animate()
                .setDuration(1000)
                .translationY(0)
                .alpha(1);
        tv.animate().setDuration(1000).scaleX(1);
    }

}
