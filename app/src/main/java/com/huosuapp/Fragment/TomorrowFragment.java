package com.huosuapp.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huosuapp.Adapter.TableListAdapter;
import com.huosuapp.Bean.KaifubiaoBean;
import com.huosuapp.MyApplication;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.text.R;
import com.huosuapp.widget.XFooterView;
import com.huosuapp.widget.XListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public class TomorrowFragment extends Fragment {
    private View view;
    private XListView listView;
    private TableListAdapter mAdapter;
    private String result;
    public int tomorrow_pager;
    private View layoutNoData;
    private Handler hanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listView.stopRefresh();
            listView.stopLoadMore();
            switch (msg.what) {
                case 1:
                    layoutNoData.setVisibility(View.GONE);
                    List<KaifubiaoBean> beans = (List<KaifubiaoBean>) msg.obj;
                    if (mAdapter.getDateList().size() != 0) {
                        mAdapter.addData((List<KaifubiaoBean>) msg.obj);
                    } else {
                        mAdapter = new TableListAdapter(beans, getActivity());
                        listView.setAdapter(mAdapter);
                    }
                    break;
                case 2:
                    layoutNoData.setVisibility(View.GONE);
                    if (mAdapter.getDateList().size() == 0) {
                        layoutNoData.setVisibility(View.VISIBLE);
                    } else {
                        listView.mFooterView.setState(XFooterView.STATE_NORMAL);
                        listView.mFooterView.mHintView.setText("没有更多数据");
                    }

                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kaifubiao, null);
        listView = (XListView) view.findViewById(R.id.table_rv);
        layoutNoData = view.findViewById(R.id.layout_noData);
        mAdapter = new TableListAdapter(null, getActivity());
        listView.setAdapter(mAdapter);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(true);
        listView.setAutoLoadEnable(true);
        listView.mFooterView.setState(XFooterView.STATE_LOADING);
        getDate();
        /**
         * 加载更多监听
         */
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Logger.msg("开始刷新数据", "");
//                analysisDate(tomorrow_pager, result);
                Message msg = new Message();
                msg.what = 2;
                hanler.sendMessage(msg);
            }

            @Override
            public void onLoadMore() {
                Logger.msg("开始下拉加载更多", tomorrow_pager + "");
//                listView.mFooterView.mHintView.setText("加载更多");
//                listView.mFooterView.setState(XFooterView.STATE_LOADING);
//                analysisDate(tomorrow_pager, result);
                Message msg = new Message();
                msg.what = 2;
                hanler.sendMessage(msg);
            }
        });
        return view;
    }


    public void getDate() {
        //获取当前星期几
        layoutNoData.setVisibility(View.GONE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String baseUrl = Constants.TABLE_URL;
        Map<String, String> params = new HashMap<>();
        params.put("key", "2");
        params.put("to", "app");
        String url = StringUtils.getCompUrlFromParams(baseUrl, params);
        Logger.msg("加载数据的地址", url);
        OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 2;
                hanler.sendMessage(msg);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {

                result = response.body().string().trim();
                Logger.msg("加载的数据", result);
//                isFirst = true;
                analysisDate(1, result);
            }
        });
    }

    private void analysisDate(int j, String str) {
        List<KaifubiaoBean> kaidus = new ArrayList<>();
        try {
//            for (int i = j; i < 15 + j; i++) {
//                JSONObject jsonObject = new JSONObject(str)
//                        .getJSONObject("game_" + i);
//                Logger.msg("处理的数据", i + "：" + jsonObject);
//                if (i > 1) {
//                    TableBean bean = new TableBean();
//                    //获取时间戳并将其转换为正常时间
//                    String d = jsonObject.getString("D");
//                    String format = "yyyy-MM-dd HH:mm:ss";
//                    SimpleDateFormat sdf = new SimpleDateFormat(format);
//                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(new Date());
//                    String data = sdf.format(new Date(Long.valueOf(d + "000")));
//
//                    data = data.split(" ")[1];
//                    bean.setData(data);
//                    bean.setGameName(jsonObject.getString("B"));//B代表游戏名
//                    bean.setGameArea(jsonObject.getString("C"));//A代表要开服的区
//                    bean.setWeek(jsonObject.getString("A"));//A代表星期
//
//                    String gameDataStr = jsonObject.getString("gamedata");
//                    if (!gameDataStr.equals("No search to the data")) {
//                        JSONObject gameDataObj = new JSONObject(gameDataStr);
//                        bean.setIconUrl(gameDataObj.getString("icon"));
//                        bean.setGameId(gameDataObj.getInt("id"));
//                    } else {
//                        bean.setIconUrl("");
//                        bean.setGameId(0);
//                    }
//
////                    if (isOneDay(bean.getWeek())) {
////                        Logger.msg("今日开服", str);
//                    mBeanList.add(bean);
////                    }
//                }
//            }
            kaidus = JsonUtil.parseList(str, KaifubiaoBean.class);
        } catch (Exception e) {
            Message msg = new Message();
            msg.what = 2;
            hanler.sendMessage(msg);
            e.printStackTrace();
        } finally {
            tomorrow_pager = 15 + j;
//        ref_ly.setRefreshing(false);
            if ( kaidus == null || kaidus.size() == 0) {
                Message msg = new Message();
                msg.what = 2;
                hanler.sendMessage(msg);
            } else {
                Message msg = new Message();
               msg.obj = kaidus;
                msg.what = 1;
                hanler.sendMessage(msg);
            }

        }

    }
}
