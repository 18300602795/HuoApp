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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.Bean.GiftCodyBean;
import com.huosuapp.Bean.Gift_Code_Bean_Error;
import com.huosuapp.Bean.Gift_Code_Bean_Succeed;
import com.huosuapp.Fragment.gift_cody_fragment;
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

public class gift_code_Listadapter extends BaseAdapter {
    private Dialog gift_diDialog;
    private Context context;
    private List<GiftCodyBean.DataBean.GiftListBean> gift_list;


    private gift_cody_fragment attachTo;

    public gift_code_Listadapter(List<GiftCodyBean.DataBean.GiftListBean> gift_list
            , Context context, gift_cody_fragment fragment) {
        this.gift_list = gift_list;
        this.context = context;
        this.attachTo = fragment;
    }


    /**
     * 如果原来没有数据，则返回一个空集合，如果有则返回原来的有数据的集合
     */
    public List<GiftCodyBean.DataBean.GiftListBean> getDataList() {
        if (gift_list == null) {
            gift_list = new ArrayList<GiftCodyBean.DataBean.GiftListBean>();
        }
        return gift_list;
    }


    /**
     * 暴露一个方法是刷新的
     *
     * @return
     */
    public void setDataList(List<GiftCodyBean.DataBean.GiftListBean> listBean) {
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
            convertView = View.inflate(parent.getContext(), R.layout.gift_code_item, null);
            vh.iv_game_icon = (ImageView) convertView.findViewById(R.id.iv_game_icon);
            vh.tv_game_name = (TextView) convertView.findViewById(R.id.tv_game_name);
            vh.gift_list_take = (Button) convertView.findViewById(R.id.gift_list_take);
            vh.tv_game_code = (TextView) convertView.findViewById(R.id.tv_gift_code);
            vh.ll_code_show = (LinearLayout) convertView.findViewById(R.id.ll_code_show);
            vh.mProgressBar= (ProgressBar) convertView.findViewById(R.id.gift_list_progress);
            vh.progress_text= (TextView) convertView.findViewById(R.id.gift_list_progress_text);
            convertView.setTag(vh);
        }
        ViewHold vh = (ViewHold) convertView.getTag();
        //点击了领取按钮
        OnclickLingqu(position, vh.gift_list_take, vh.tv_game_code);

        GiftCodyBean.DataBean.GiftListBean gb = gift_list.get(position);
        vh.tv_game_name.setText(gb.getGiftname());
        if (gb.getIcon() != null) {
            ImgUtil.loadImage(gb.getIcon(),R.drawable.ic_launcher
                    ,vh.iv_game_icon,attachTo);
        }

        vh.tv_game_code.setText("礼包数量："+gb.getTotal());  //显示剩余礼包数量
        float i = (float) gb.getRemain() / (float) gb.getTotal() * 100;
        vh.mProgressBar.setProgress((int) (i + 0.5));
        vh.progress_text.setText("(剩余" + (int) (i + 0.5) + "%)");
        return convertView;
    }

    /**
     * 游戏礼包页面点击的监听器
     */

    private void OnclickLingqu(final int position, View v, final TextView tv_code) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = SharePrefUtil.getBoolean(Global.getContext(), SharePrefUtil.KEY.FIRST_LOGIN, true);   //判断用户有没有登录
                if (isLogin) {
                    // Toast.makeText(this,"请先登录哦",Toast.LENGTH_SHORT).show();
                    //暂时先去到登录界面
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    Activity activity = (Activity) context;
                    activity.overridePendingTransition(R.anim.act_in, R.anim.act_out);
                    return;
                } else {
                    getCodeData(gift_list.get(position).getGiftid(), position, tv_code);
                }
            }
        });
    }


    static class ViewHold {
        ImageView iv_game_icon;
        Button gift_list_take;
//        TextView tv_gift_detail;
        ProgressBar mProgressBar;
        TextView tv_game_name, tv_game_code,progress_text;
        LinearLayout ll_code_show;
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
        Map<String, String> map = new HashMap<String, String>();
        map.put("giftid", giftid + "");
        OkHttpUtils.postString(Constants.GIFT_ADD, false, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string().trim();
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
            Logger.msg("到领取失败", gift_code_bean_error.getMsg());
        }
        gift_code_bean_succeed = JsonUtil.getJsonUtil().json2Bean(res, Gift_Code_Bean_Succeed.class);
        if (gift_code_bean_succeed == null) {
            return;
        }
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
//            Window window = gift_diDialog.getWindow();
//            window.setWindowAnimations(R.style.gift_dialog_anim);
//            gift_diDialog.set
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

