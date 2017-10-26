package com.huosuapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huosuapp.Adapter.SearchGVHot_Adapter;
import com.huosuapp.Adapter.SearchGVTuijian_Adapter;
import com.huosuapp.Adapter.SerachResultListAdapter;
import com.huosuapp.Bean.HomeBeanList;
import com.huosuapp.Bean.SearchTuijianHotBean;
import com.huosuapp.Bean.SearchhotWordBean;
import com.huosuapp.MyApplication;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.text.R;
import com.huosuapp.view.FlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 搜索界面
 */
public class SearchActivity extends BaseActivity2 {
    private ImageView img;

    private LinearLayout llt1; //装载着第一个流布局
    private LinearLayout llt2; //装载着第二个流布局
    private FlowLayout flowLayout; //流布局
    private SearchGVHot_Adapter searchGVHot_adapter;//搜索热词适配器
    private SearchGVTuijian_Adapter searchGVTuijian_adapter;//推荐搜索适配器
    private GridView hot_gv, trjian_gv;//搜索热词，推荐搜索
    private ListView listView_searchResult;//搜索结果
    private LinearLayout layoutsearchResult;//搜索结果布局
    private SerachResultListAdapter searchResultAdapter;//搜索结果适配器
    private EditText et_search;//输入框
    private Long lastFontChangeTime;


    @Override
    public void parUI(Message mes) {
        switch (mes.what) {
            case 1:
                try{
                    if (searchGVHot_adapter == null) {
                        searchGVHot_adapter = new SearchGVHot_Adapter(searchhotWordBean.getData(), SearchActivity.this);
                        //  Toast.makeText(SearchActivity.this,"到搜索了",Toast.LENGTH_LONG).show();
                    }
                    hot_gv.setAdapter(searchGVHot_adapter);
                    searchGVHot_adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Logger.msg("数据错误", e.toString());
                }
                break;
            case 2:
                try{
                    if (searchGVTuijian_adapter == null) {
                        searchGVTuijian_adapter = new SearchGVTuijian_Adapter(searchTuijianHotBean.getData(), SearchActivity.this);
                        //  Toast.makeText(SearchActivity.this,"到搜索了",Toast.LENGTH_LONG).show();
                    }
                    Logger.msg("123", "444");
                    trjian_gv.setAdapter(searchGVTuijian_adapter);
                    searchGVTuijian_adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Logger.msg("数据错误", e.toString());
                }
                break;
            case 3:
                if (searchResultBean != null && searchResultBean.getData() == null) {
                    searchResultAdapter = new SerachResultListAdapter(new ArrayList<HomeBeanList.DataBean.GameListBean>(), SearchActivity.this);
                    listView_searchResult.setAdapter(searchResultAdapter);
                }
                if (searchResultBean != null && searchResultBean.getData() != null && searchResultBean.getData().getGame_list() != null && searchResultBean.getData().getGame_list().size() > 0) {
                    searchResultAdapter = new SerachResultListAdapter(searchResultBean.getData().getGame_list(), SearchActivity.this);
                    listView_searchResult.setAdapter(searchResultAdapter);
                }
                break;
        }
    }


    /**
     * 初始化本页界面
     */
    public void initUI() {
        hot_gv = (GridView) findViewById(R.id.houtWord_gridview); //搜索热词布局
        layoutsearchResult = (LinearLayout) findViewById(R.id.searchResult); //搜索布局
        trjian_gv = (GridView) findViewById(R.id.ruijian_gridview); //推荐搜索布局
        listView_searchResult = (ListView) findViewById(R.id.lv_searchResult);
        //llt1 = (LinearLayout) findViewById(R.id.llt_1);
        // llt2 = (LinearLayout) findViewById(R.id.llt_2);
        et_search = (EditText) findViewById(R.id.et_search);
        img = (ImageView) findViewById(R.id.iv_logo);

        img.setOnClickListener(this);
        et_search.setFocusable(true);
        et_search.setFocusableInTouchMode(true);
        et_search.requestFocus();
    /*    flowLayout = new FlowLayout(this);
        int padding = Global.dp2px(6, this);
        flowLayout.setPadding(padding, padding, padding, padding);
        llt1.addView(flowLayout);*/
        // showData();

    }

    @Override
    public void initData() {
        //获取搜索热词
        getData();

    //获取推荐搜索
        getData1();
    }


    private void getData1() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("clientid", 1 + "");
        map.put("appid", 1 + "");
        map.put("agent", "");
        OkHttpUtils.postString(Constants.RECOMMEND_LIST, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                Logger.msg(ALL, res);
                parjson1(res);
            }
        });
    }

    private SearchTuijianHotBean searchTuijianHotBean;

    private void parjson1(String res) {
        // searchTuijianHotBean = JsonUtil.json2Bean(res, SearchTuijianHotBean.class);
        searchTuijianHotBean = JsonUtil.getJsonUtil().json2Bean(res, SearchTuijianHotBean.class);
        if (searchTuijianHotBean != null) {
            Message mes = Message.obtain();
            mes.what = 2;
            handler.sendMessage(mes);
        }
    }

    private String ALL = "SearchActivity";

    private void getData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("clientid", 1 + "");
        map.put("appid", 1 + "");
        map.put("agent", "");
        OkHttpUtils.postString(Constants.HOTWORD_LIST, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                Logger.msg(ALL, res);
                parjson(res);
            }
        });
    }

    private SearchhotWordBean searchhotWordBean;

    private void parjson(String res) {
        // searchhotWordBean = JsonUtil.json2Bean(res, SearchhotWordBean.class);
        searchhotWordBean = JsonUtil.getJsonUtil().json2Bean(res, SearchhotWordBean.class);
        if (searchhotWordBean != null) {
            Message mes = Message.obtain();
            mes.what = 1;
            handler.sendMessage(mes);
        }
    }

    @Override
    public int getViewID() {
        return R.layout.search_act;
    }


    /**
     * 本页的点击事件
     */
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.iv_logo:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
            default:
                break;
        }

    }

    @Override
    protected void initListener() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    layoutsearchResult.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    return;
                }
                layoutsearchResult.setVisibility(View.VISIBLE);
                initSearchData(s.toString());

            }
        });

        //// 输入完后按键盘上的搜索键
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                return false;
            }
        });


//        /**
//         * 搜索出来点击列表监听，去到游戏详情页
//         */
//
//        listView_searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                HomeBeanList.DataBean.GameListBean gameListBean = (HomeBeanList.DataBean.GameListBean) parent.getItemAtPosition(position);
//                if (gameListBean != null) {
//                    int gameID = gameListBean.getGameid();
//                    enterIntoGameDetails(gameID);
//                }
//
//            }
//        });


        /**
         * 搜索热词布局
         */
        hot_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                et_search.setText(s);
                et_search.setSelection(s.length());
                initSearchData(s);
            }
        });


        trjian_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                et_search.setText(s);
                et_search.setSelection(s.length());
                initSearchData(s);
            }
        });
    }

    private void initSearchData(String s) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("clientid", 12 + "");
        map.put("appid", 100 + "");
        map.put("from", "3");
        map.put("agent", "");
        map.put("q", s + "");
        map.put("catalog", "");

        String url = StringUtils.getCompUrlFromParams(Constants.URL_SEARCH_LIST, map);
        Logger.msg("查询", url);
        OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                Logger.msg("搜索出来的数据", res);
                parjson2(res);
            }
        });
    }

    HomeBeanList searchResultBean;

    private void parjson2(String res) {
        //  searchResultBean = JsonUtil.json2Bean(res, SearchResultBean.class);
        searchResultBean = JsonUtil.getJsonUtil().json2Bean(res, HomeBeanList.class);
        if (searchResultBean != null) {
            Message mes = Message.obtain();
            mes.what = 3;
            handler.sendMessage(mes);
        }

    }


    /**
     * 进入首页列表的游戏详情
     *
     * @param gameID
     */
    public void enterIntoGameDetails(int gameID) {
        Intent intent3 = new Intent(this, GameDetailActivity.class);
        intent3.putExtra("gameID", gameID);
        startActivity(intent3);
        overridePendingTransition(R.anim.act_in, R.anim.act_out);
    }
}
