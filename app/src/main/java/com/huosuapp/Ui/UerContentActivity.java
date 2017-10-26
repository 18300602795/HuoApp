package com.huosuapp.Ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huosuapp.Bean.UserWalletBean;
import com.huosuapp.MyApplication;
import com.huosuapp.Util.CircleImageView;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.Util.StringUtils;
import com.huosuapp.Util.TouXiangCache;
import com.huosuapp.pay.ChargeActivityForWap;
import com.huosuapp.pay.OnPaymentListener;
import com.huosuapp.pay.PaymentCallbackInfo;
import com.huosuapp.pay.PaymentErrorMsg;
import com.huosuapp.text.BuildConfig;
import com.huosuapp.text.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 个人中心页
 */
public class UerContentActivity extends BaseActivity {
	public static final String TAG = UerContentActivity.class.getSimpleName();
	private RelativeLayout all_returns;// 点击界面返回箭头
	private LinearLayout llt_pet_names;// 点击返回键进入到用户信息
	private LinearLayout llt_enter_setting;// 点击进入到设置中心
	private LinearLayout llt_recharge;// 点击了充值
	private LinearLayout llt_giftdetails;// 点击了礼包
	private LinearLayout llt_userinformation;
	private CircleImageView user_imgView;// 个人中心头像
	private TextView tv_nicheng, tv_xingbie, tv_qianming, tv_shengri;
	private TextView tv_nologin;// 默认是显示的，未登陆的

	private Handler handler = new Handler();
	private LinearLayout llt_kefu, llt_mimaxiugai, llt_mibaoyouxiang,
			llt_mibaoshouji;
	private LinearLayout llt_qianbao, llt_chongzhijilu, llt_xiaofeishijian;

	@Override
	public void parUI(Message mes) {

	}

	protected void initListener() {
		llt_pet_names.setOnClickListener(this);
		all_returns.setOnClickListener(this);
		llt_enter_setting.setOnClickListener(this);
		llt_recharge.setOnClickListener(this);
		llt_giftdetails.setOnClickListener(this);


		llt_kefu.setOnClickListener(this);
		llt_mimaxiugai.setOnClickListener(this);
		llt_mibaoyouxiang.setOnClickListener(this);
		llt_mibaoshouji.setOnClickListener(this);
		if(!BuildConfig.useMessage){
			llt_mibaoshouji.setVisibility(View.INVISIBLE);
		}
//		llt_qianbao.setOnClickListener(this);
		llt_chongzhijilu.setOnClickListener(this);
		llt_xiaofeishijian.setOnClickListener(this);
	}

	@Override
	public void initUI() {
		stateLayout.showContentView();
		llt_pet_names = (LinearLayout) findViewById(R.id.llt_pet_name);
		all_returns = (RelativeLayout) findViewById(R.id.all_return);
		llt_enter_setting = (LinearLayout) findViewById(R.id.enter_setting);
		llt_recharge = (LinearLayout) findViewById(R.id.llt_recharge);
		llt_giftdetails = (LinearLayout) findViewById(R.id.llt_giftdetails);
		user_imgView = (CircleImageView) findViewById(R.id.user_circleImageView);
		llt_userinformation = (LinearLayout) findViewById(R.id.llt_userinformation);
		tv_nicheng = (TextView) findViewById(R.id.nicheng);
		tv_xingbie = (TextView) findViewById(R.id.xingbie);
		tv_qianming = (TextView) findViewById(R.id.qianming);
		tv_shengri = (TextView) findViewById(R.id.shengri);
		tv_nologin = (TextView) findViewById(R.id.tv_no_login);

		llt_kefu = (LinearLayout) findViewById(R.id.llt_kefu);
		llt_mimaxiugai = (LinearLayout) findViewById(R.id.llt_mimaxiugai);
		llt_mibaoyouxiang = (LinearLayout) findViewById(R.id.llt_mibaoyouxiang);
		llt_mibaoshouji = (LinearLayout) findViewById(R.id.llt_mibaoshouji);

		llt_qianbao = (LinearLayout) findViewById(R.id.llt_qianbao);

		llt_chongzhijilu = (LinearLayout) findViewById(R.id.llt_chongzhijilu);
		llt_xiaofeishijian = (LinearLayout) findViewById(R.id.llt_xiaofeijilu);
		if(getPackageName().equals("com.yuhui.huosuapp")){
			llt_xiaofeishijian.setVisibility(View.GONE);
		}
	}

	private static final String ATG = "收到homefraament的消息";

	@Override
	public void initData() {
		/*
		 * getImgView(); getUserInformation();
		 */
		getUser();
	}

	/**
	 * 获取头像图片
	 * 
	 * @return
	 */
	public void getImgView() {
		String path = SharePrefUtil.TouXiangDb(this, MyApplication.pic_path);
		if (path != null) { // 根据路径获取照片
			user_imgView.setImageBitmap(TouXiangCache.getphoto(path));
		}
	}

	/**
	 * 获取用户传过来的用户信息
	 * 
	 * @return
	 */
	private void getUserInformation() {
		boolean isSavealter = SharePrefUtil.getBoolean(this,
				SharePrefUtil.KEY.SAVE_ALTER, false);
		if (isSavealter) {
			// UndefinedBean undefinedBean = (UndefinedBean)
			// getIntent().getSerializableExtra("UndefinedBean");
			String nicheng = SharePrefUtil.getString(this,
					SharePrefUtil.KEY.NICHENG, null);
			String xingbie = SharePrefUtil.getString(this,
					SharePrefUtil.KEY.SEX, null);
			String happyday = SharePrefUtil.getString(this,
					SharePrefUtil.KEY.HAPPY_BRITHDAY, null);
			String qianming = SharePrefUtil.getString(this,
					SharePrefUtil.KEY.QIANMING, null);
			if (!TextUtils.isEmpty(nicheng) && !TextUtils.isEmpty(xingbie)
					&& !TextUtils.isEmpty(happyday)
					&& !TextUtils.isEmpty(qianming)) {
				llt_userinformation.setVisibility(View.VISIBLE);
				tv_nicheng.setText("昵称:" + nicheng);
				tv_xingbie.setText("性别:" + xingbie);
				tv_qianming.setText("签名:" + qianming);
				tv_shengri.setText("生日:" + happyday);
				tv_nologin.setVisibility(View.GONE);
			}
		} else {
			return;
		}
	}

	@Override
	public int getViewID() {
		return R.layout.ttv_usercenter;
	}

	/**
	 * 本页的点击事件
	 */
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.all_return:
			System.out.println("点击了返回键");
			finish();
			overridePendingTransition(R.anim.act_back_out_in,
					R.anim.act_back_in_out);
			break;
		case R.id.enter_setting: // 点击进入到设置中心1
			intent.setClass(this, SettingActivity.class);
			System.out.println("点击了用户信息");
			startActivity(intent);
			overridePendingTransition(R.anim.act_in, R.anim.act_out);
			break;
		case R.id.llt_recharge: // 点击充值到充值界面
			ChargeActivityForWap.paymentListener = new OnPaymentListener() {
				@Override
				public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
				/*	Toast.makeText(
							getApplication(),
							"充值金额数：" + callbackInfo.money + " 消息提示："
									+ callbackInfo.msg, Toast.LENGTH_LONG)
							.show();*/
					getUserWallet();
				}

				@Override
				public void paymentError(PaymentErrorMsg errorMsg) {
				/*	Toast.makeText(
							getApplication(),
							"充值失败：code:" + errorMsg.code + "  ErrorMsg:"
									+ errorMsg.msg + "  预充值的金额："
									+ errorMsg.money, Toast.LENGTH_LONG).show();*/
					getUserWallet();
				}
			};
			String url = Constants.BASE+"/float.php/Mobile/Wallet/charge";
			Intent i = new Intent(this, ChargeActivityForWap.class);
			i.putExtra("url", url);
			i.putExtra("title", "支付充值");
			i.putExtra("hs-token", "支付充值");
			i.putExtra("timestamp", "支付充值");
			this.startActivity(i);
			break;

		case R.id.llt_giftdetails: // 点击到礼包界面
			intent.setClass(this, giftActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.act_in, R.anim.act_out);
			break;

		case R.id.llt_kefu: // 进入到客服界面
			EnterHelpActivity(intent, 0);
			break;
		case R.id.llt_mimaxiugai: // 进入到密码修改界面
			EnterHelpActivity(intent, 1);
			break;
		case R.id.llt_mibaoyouxiang: // 进入到密保邮箱界面
			EnterHelpActivity(intent, 2);
			break;
		case R.id.llt_mibaoshouji: // 进入到密保手机
			EnterHelpActivity(intent, 3);
			break;
		case R.id.llt_qianbao: // 钱包
			EnterHelpActivity(intent, 4);
			break;
		case R.id.llt_chongzhijilu: // 充值记录
			EnterHelpActivity(intent, 5);
			break;
		case R.id.llt_xiaofeijilu: // 消费记录
			EnterHelpActivity(intent, 6);
			break;

		default:
			break;
		}
	}

	public void EnterHelpActivity(Intent intent, int type) {
		intent.setClass(this, HelpActivity.class);
		intent.putExtra("help", type);
		startActivity(intent);
		overridePendingTransition(R.anim.act_in, R.anim.act_out);
	}

	@Override
	protected void onStart() {
		super.onStart();
		/*
		 * getImgView(); getUserInformation();
		 */
		getUser();
	}

	/*  *//**
	 * 监听返回键，返回MainActivity
	 */
	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK) { finish(); Intent intent = new
	 * Intent(this,MainActivity.class); startActivity(intent);
	 * overridePendingTransition(R.anim.act_in, R.anim.act_out); return true; }
	 * return true; }
	 */

	/**
	 * 获取保存的用户名
	 */
	public void getUser() {
		String Username = SharePrefUtil.getString(Global.getContext(),
				SharePrefUtil.KEY.NICHENG, "");
		if (!TextUtils.isEmpty(Username)) {
			tv_nologin.setText("账户:"+Username);
		}
		 getUserWallet();
	}

	private void getUserWallet() { // 刷新平台币
		Map<String, String> map = new HashMap<String, String>();
		/*map.put(Constants.CLIENT_ID, 12 + "");
		map.put(Constants.APP_ID, 100 + "");
		map.put("agent", "");
		map.put("from", 3 + "");*/
		String url = StringUtils.getCompUrlFromParams(
				Constants.URL_USER_WALLET, map);
		Logger.msg("获取钱包余额url", url);
		OkHttpUtils.getString(url, MyApplication.isCache, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String res = response.body().string().trim();
				Logger.msg("获取钱包余额", res);
				Log.e("获取钱包余额", "onResponse: "+res );
				handler.post(new Runnable() {

					@Override
					public void run() {
						UserWalletBean bean = JsonUtil.getJsonUtil().json2Bean(
								res, UserWalletBean.class);
						if(bean!=null){
							if (bean.getCode() == 200) {
								String money = bean.getData().getRemain();
								TextView wallet_num = (TextView) findViewById(R.id.wallet_num);
								wallet_num.setText(money);
							}
						}
					}
				});
			}
		});

	}
}
