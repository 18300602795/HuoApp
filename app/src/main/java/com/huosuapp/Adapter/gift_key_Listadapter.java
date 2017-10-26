package com.huosuapp.Adapter;

public class gift_key_Listadapter  {
//	private Dialog gift_diDialog;
//	private Context context;
//	List<KeyCodeBean.DataBean.GiftListBean> gift_list;
//	public gift_key_Listadapter(ArrayList<KeyCodeBean.DataBean.GiftListBean> dataList,Context context) {
//		super(dataList);
//		this.context=context;
//	}
//
//	/**
//	 * 暴露一个方法是刷新的
//	 * @return
//	 */
//	public void setDataList(List<KeyCodeBean.DataBean.GiftListBean> listBean){
//		this.gift_list=listBean;
//	}
//	@Override
//	public int getItemLayoutId(int position) {
//
//		return R.layout.gift_key_item;
//	}
//
//	@Override
//	public Object createViewHolder(View convertView, int position) {
//		    ViewHold vh = new ViewHold();
//			vh.iv_game_icon=(ImageView) convertView.findViewById(R.id.iv_game_icon);
//			vh.tv_game_name=(TextView) convertView.findViewById(R.id.tv_game_name);
//			vh.tv_start_time=(TextView) convertView.findViewById(R.id.tv_start_time);
//			vh.tv_copy_code=(TextView) convertView.findViewById(R.id.tv_copy_code);
//			vh.tv_gift_code=(TextView) convertView.findViewById(R.id.tv_gift_code);
//		return vh;
//	}
//
//	@Override
//	public void showData(Object viewHolder, KeyCodeBean.DataBean.GiftListBean data, int position) {
//		ViewHold vh= (ViewHold) viewHolder;
//		//点击了领取按钮
//		//vh.tv_copy_code.setOnClickListener(onItemClickListener);
//		OnclickLingqu(position, vh.tv_copy_code, vh.tv_copy_code);
//
//		Logger.msg("ID是多少=============", data.getGiftid() + "");
//		vh.tv_game_name.setText(data.getGiftname());
//		vh.tv_start_time.setText("起止时间" + data.getStarttime() + " " + data.getEnttime());
//		Logger.msg("全部数量",data.getTotal()+"");
//		if (data.getIcon() != null) {
//			ImgUtil.getBitmapUtils(vh.tv_start_time.getContext()).display(vh.iv_game_icon, data.getIcon());
//		}
//      /*  Logger.msg("getToal",gb.getTotal()+"");
//        Logger.msg("getTRemain",gb.getRemain()+"");*/
//		Logger.msg("更新之后的剩余数量",data.getRemain()+"");
//		Logger.msg("更新之后的总量",data.getTotal()+"");
//		vh.tv_gift_code.setText(data.getRemain() + "");  //显示剩余礼包数量
//	}
//	static class ViewHold{
//		ImageView iv_game_icon;
//		TextView tv_game_name,tv_gift_code,tv_start_time,tv_copy_code;
//	}
//
//
//	/**
//	 * 游戏礼包页面点击的监听器
//	 */
//	public View.OnClickListener onItemClickListener = new View.OnClickListener() {
//		@Override
//		public void onClick(View view) {
//			gift_diDialog = new Dialog(context, R.style.customDialog);
//			View gift_View = LayoutInflater.from(context).inflate(R.layout.gift_code_dialog, null);
//			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Global.dp2px(315, context), ViewGroup.LayoutParams.WRAP_CONTENT);
//			//关闭对话框
//			gift_View.findViewById(R.id.tv_dialog_close).setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					if (gift_diDialog!=null&&gift_diDialog.isShowing()){
//						gift_diDialog.dismiss();;
//					}
//				}
//			});
//			gift_diDialog.setContentView(gift_View,lp);
//			gift_diDialog.show();
//		}
//	};
//	/**
//	 * 游戏礼包页面点击的监听器
//	 */
//	private int i;
//	private void OnclickLingqu(final int position, View v, final TextView tv_code) {
//		v.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				i = position;
//				boolean isLogin = SharePrefUtil.getBoolean(Global.getContext(), SharePrefUtil.KEY.FIRST_LOGIN, true);   //判断用户有没有登录
//				if (isLogin) {
//					// Toast.makeText(this,"请先登录哦",Toast.LENGTH_SHORT).show();
//					//暂时先去到登录界面
//					Intent intent = new Intent(context, LoginActivity.class);
//					context.startActivity(intent);
//					Activity activity = (Activity) context;
//					activity.overridePendingTransition(R.anim.act_in, R.anim.act_out);
//					return;
//				} else {
//					getCodeData(gift_list.get(position).getGiftid(),position, tv_code);
//					Logger.msg("礼包适配器", gift_list.get(position).getGiftid() + "");
//				}
//			}
//		});
//	}
//
//	public void copy(String text, Context context) {
//		//得到剪贴板管理
//		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//		cmb.setText(text);
//		showToast("复制成功,请黏贴到指定地方", 0);
//	}
//
//
//	/**
//	 * 不会一直重复重复重复重复的提醒了
//	 */
//	private Toast toast = null;
//
//	protected void showToast(String msg, int length) {
//		if (toast == null) {
//			toast = Toast.makeText(context, msg, length);
//		} else {
//			toast.setText(msg);
//		}
//		toast.show();
//	}
//
//
//	/**
//	 * 访问网络请求礼包兑换码
//	 */
//	public void getCodeData(long giftid,final int pos,final TextView tv_code) {
//		Logger.msg("礼包ID", giftid + "");
//		Map<String, String> map = new HashMap<String, String>();
//		map.put(Constants.CLIENT_ID, 12 + "");
//		map.put(Constants.APP_ID, 100 + "");
//		map.put(Constants.FROM, 3 + "");
//		map.put("giftid", giftid + "");
//		OkHttpUtils.postGiftCode(Constants.GIFT_ADD, false, map, new Callback() {
//			@Override
//			public void onFailure(Request request, IOException e) {
//
//			}
//
//			@Override
//			public void onResponse(Response response) throws IOException {
//				final String res = response.body().string().trim();
//				Logger.msg("点击领取返回来的信息", res);
//				Logger.msg("获取礼包的长度", res.length() + "");
//				Global.getHandler().post(new Runnable() {
//					@Override
//					public void run() {
//						parCodejson(res, tv_code,pos);
//					}
//				});
//			}
//		});
//	}
//
//	private Gift_Code_Bean_Error gift_code_bean_error;
//	private Gift_Code_Bean_Succeed gift_code_bean_succeed;
//
//	private void parCodejson(final String res, final TextView tv_code1,final int pos) {
//		if (res.length() > 20 && res.length() < 39) {
//			gift_code_bean_error = JsonUtil.getJsonUtil().json2Bean(res, Gift_Code_Bean_Error.class);
//			if (gift_code_bean_error == null) {
//				return;
//			} else {
//				Global.showToast(gift_code_bean_error.getMsg(), 0);
//				Logger.msg("到领取失败", gift_code_bean_error.getMsg());
//			}
//		} else {
//			gift_code_bean_succeed = JsonUtil.getJsonUtil().json2Bean(res, Gift_Code_Bean_Succeed.class);
//			if (gift_code_bean_succeed.getCode() > 300 && gift_code_bean_succeed.getCode() < 350) {
//				Global.showToast(gift_code_bean_succeed.getMsg(), 0);
//				return;
//			}
//			if (gift_code_bean_succeed != null) {
//				gift_diDialog = new Dialog(context, R.style.customDialog);
//				final View gift_View = LayoutInflater.from(context).inflate(R.layout.gift_code_dialog, null);
//				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Global.dp2px(315, context), ViewGroup.LayoutParams.WRAP_CONTENT);
//				TextView tv_code = (TextView) gift_View.findViewById(R.id.tv_gamegift_code);
//				TextView tv_copy = (TextView) gift_View.findViewById(R.id.tv_copy);
//				//获取礼包剩余的数量
//				final long Total = gift_list.get(pos).getTotal();
//				final long remain=gift_list.get(pos).getRemain();
//				Logger.msg("剩余数量11111111",remain+"");
//				Logger.msg("剩余数量22222222",Total+"");
//				if (gift_code_bean_succeed.getData().getGiftcode() != null && gift_code_bean_succeed.getData().getGiftcode() != "") {
//					tv_code.setText(gift_code_bean_succeed.getData().getGiftcode());
//				}
//				final String text = tv_code.getText().toString();
//				if (tv_copy.getText().equals("复制")) {
//					tv_copy.setOnClickListener(new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							copy(text, context);
//							gift_diDialog.dismiss();
//							if(gift_list!=null&&gift_list.size()>0){
//								Logger.msg("剩余数量1",Total+"");
//								Logger.msg("之前的：====>",gift_list.get(pos).getRemain()+"");
//								//gift_code_Listadapter.this.notifyDataSetChanged();
//								notifyDataChanged();
//							}
//						}
//					});
//				}
//				//关闭对话框
//				gift_View.findViewById(R.id.tv_dialog_close).setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						if (gift_diDialog != null && gift_diDialog.isShowing()) {
//							gift_diDialog.dismiss();
//							if(gift_list!=null&&gift_list.size()>0){
//								Logger.msg("剩余数量1",Total+"");
//								Logger.msg("之前的：====>",gift_list.get(pos).getRemain()+"");
//								//gift_code_Listadapter.this.notifyDataSetChanged();
//								notifyDataChanged();
//							}
//						}
//					}
//				});
//				gift_diDialog.setContentView(gift_View, lp);
//				gift_diDialog.show();
//			}
//		}
//	}
//
//	public void notifyDataChanged(){
//		if(context!=null){
//			if (attachTo!=null) {
//				attachTo.getData(3);
//			}
//		}
//	}

}
