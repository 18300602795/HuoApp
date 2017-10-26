package com.huosuapp.Ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.Bean.UndefinedBean;
import com.huosuapp.MyApplication;
import com.huosuapp.PopWindow.SelectPopupWindow;
import com.huosuapp.Util.CircleImageView;
import com.huosuapp.Util.QieYuanTu;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.Util.TouXiangCache;
import com.huosuapp.text.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 用户信息页
 */
public class UndefinedActivity extends BaseActivity {
    private final static int CODE_CAMERA_REQUEST = 10;
    private final static int CODE_GALLERY_REQUEST = 11;
    private static final int CODE_RESULT_REQUEST = 12;
    private TextView mTV, tv_sex, tv_happybriday,tv_nicheng, tv_auto;;
    private LinearLayout paizhao;//点击了拍照
    private Dialog myDialog; ///弹出选择头像对话框
    private SelectPopupWindow SexPopupWindow; //性别组popWindow
    private View rootView; //本页的跟布局
    private LinearLayout llt_sex, lly_happybriday, llt_petname, llt_auto;//性别组，生日组，昵称,签名
    private RelativeLayout all_returns;//点击了返回键
    private CircleImageView user_img;
    private String AAA;
    private Bitmap bp;
    private Button btn_save_alter;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "head_image.jpg";
    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 100;
    private static int output_Y = 100;

    @Override
    public int getViewID() {
        return R.layout.undefined;
    }


    @Override
    protected void initListener() {
        llt_sex.setOnClickListener(this);
        paizhao.setOnClickListener(this);
        all_returns.setOnClickListener(this);
        lly_happybriday.setOnClickListener(this);
        llt_petname.setOnClickListener(this);
        llt_auto.setOnClickListener(this);
        btn_save_alter.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        stateLayout.showContentView();
        paizhao = (LinearLayout) findViewById(R.id.paizhao);
        llt_sex = (LinearLayout) findViewById(R.id.llt_sex);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        //tv_auto1= (TextView) findViewById(R.id.tv_auto);
        llt_petname = (LinearLayout) findViewById(R.id.llt_pet_name);
        llt_auto = (LinearLayout) findViewById(R.id.llt_auto);
        tv_happybriday = (TextView) findViewById(R.id.tv_happybriday);
        lly_happybriday = (LinearLayout) findViewById(R.id.llt_happybriday);
        all_returns = (RelativeLayout) findViewById(R.id.all_return);
        user_img = (CircleImageView) findViewById(R.id.user_photo);
        btn_save_alter = (Button) findViewById(R.id.btn_save_alter);
        mTV = (TextView) findViewById(R.id.tv_nav);
        tv_auto = (TextView) findViewById(R.id.tv_auto);
        tv_nicheng = (TextView) findViewById(R.id.nicheng);
        mTV.setText("用户信息");
    }

    @Override
    public void initData() {
        String path = SharePrefUtil.TouXiangDb(this, MyApplication.pic_path);
        if (path != null) {
            //根据路径找图片
            user_img.setImageBitmap(TouXiangCache.getphoto(path));
        }
        getUserInformation();
    }


    @Override
    public void parUI(Message mes) {

    }

    /**
     * 本页的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_return: //点击了返回键
                finish();
                overridePendingTransition(R.anim.act_back_out_in, R.anim.act_back_in_out);
                break;
            case R.id.paizhao: //点击了头像弹出对话框
                showDialog_header();
                break;
            case R.id.llt_paizhao: //点击了拍照
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                choseHeadImageFromCameraCapture();
                break;
            case R.id.llt_bendi_photo: //点击了从手机相册导入头像
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                //允许用户选择特殊种类的数据，并返回（特殊种类的数据：照一张相片或录一段音）
                choseHeadImageFromGallery();
                break;
            case R.id.llt_sex:  //点击了性别选择组
                System.out.println("点击了性别组按钮");
                showpopWindow_sex();
                break;
            case R.id.llt_man:
                tv_sex.setText("男");
                myDialog.dismiss();
                break;
            case R.id.llt_woman:
                tv_sex.setText("女");
                myDialog.dismiss();
                break;
            case R.id.tv_cancel:
                myDialog.dismiss();
                break;
            case R.id.llt_happybriday:
                showHappyBridayDialog();
                break;
            case R.id.llt_pet_name:
                onCreateDialog1(2);
                break;
            case R.id.llt_auto:
                onCreateDialog1(1);
                break;
            case R.id.btn_save_alter:  //点击了保存修改
                saveUserInformation();
                break;
            default:
                break;
        }

    }

    /**
     * 点击保存修改走的方法
     */
    private void saveUserInformation() {
        UndefinedBean user = new UndefinedBean();
        if (TextUtils.isEmpty(tv_nicheng.getText().toString().trim())||TextUtils.isEmpty(tv_sex.getText().toString().trim())
                ||TextUtils.isEmpty(tv_happybriday.getText().toString().trim())
                ||TextUtils.isEmpty(tv_auto.getText().toString().trim())
                ){
            Toast.makeText(this,"请您完善个人信息哦！",Toast.LENGTH_LONG).show();
            return;
        }
       if (!TextUtils.isEmpty(tv_nicheng.getText().toString().trim())) {
            user.pet_name = tv_nicheng.getText().toString().trim();
        }
        if (!TextUtils.isEmpty(tv_sex.getText().toString().trim())) {
            user.sex = tv_sex.getText().toString().trim();
        }
        if(!TextUtils.isEmpty(tv_happybriday.getText().toString().trim())){
            user.happy_brithday=tv_happybriday.getText().toString().trim();
        }
       if(!TextUtils.isEmpty(tv_auto.getText().toString().trim())){
            user.signature=tv_auto.getText().toString().trim();
        }
        SharePrefUtil.saveBoolean(this,SharePrefUtil.KEY.SAVE_ALTER,true);
        SharePrefUtil.saveString(this,SharePrefUtil.KEY.NICHENG,user.pet_name);
        SharePrefUtil.saveString(this,SharePrefUtil.KEY.SEX,user.sex);
        SharePrefUtil.saveString(this,SharePrefUtil.KEY.HAPPY_BRITHDAY,user.happy_brithday);
        SharePrefUtil.saveString(this,SharePrefUtil.KEY.QIANMING,user.signature);
        finish();
      /*  Intent intent =new Intent(this,UerContentActivity.class);
        Bundle bundle =new Bundle();
        bundle.putSerializable("UndefinedBean",user);
        intent.putExtras(bundle);
        startActivity(intent);*/
    }


    private EditText edt_reply;
    private Button btn_reply;

    //弹出呢称，或者签名输入框
    protected Dialog onCreateDialog1(final int position) {

        final Dialog customDialog = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_comment, null);
        edt_reply = (EditText) mView.findViewById(R.id.edt_comments);
        btn_reply = (Button) mView.findViewById(R.id.btn_send);
        Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(mView);
        customDialog.show();

        //当editText获取焦点的时候,调用显示键盘命令
        edt_reply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                customDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });

        btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 1) {
                    tv_auto.setText(edt_reply.getText().toString().trim());
                }
                if (position == 2) {
                    tv_nicheng.setText(edt_reply.getText().toString().trim());
                }
                customDialog.dismiss();
            }
        });
        return customDialog;
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intent = new Intent();
        // 设置文件类型
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }


    /**
     * 显示生日列表选择对话框
     */
    private Calendar cal;
    private SimpleDateFormat df;

    private void showHappyBridayDialog() {
        cal = Calendar.getInstance();
        df = new SimpleDateFormat();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                listener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setTitle("生日");
        datePickerDialog.show();

    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() { //

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

            cal.set(Calendar.YEAR, arg1);

            cal.set(Calendar.MONTH, arg2);

            cal.set(Calendar.DAY_OF_MONTH, arg3);

            updateDate();

        }

    };

    private void updateDate() {
        df = new SimpleDateFormat("yyyy-MM-dd");
        tv_happybriday.setText(df.format(cal.getTime()));

    }

    /**
     * 显示性别选择组popWindow
     */
    private void showpopWindow_sex() {
        LinearLayout dialogRoot = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ttv_dialog_sex, null);
        dialogRoot.findViewById(R.id.llt_man).setOnClickListener(this);
        dialogRoot.findViewById(R.id.llt_woman).setOnClickListener(this);
        myDialog = new Dialog(this, R.style.customDialog);
        myDialog.setContentView(dialogRoot);
        Window diaWindow = myDialog.getWindow();
        diaWindow.setGravity(Gravity.CENTER);
        myDialog.show();
    }

    /**
     * 显示相册选择dialog
     */
    private void showDialog_header() {
        LinearLayout dialogRoot = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ttv_dialog_head, null);
        dialogRoot.findViewById(R.id.llt_paizhao).setOnClickListener(this);
        dialogRoot.findViewById(R.id.llt_bendi_photo).setOnClickListener(this);
        myDialog = new Dialog(this, R.style.customDialog);
        myDialog.setContentView(dialogRoot);
        Window diaWindow = myDialog.getWindow();
        diaWindow.setGravity(Gravity.CENTER);
        myDialog.show();
    }

    /**
     * 处理头像选择逻辑
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_CANCELED) {
            Toast.makeText(this, "你取消选择了", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempfile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempfile));
                } else {
                    Toast.makeText(this, "没有SD卡存在哦！", Toast.LENGTH_SHORT).show();
                }
            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }
                break;
        }
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            final Bitmap photo = extras.getParcelable("data");
            if (photo != null) {
                bp = photo;
                Bitmap roundedCornerBitmap = QieYuanTu.toRoundBitmap(bp);
                TouXiangCache.saveMyBitmap(roundedCornerBitmap, MyApplication.pic_path);
                String touxiang = SharePrefUtil.TouXiangDb(this, MyApplication.pic_path);
                if (touxiang == null) {
                    SharePrefUtil.saveString(this, "pic_path", MyApplication.pic_path);
                }
                Log.e("---------------->", "--------roundedCornerBitmap---------->" + MyApplication.pic_path);
                QieYuanTu.setRoundConner(user_img, roundedCornerBitmap);
            }
        }
    }

    /**
     * 裁剪原始的图片
     */
    private void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从SP获取用户信息
     */
    private void getUserInformation() {
        boolean isSavealter = SharePrefUtil.getBoolean(this, SharePrefUtil.KEY.SAVE_ALTER, false);
        if (isSavealter) {
            //UndefinedBean undefinedBean = (UndefinedBean) getIntent().getSerializableExtra("UndefinedBean");
            String nicheng=SharePrefUtil.getString(this,SharePrefUtil.KEY.NICHENG,null);
            String xingbie= SharePrefUtil.getString(this,SharePrefUtil.KEY.SEX,null);
            String happyday=SharePrefUtil.getString(this,SharePrefUtil.KEY.HAPPY_BRITHDAY,null);
            String qianming=SharePrefUtil.getString(this,SharePrefUtil.KEY.QIANMING,null);
            if (!TextUtils.isEmpty(nicheng)&&!TextUtils.isEmpty(xingbie)&&
                    !TextUtils.isEmpty(happyday)&&!TextUtils.isEmpty(qianming)
                    ) {
               // llt_userinformation.setVisibility(View.VISIBLE);
                tv_nicheng.setText(nicheng);
                tv_sex.setText(xingbie);
                tv_auto.setText(qianming);
                tv_happybriday.setText(happyday);
                //tv_nologin.setVisibility(View.GONE);
            }
        }else{
            return;
        }
    }
}
