package com.huosuapp.Ui;

import android.app.Dialog;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.Util.Global;
import com.huosuapp.text.R;

/**
 * 礼包详情页
 */
public class GiftDetailsActivity extends BaseActivity {
    private RelativeLayout rlt_allreturn;
    private TextView tv_title;
    private Button btn_lingqu;

    @Override
    public void parUI(Message mes) {

    }

    @Override
    protected void initListener() {
        rlt_allreturn.setOnClickListener(this);
        tv_title.setOnClickListener(this);
        btn_lingqu.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        stateLayout.showContentView();
        rlt_allreturn= (RelativeLayout) findViewById(R.id.all_return);
        tv_title= (TextView) findViewById(R.id.tv_nav);
        btn_lingqu= (Button) findViewById(R.id.btn_lingqu);
        tv_title.setText("王者荣耀");
    }

    @Override
    public void initData() {

    }

    @Override
    public int getViewID() {
        return R.layout.giftdetails_main;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.all_return:
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
            case R.id.btn_lingqu: //点击了领取
                onCreateDialog();
                break;
        }
    }

    //弹出领取礼包对话框
    private Dialog gift_diDialog;
    private void onCreateDialog() {
        gift_diDialog = new Dialog(this, R.style.customDialog);
        View gift_View = LayoutInflater.from(this).inflate(R.layout.gift_code_dialog, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Global.dp2px(315, this), ViewGroup.LayoutParams.WRAP_CONTENT);
        //关闭对话框
        gift_View.findViewById(R.id.tv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gift_diDialog!=null&&gift_diDialog.isShowing()){
                    gift_diDialog.dismiss();;
                }
            }
        });
        gift_diDialog.setContentView(gift_View,lp);
        gift_diDialog.show();
    }
}
