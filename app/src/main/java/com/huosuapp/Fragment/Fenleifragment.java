package com.huosuapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.huosuapp.Adapter.Fenleiadapter;
import com.huosuapp.Bean.FenleiBean;
import com.huosuapp.MyApplication;
import com.huosuapp.Ui.FenleiGameDetailsActivity;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.text.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 分类界面的fragment
 */
public class Fenleifragment extends Basefragment {
    private GridView fenGridView;
    private Fenleiadapter mstandlistadapter;
    private ArrayList<String> list = new ArrayList<String>();

    @Override
    public void parUI(Message mes) {
        if (mes.what == 10) {
            if (mstandlistadapter == null) {
                data = (List<FenleiBean.DataBean>) mes.obj;
                if (data==null){
                    stateLayout.showFailView();
                }
                stateLayout.showContentView();
                mstandlistadapter = new Fenleiadapter(data);
                fenGridView.setAdapter(mstandlistadapter);
            }
        }
    }


    @Override
    public View getContentView() {

        return null;
    }


    @Override
    public int getContentViewLayoutId() {

        return R.layout.standfragment;
    }

    @Override
    public void initView() {
        fenGridView = findView(R.id.gv_fenlei);
        fenGridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); //去掉点击出现黄色背景效果
        stateLayout.showContentView();
    }
    @Override
    public void initListener() {
        fenGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //进入分类游戏列表
                FenleiBean.DataBean data1 = (FenleiBean.DataBean) adapterView.getItemAtPosition(position);
                enterFenleiDetails(data1);

            }
        });
    }

    //进入分类游戏列表
    private void enterFenleiDetails(FenleiBean.DataBean dataBean) {
        Intent intent = new Intent(context, FenleiGameDetailsActivity.class);
        intent.putExtra("textTile", dataBean.getTypename());
        intent.putExtra("typeid", dataBean.getTypeid());
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.act_in, R.anim.act_out);

    }


    @Override
    public void initData() {
        //获取分类数据列表
        getData();
    }

    private String SSS = "Fenleifragment";

    private void getData() {
        Map<String, String> map = new HashMap<String, String>();
       /* map.put(Constants.CLIENT_ID, 12 + "");
        map.put(Constants.APP_ID, 100 + "");
        map.put("agent", "");
        map.put(Constants.FROM,3+"");*/
        String url = StringUtils.getCompUrlFromParams(Constants.TYPE_LIST,map);
        OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string().trim();
                Logger.msg(SSS, res + "");
                parjson(res);
            }
        });
    }

    private List<FenleiBean.DataBean> data;

    private void parjson(String res) {
        if (res.length()>28&&res.length()<50){
            return;
        }
        FenleiBean homeBeanList=JsonUtil.getJsonUtil().json2Bean(res,FenleiBean.class);
        if (homeBeanList==null){
            return;
        } else {
            data = homeBeanList.getData();
            Message mes = Message.obtain();
            mes.what = 10;
            mes.obj = data;
            handler.sendMessage(mes);
        }

    }


    @Override
    public CharSequence getTitle() {

        return null;
    }

}
