package com.huosuapp.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.Bean.Gift_Code_Bean_Error;
import com.huosuapp.Bean.Gift_Code_Bean_Succeed;
import com.huosuapp.Bean.KeyCodeBean;
import com.huosuapp.Fragment.key_cody_fragment;
import com.huosuapp.Ui.LoginActivity;
import com.huosuapp.Util.Constants;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.ImgUtil;
import com.huosuapp.Util.JsonUtil;
import com.huosuapp.Util.Logger;
import com.huosuapp.Util.OkHttpUtils;
import com.huosuapp.Util.SharePrefUtil;
import com.huosuapp.text.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class KeyCodeListadapter extends BaseAdapter {
    private Dialog gift_diDialog;
    private Context context;
    private List<KeyCodeBean.DataBean.GiftListBean> gift_list;
    private int i;

    private key_cody_fragment attachTo;

    public KeyCodeListadapter(List<KeyCodeBean.DataBean.GiftListBean> gift_list, Context context, key_cody_fragment fragment) {
        this.gift_list = gift_list;
        this.context = context;
        this.attachTo = fragment;
    }


    /**
     * 如果原来没有数据，则返回一个空集合，如果有则返回原来的有数据的集合
     */
    public List<KeyCodeBean.DataBean.GiftListBean> getDataList() {
        if (gift_list == null) {
            gift_list = new ArrayList<KeyCodeBean.DataBean.GiftListBean>();
        }
        return gift_list;
    }


    /**
     * 暴露一个方法是刷新的
     * @return
     */
    public void setDataList(List<KeyCodeBean.DataBean.GiftListBean> listBean) {
        this.gift_list = listBean;
    }

    @Override
    public int getCount() {
        return gift_list == null ? 0 : gift_list.size();
    }

    @Override
    public Object getItem(int position) {
        return gift_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            ViewHold vh = new ViewHold();
            convertView = View.inflate(parent.getContext(), R.layout.gift_key_item, null);
            vh.iv_game_icon = (ImageView) convertView.findViewById(R.id.iv_game_icon);
            vh.tv_game_name = (TextView) convertView.findViewById(R.id.tv_game_name);
            vh.tv_start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
            vh.tv_copy_code = (TextView) convertView.findViewById(R.id.tv_copy_code);
            vh.tv_gift_code = (TextView) convertView.findViewById(R.id.tv_gift_code);

            convertView.setTag(vh);
        }
        ViewHold vh = (ViewHold) convertView.getTag();
        vh.tv_copy_code.setText("领取");
        //点击了领取按钮
        //vh.tv_copy_code.setOnClickListener(onItemClickListener);
        OnclickLingqu(position, vh.tv_copy_code, vh.tv_gift_code);

        KeyCodeBean.DataBean.GiftListBean gb = gift_list.get(position);
        vh.tv_game_name.setText(gb.getGiftname());
        vh.tv_start_time.setText("起止时间" + gb.getStarttime() + " " + gb.getEnttime());
        Logger.msg("全部数量", gb.getTotal() + "");
        if (gb.getIcon() != null) {
//            ImgUtil.getBitmapUtils(parent.getContext()).display(vh.iv_game_icon, gb.getIcon());
            ImgUtil.loadImage(gb.getIcon(),vh.iv_game_icon);
        }
        Logger.msg("更新之后的剩余数量", gb.getRemain() + "");
        Logger.msg("更新之后的总量", gb.getTotal() + "");
        vh.tv_gift_code.setText(gb.getRemain() + "");  //显示剩余礼包数量
        return convertView;
    }

    /**
     * 游戏礼包页面点击的监听器
     */

    private void OnclickLingqu(final int position, View v, final TextView tv_code) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = position;
                boolean isLogin = SharePrefUtil.getBoolean(Global.getContext(), SharePrefUtil.KEY.FIRST_LOGIN, true);   //判断用户有没有登录
                if (isLogin) {
                    // Toast.makeText(this,"请先登录哦",Toast.LENGTH_SHORT).show();
                    //暂时先去到登录界面
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    Activity activity = (Activity) context;
                    activity.overridePendingTransition(R.anim.act_in, R.anim.act_out);
                } else {
                    getCodeData(gift_list.get(position).getGiftid(), position, tv_code);
                    Logger.msg("礼包适配器", gift_list.get(position).getGiftid() + "");
                }
            }
        });
    }


    static class ViewHold {
        ImageView iv_game_icon;
        TextView tv_game_name, tv_gift_code, tv_start_time, tv_copy_code;
    }


    /**
     * 得到剪贴板管理器
     */
    public void copy(String text, Context context) {
        //得到剪贴板管理
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(text);
        showToast("复制成功,请黏贴到指定地方", 0);
    }


    /**
     * 不会一直重复重复重复重复的提醒了
     */
    private Toast toast = null;

    protected void showToast(String msg, int length) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, length);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }


    /**
     * 访问网络请求礼包兑换码
     */
    public void getCodeData(long giftid, final int pos, final TextView tv_code) {
//        Logger.msg("礼包ID", giftid + "");
        Map<String, String> map = new HashMap<>();
        map.put("giftid", giftid + "");
        OkHttpUtils.postString(Constants.CDKEY_ADD, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string().trim();
//                Logger.msg("点击领取返回来的信息", res);
                Global.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        parCodejson(res, tv_code, pos);
                    }
                });
            }
        });
    }

    private Gift_Code_Bean_Error gift_code_bean_error;
    private Gift_Code_Bean_Succeed gift_code_bean_succeed;

    private void parCodejson(final String res, final TextView tv_code1, final int pos) {
        gift_code_bean_error = JsonUtil.getJsonUtil().json2Bean(res, Gift_Code_Bean_Error.class);
        if (gift_code_bean_error == null) {
            return;
        } else if ("300".compareTo(gift_code_bean_error.getCode())<=0) {
            Global.showToast(gift_code_bean_error.getMsg(), 0);
//            Logger.msg("到领取失败", gift_code_bean_error.getMsg());
        }
        gift_code_bean_succeed = JsonUtil.getJsonUtil().json2Bean(res, Gift_Code_Bean_Succeed.class);
        if (gift_code_bean_succeed.getCode() > 300 && gift_code_bean_succeed.getCode() < 350) {
            Global.showToast(gift_code_bean_succeed.getMsg(), 0);
            return;
        }
        if (gift_code_bean_succeed != null) {
            gift_diDialog = new Dialog(context, R.style.customDialog);
            final View gift_View = LayoutInflater.from(context).inflate(R.layout.gift_code_dialog, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Global.dp2px(315, context), ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView tv_code = (TextView) gift_View.findViewById(R.id.tv_gamegift_code);
            TextView tv_copy = (TextView) gift_View.findViewById(R.id.tv_copy);
            //获取礼包剩余的数量
            final long Total = gift_list.get(pos).getTotal();
            final long remain = gift_list.get(pos).getRemain();
            if (gift_code_bean_succeed != null && gift_code_bean_succeed.getData() != null && gift_code_bean_succeed.getData().getGiftcode() != null && gift_code_bean_succeed.getData().getGiftcode() != "") {
                tv_code.setText(gift_code_bean_succeed.getData().getGiftcode());
            }
            final String text = tv_code.getText().toString();
            if (tv_copy.getText().equals("复制")) {
                tv_copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copy(text, context);
                        gift_diDialog.dismiss();
                        if (gift_list != null && gift_list.size() > 0) {
                            notifyDataChanged();
                        }
                    }
                });
            }
            //关闭对话框
            gift_View.findViewById(R.id.tv_dialog_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (gift_diDialog != null && gift_diDialog.isShowing()) {
                        gift_diDialog.dismiss();
                        if (gift_list != null && gift_list.size() > 0) {
                            notifyDataChanged();
                        }
                    }
                }
            });
            gift_diDialog.setContentView(gift_View, lp);
            gift_diDialog.show();
        }
    }
    public void notifyDataChanged() {
        if (context != null) {
            if (attachTo != null) {
                attachTo.getCodData(1);
            }
        }
    }
}

