package com.huosuapp.Fragment;

import android.os.Bundle;

import com.huosuapp.base.LazyFragment;
import com.huosuapp.text.R;

/**
 * Created by liu hong liang on 2016/9/18.
 */
public class RecommandFragment extends LazyFragment{
//    PtrClassicFrameLayout advRefresh;
//    RecyclerView recyclerView;
//    BaseRefreshLayout baseRefreshLayout;
//    List<GameBeanNew> datas=new ArrayList<>();
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_recommand);
//        advRefresh = (PtrClassicFrameLayout) findViewById(R.id.advRefresh);
//        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        baseRefreshLayout=new RefreshUltraLayout(advRefresh);
//        baseRefreshLayout.setAdvRefreshListener(new AdvRefreshListener(){
//
//            @Override
//            public void getPageData(int i) {
//
//            }
//        });
//        baseRefreshLayout.setAdapter();
    }
}
