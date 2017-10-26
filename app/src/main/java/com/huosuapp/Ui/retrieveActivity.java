package com.huosuapp.Ui;

import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.text.R;


/**
 * 找回密码界面 2016/7/12.
 */
public class retrieveActivity extends BaseActivity {
    private ImageView iv_return;
    private EditText etphone,autocode;
    private Button btn,btncode;//点击了下一步
    private TextView tv;
    private RelativeLayout allreturn;

    @Override
    public void parUI(Message mes) {

    }

    @Override
    public void initUI() {
        stateLayout.showContentView();
        iv_return= (ImageView) findViewById(R.id.iv_nav);
        etphone= (EditText) findViewById(R.id.et_phone);
        autocode= (EditText) findViewById(R.id.et_auth_code);
        allreturn= (RelativeLayout) findViewById(R.id.all_return);
        btncode= (Button) findViewById(R.id.get_auto_code);
        tv= (TextView) findViewById(R.id.tv_nav);
        tv.setText("找回密码");
        btn= (Button) findViewById(R.id.btn_login);


    }

    @Override
    public void initData() {

    }

    @Override
    public int getViewID() {
        return R.layout.retrieve_password;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
          case R.id.all_return:
            finish();
            overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
            break;
            case R.id.get_auto_code:
                //Toast.makeText(this,"点击了获取验证码",Toast.LENGTH_SHORT).show();
                changeBtnGetCode();
                getValidateCode();
                break;
        }
    }


    //定于变量
    private boolean tag = true;
    private int i = 60; //定于获取验证码60秒
    private String valicationCode; //客户端输入的验证码
    private String userName;//输入的手机号（账号）
    /**
     * 验证码改变
     */
    private void changeBtnGetCode() {
        new Thread(){
            @Override
            public void run() {
                if (tag){
                    while (i>0){
                        i--;
                        if (retrieveActivity.this==null){
                            break;
                        }
                        retrieveActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btncode.setText("获取验证码("+i+")");
                                btncode.setClickable(false);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag=false;
                }
                i=60;
                tag=true;
                if (retrieveActivity.this!=null){
                    retrieveActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btncode.setText("重新发送");
                            btncode.setClickable(true);
                        }
                    });
                }
            }
        }.start();
    }


    /**
     * 获取验证码
     *
     */
    public  boolean getValidateCode() {
        String name = etphone.getText().toString().trim();
        String code =btncode.getText().toString().trim();
        if (name.equals("")){
            Toast.makeText(this, "请输入用户名或手机号!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            userName=name;
            valicationCode=code;
            Thread codeThread=new Thread();
        }
        return true;
    }

    @Override
    protected void initListener() {
        btn.setOnClickListener(this);
        allreturn.setOnClickListener(this);
        btncode.setOnClickListener(this);
    }


}
