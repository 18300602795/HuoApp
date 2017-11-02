package com.huosuapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huosuapp.Adapter.HotList_adapter;
import com.huosuapp.Bean.HotRenmenBean;
import com.huosuapp.Bean.LunboImgViewBean;
import com.huosuapp.MyApplication;
import com.huosuapp.Ui.GameDetailActivity;
import com.huosuapp.Ui.HotActivity;
import com.huosuapp.Ui.TableActivity;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.DimensionUtil;
import com.huosuapp.Util.ImageCache;
import com.huosuapp.Util.ImgUtil;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.text.R;
import com.huosuapp.view.LoadMoreListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 推荐页面
 */
public class Homefragment1 extends Basefragment implements
        OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private LoadMoreListView homeListView;
    private int currentPage=1;
    private View head1;
    private ImageView iv_gettop;// 快速回到顶部
    private ViewPager mvViewPager;// 轮播图
    private LinearLayout ll_dots; // 存储圆点的容器
    private mvViewPagerAdapter mvViewPageradapter;// 轮播图适配器
    private List<View> mDots = new ArrayList<>();// 存放圆点视图的集合
    private ArrayList<LunboImgViewBean.DataBean.ListBean> listbean;// 装载着轮播图的信息
    private Intent intents = new Intent();
    private SwipeRefreshLayout mRefreshLayout;

    private final int REFRESH_FAILURE = 1, REFRESH_SUCCEED = 2, PAGER = 5,GAME_LIST=6, GAME_MORE=7, GAME_NO_MORE=8, GAME_ERROR=9;


    /**
     * 中间四个图片
     */
    private LinearLayout mTable, mBT, mBreak, mGift;
    private HotRenmenBean.DataBean mHomeListBean;

    @Override
    public void parUI(Message mes) {
        switch (mes.what) {
            case PAGER:
                listbean = (ArrayList<LunboImgViewBean.DataBean.ListBean>) mes.obj;
                if (listbean != null && listbean.size() > 0) {
                    tryToShowAD();
                    showBanner();
                    startScrollView();
                } else {
                    tryToHintAd();
                }

                if (!OkHttpUtils.isNetWorkConneted(MyApplication.getContext())){
                    Toast.makeText(context, "网络似乎有点小问题", Toast.LENGTH_SHORT).show();
                }

                break;
            case REFRESH_FAILURE:
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, "网络似乎有点小问题", Toast.LENGTH_SHORT).show();
                }
                break;
            case REFRESH_SUCCEED:
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                break;
            case  GAME_LIST:
                homeListAdapter = new HotList_adapter(
                        new ArrayList<HotRenmenBean.DataBean.GameListBean>(), getActivity());
                homeListView.setAdapter(homeListAdapter);
                if (mHomeListBean.getGame_list() != null) {
                    if (mHomeListBean.getGame_list().size() < 15)
                        homeListView.showNoMoreData();
                    Collections.sort(mHomeListBean.getGame_list(), new HomeListComparator());
                    homeListAdapter.getDataList().addAll(mHomeListBean.getGame_list());
                }
                break;
            case GAME_MORE:
                homeListView.footerView.setVisibility(View.GONE);
                if (mHomeListBean.getGame_list() != null) {
                    if (mHomeListBean.getGame_list().size() < 15)
                        homeListView.showNoMoreData();
                    Collections.sort(mHomeListBean.getGame_list(), new HomeListComparator());
                    homeListAdapter.getDataList().addAll(mHomeListBean.getGame_list());
                }
                break;
            case GAME_NO_MORE:
                homeListView.footerView.setVisibility(View.GONE);
                        homeListView.showNoMoreData();
                break;
            case GAME_ERROR:
                homeListView.footerView.setVisibility(View.GONE);
                homeListView.showLoadError();
                break;
        }
    }

    /***
     * 试图去隐藏轮播图 AD(广告)
     */
    private void tryToHintAd() {
        if (mvViewPager != null) {
            mvViewPager.setVisibility(View.GONE);
        }
    }

    /***
     * 试图去显示轮播图 AD(广告)
     */
    private void tryToShowAD() {
        if (mvViewPager != null) {
            mvViewPager.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View getContentView() {

        return null;
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.homefragment;
    }

    @Override
    public void initView() {
        homeListView = findView(R.id.lv_home);
        iv_gettop = findView(R.id.iv_gettop);
        head1 = LayoutInflater.from(context).inflate(R.layout.hot_head, null);
        mvViewPager = (ViewPager) head1.findViewById(R.id.vp_started_viewpager);
        ll_dots = (LinearLayout) head1.findViewById(R.id.ll_dots);

        mTable = (LinearLayout) head1.findViewById(R.id.home_table);
        mBT = (LinearLayout) head1.findViewById(R.id.home_bt);
        mBreak = (LinearLayout) head1.findViewById(R.id.home_break);
        mGift = (LinearLayout) head1.findViewById(R.id.home_gift);
        mTable.setOnClickListener(this);
        mBT.setOnClickListener(this);
        mBreak.setOnClickListener(this);
        mGift.setOnClickListener(this);

        mRefreshLayout = findView(R.id.home_refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipeRefreshColors));
        mRefreshLayout.setRefreshing(false);
        initHotTvType();
        homeListView.addHeaderView(head1);
    homeListView.setOnLoadingMoreListener(new LoadMoreListView.OnLoadingMoreListener() {
        @Override
        public void onLoadingMore(LoadMoreListView listView, int scrollState) {
            Logger.msg("开始加载更多数据 ", "");
            if (homeListView.currentState != homeListView.STATE_NO_MORE_DATA){
                homeListView.showLoadingMore();
                getHomeList(currentPage + 1);
            }
        }

        @Override
        public void onClickLoadErroView(LoadMoreListView listView) {
        }
    });
        tryToHintAd();
        stateLayout.showContentView();
    }

    Handler abc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentItem = mvViewPager.getCurrentItem();
            mvViewPager.setCurrentItem(currentItem + 1);
        }
    };

    /**
     * 开始轮播图片
     */
    private void startScrollView() {
        if (mvViewPageradapter == null) {
            mvViewPageradapter = new mvViewPagerAdapter();
            mvViewPager.setAdapter(mvViewPageradapter);
            mvViewPager.setCurrentItem( 10000  * listbean.size());
        } else {
            mvViewPageradapter.notifyDataSetChanged();
        }
        // 实现轮播效果
        abc.sendEmptyMessageDelayed(0, 4000);
    }

    /**
     * 显示广告条
     */
    private void showBanner() {
        // 创建ViewPager对应的点
        ll_dots.removeAllViews();
        mDots.clear();
        for (int i = 0; i < listbean.size(); i++) {
            View dot = new View(context);
            int size = DimensionUtil.dip2px(context, 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    size, size);
            params.leftMargin = size;
            if (i == 0) {
                dot.setBackgroundResource(R.drawable.dot_focus);// 默认选择第1个点
            } else {
                dot.setBackgroundResource(R.drawable.dot_normal);
            }
            dot.setLayoutParams(params);
            ll_dots.addView(dot);
            mDots.add(dot);
        }

    }

    // 初始化字体
    private void initHotTvType() {

    }

    @Override
    public void initListener() {

        iv_gettop.setOnClickListener(this);// 快速回到顶部
        // viewpager滑动监听
        mvViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int count = mDots.size();
                for (int i = 0; i < count; i++) {
                    if (position % listbean.size() == i) {
                        mDots.get(i).setBackgroundResource(
                                R.drawable.dot_focus);
                    } else {
                        mDots.get(i).setBackgroundResource(
                                R.drawable.dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                abc.removeCallbacksAndMessages(null);
                abc.sendEmptyMessageDelayed(0, 4000);
            }
        });


//        //条目的点击事件
//        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView,
//                                    View view, int position, long l) {
//                HotRenmenBean.DataBean.GameListBean gameListBean = (HotRenmenBean.DataBean.GameListBean)
//                        adapterView.getItemAtPosition(position);
//                if (gameListBean != null) {
//                    int gameid = gameListBean.getGameid();
//                    enterIntoGameDetails(gameid);
//                }
//            }
//        });
    }


    @Override
    public void initData() {
        getDataLunboImgView();
        getHomeList(1);
    }

    private void getDataLunboImgView() {
        Map<String, String> map = new HashMap<>();
        map.put("hot", 1 + "");
        map.put("cnt", 3 + "");
        map.put("category", 2 + "");
        map.put("page", 1 + "");
        String url = StringUtils
                .getCompUrlFromParams(Constants.SLIDE_LIST, map);
//        Logger.msg("轮播图的信息url", url);
        OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessageDelayed(REFRESH_FAILURE, 4300);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                handler.sendEmptyMessageDelayed(REFRESH_SUCCEED, 4300);
                parlunboImgView(res);
            }
        });
    }

    private LunboImgViewBean lunboImgViewBean;

    private void parlunboImgView(String res) {

        lunboImgViewBean = JsonUtil.getJsonUtil().json2Bean(res,
                LunboImgViewBean.class);

        if (lunboImgViewBean != null) {
            if (lunboImgViewBean.getData() == null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "" + lunboImgViewBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            Message mes = Message.obtain();
            mes.obj = lunboImgViewBean.getData().getList();
            mes.what = PAGER;
            handler.sendMessage(mes);
        }
    }


    @Override
    public CharSequence getTitle() {
        return null;
    }

    /**
     * 本页的点击事件处理
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //开服表
            case R.id.home_table:
                Intent intent = new Intent(context, TableActivity.class);
                startActivity(intent);
                break;
            //变态版
            case R.id.home_bt:
                intents.putExtra("hot", 3); // 热门网游
                intents.setClass(context, HotActivity.class);
                context.startActivity(intents);
                ((Activity) context).overridePendingTransition(R.anim.act_in,
                        R.anim.act_out);
                break;
            //破解版
            case R.id.home_break:
                intents.putExtra("hot", 2); // 热门单机
                intents.setClass(context, HotActivity.class);
                context.startActivity(intents);
                ((Activity) context).overridePendingTransition(R.anim.act_in,
                        R.anim.act_out);
                break;
            //礼包
            case R.id.home_gift:
                ViewPager activityPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
                activityPager.setCurrentItem(3);
                break;

            case R.id.iv_gettop:
                homeListView.setSelection(0);
                break;

            default:
                break;
        }

    }

    public void getHomeList(final int requestPage) {
        Map<String, String> map = new HashMap<>();
        map.put("hot", 1 + "");
        map.put("page", requestPage + "");
        map.put("offset", 15 + "");
        String url = StringUtils.getUrlContainAppid(Constants.URL_GAME_LIST, map);
        Logger.msg("getHomeList: ", url);
        OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.msg("","加载失败");
                handler.sendEmptyMessage(GAME_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
//                File file=new File(context.getCacheDir())
                Logger.msg("", res);
                currentPage = requestPage;
                analysisHomeList(res, currentPage);
            }
        });
    }

    private HotList_adapter homeListAdapter;

    /**
     * 解析json字符串
     *
     * @param res 后台返回的json字符串
     */
    private void analysisHomeList(String res, int currentPage) {

        HotRenmenBean homeList = JsonUtil.getJsonUtil().json2Bean(res, HotRenmenBean.class);
        if (homeList != null) {
            if (currentPage == 1){
                mHomeListBean = homeList.getData();
                if (mHomeListBean != null) {
                    handler.sendEmptyMessage(GAME_LIST);
                }
            }else {
                homeListView.onLoadingMoreComplete();
                mHomeListBean = homeList.getData();
                if (mHomeListBean != null) {
                    handler.sendEmptyMessage(GAME_MORE);
                }
            }
        }else {
            handler.sendEmptyMessage(GAME_NO_MORE);
        }
    }

    /**
     * 进入首页列表的详情
     *
     * @param gameID 游戏的id
     */
    public void enterIntoGameDetails(int gameID) {
        Intent intent3 = new Intent(context, GameDetailActivity.class);
        intent3.putExtra("gameID", gameID);
        context.startActivity(intent3);
        ((Activity) context).overridePendingTransition(R.anim.act_in,
                R.anim.act_out);
    }

    @Override
    public void onRefresh() {
        getDataLunboImgView();
        getHomeList(1);
    }


    /**
     * head1图片轮播适配器
     */
    public class mvViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return listbean == null ? 0 : Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(final ViewGroup container,
                                      final int position) {
            ImageView img = new ImageView(container.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    -1, -1);
            img.setLayoutParams(layoutParams);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            final LunboImgViewBean.DataBean.ListBean listBean = listbean.get(position % listbean.size());
            if (listBean.getImage() == null) {
                ImageCache.scaleLoad(img, R.drawable.banner,
                        container.getContext());
            } else {
                ImgUtil.loadImage(listBean.getImage()
                        ,R.drawable.icon_horizontal,img,Homefragment1.this);
            }
            // 获取gameid 如果是0直接跳转链接，不是0跳转到游戏详情
            // 对轮播图点击监听
            img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String des = listBean.getDes();
                    if (des.equals("")) {
                        return;
                    }
                    enterIntoGameDetails(Integer.parseInt(listBean.getDes()));
                }
            });
            container.addView(img);
            return img;
        }
    }

    /**
     * 移除handle消息，如果在片段退出不移除的话有可能导致JVM无法回收这个对象
     */
    @Override
    public void onDestroy() {
        abc.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public class HomeListComparator implements Comparator<HotRenmenBean.DataBean.GameListBean> {

        @Override
        public int compare(HotRenmenBean.DataBean.GameListBean o1, HotRenmenBean.DataBean.GameListBean o2) {
            return o1.getDowncnt() - o2.getDowncnt() < 0 ? 1 : -1;
        }
    }

}
