package com.huosuapp.Fragment;

import android.os.Message;
import android.view.View;

import com.huosuapp.Adapter.gift_code_Listadapter;
import com.huosuapp.Bean.GiftCodyBean;
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

public class gift_cody_fragment extends Basefragment {
    private gift_code_Listadapter mgiGiftListadapter;
    private XListView ls;
    private GiftCodyBean giftCodyBean;
    private int currentPage=1;
    /** 礼包列表刷新的 */
    protected static final int UPDATE_CODE = 3;

    @Override
    public void parUI(Message mes) {
    }

    @Override
    public View getContentView() {

        return null;
    }

    @Override
    public int getContentViewLayoutId() {

        return R.layout.gift_code_fragment;
    }

    @Override
    public void initView() {
        stateLayout.showContentView();
        ls = findView(R.id.lv_gift_code);
        ls.setPullRefreshEnable(true);
        ls.setPullLoadEnable(true);
        ls.setAutoLoadEnable(true);
        if (mgiGiftListadapter == null) {
            mgiGiftListadapter = new gift_code_Listadapter(null, context,this);
            ls.setAdapter(mgiGiftListadapter);
            ls.mFooterView.setState(XFooterView.STATE_LOADING);
        }
    }

    @Override
    public void initListener() {
        /**
         * 加载更多监听
         */
        ls.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getCodData(1);
            }

            @Override
            public void onLoadMore() {
                getCodData(currentPage+1);
            }
        });

    }

    @Override
    public void initData() {
        getCodData(1);

    }

    private String AOO = "gift_cody_fragment";


    public void getCodData(final int type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", type + "");
        map.put("offset", 15 + "");

        String url = StringUtils.getUrlContainAppid(Constants.GIFT_LIST, map);
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(0*1000);
        http.configCurrentHttpCacheExpiry(0);
        Logger.msg("礼包数据接口", url);
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>(){
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parjson(responseInfo.result, type);
                        Logger.msg(AOO, responseInfo.result);
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        stateLayout.showContentView();
                        ls.stopRefresh();
                        ls.stopLoadMore();
                    }
                });


//        OkHttpUtils.getString(url, false, new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                String res = response.body().string().trim();
//                parjson(res, type);
//                Logger.msg(AOO, res);
//            }
//        });
    }


    private void parjson(String res, int type) {
        ls.stopRefresh();
        ls.stopLoadMore();
        if(type==1){
            ls.setRefreshTime(Utils.getTime());
        }
        stateLayout.showContentView();
        //homeBeanList = JsonUtil.json2Bean(res, BreakOnlineBean.class);
        giftCodyBean = JsonUtil.getJsonUtil().json2Bean(res, GiftCodyBean.class);
        if (giftCodyBean!=null) {
            GiftCodyBean.DataBean databean = giftCodyBean.getData();
            if(databean!=null&&databean.getGift_list()!=null){
                if(type==1){
                    mgiGiftListadapter.getDataList().clear();
                }
                mgiGiftListadapter.getDataList().addAll(databean.getGift_list());
                mgiGiftListadapter.notifyDataSetChanged();
                currentPage=type;
                if (databean.getGift_list().size() < 15){
                    Logger.msg("","没有更多数据");
                    ls.mFooterView.setState(XFooterView.STATE_NORMAL);
                    ls.mFooterView.mHintView.setText("没有更多数据");
                }
            }else {
                Logger.msg("","没有更多数据");
                ls.mFooterView.mHintView.setText("没有更多数据");
            }
            if(mgiGiftListadapter.getDataList().isEmpty()){
                stateLayout.showEmptyView();
            }
        }
    }

    public CharSequence getTitle() {
        return "游戏礼包";
    }

}
