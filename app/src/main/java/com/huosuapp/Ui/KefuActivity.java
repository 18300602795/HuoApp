package com.huosuapp.Ui;

import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.text.R;


/**
 * Created by admin on 2016/9/2.
 */
public class KefuActivity extends BaseActivity {
    private TextView tv_title,tv_kefu1,tv_kefu2,tv_kefu3,tv_kefu4;
    private RelativeLayout all_return;
    @Override
    protected void initListener() {
        all_return.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        stateLayout.showContentView();
        tv_title= (TextView) findViewById(R.id.tv_nav);
        tv_kefu1= (TextView) findViewById(R.id.tv_kefu1);
        tv_kefu2= (TextView) findViewById(R.id.tv_kefu2);
        tv_kefu3= (TextView) findViewById(R.id.tv_kefu3);
        tv_kefu4= (TextView) findViewById(R.id.tv_kefu4);
        all_return= (RelativeLayout) findViewById(R.id.all_return);
        tv_title.setText("客服");
    }

    @Override
    public void initData() {
      /*  Map<String, String> map = new HashMap<String, String>();
        map.put("clientid", 1 + "");
        map.put("appid", 1 + "");
        map.put("agent", "");

        OkHttpUtils.postString(Constants.GET_HELP_INFO, false, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String res =response.body().string().trim();
                Logger.msg("获取出来的客服信息",res);
                parjson(res);
            }
        });*/
    }

    private void parjson(String res) {

    }

    @Override
    public int getViewID() {
        return R.layout.kefu_main;
    }

    @Override
    public void parUI(Message mes) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_return:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
            break;
        }
    }
}
