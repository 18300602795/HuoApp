package com.huosuapp.Adapter;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.Bean.UserGiftCodeBean;
import com.huosuapp.Util.ImgUtil;
import com.huosuapp.text.R;

import java.util.ArrayList;
import java.util.List;

public class gift_Usercode_Listadapter extends BaseAdapter {
//    private int[] drawbles = {R.drawable.gift1, R.drawable.gift2, R.drawable.gift3};
    private Dialog gift_diDialog;
    private Context context;
    private List<UserGiftCodeBean.DataBean.GiftListBean> gift_list;
    public gift_Usercode_Listadapter(List<UserGiftCodeBean.DataBean.GiftListBean> gift_list, Context context) {
        this.gift_list = gift_list;
        this.context = context;
    }


    /**
     * 如果原来没有数据，则返回一个空集合，如果有则返回原来的有数据的集合
     */
    public List<UserGiftCodeBean.DataBean.GiftListBean> getDataList() {
        if (gift_list == null) {
            gift_list = new ArrayList<UserGiftCodeBean.DataBean.GiftListBean>();
        }
        return gift_list;
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
            convertView = View.inflate(parent.getContext(), R.layout.user_gift_code_item, null);
            vh.iv_game_icon = (ImageView) convertView.findViewById(R.id.iv_game_icon);
            vh.tv_game_name = (TextView) convertView.findViewById(R.id.tv_game_name);
            vh.tv_gift_detail = (TextView) convertView.findViewById(R.id.tv_gift_detail);
            vh.tv_start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
            vh.tv_copy_code = (TextView) convertView.findViewById(R.id.tv_copy_code);
            vh.tv_game_code = (TextView) convertView.findViewById(R.id.tv_gift_code);
            vh.ll_code_show = (LinearLayout) convertView.findViewById(R.id.ll_code_show);
            vh.tv_game_user_code = (TextView) convertView.findViewById(R.id.tv_gamegift_code);
            convertView.setTag(vh);
        }

        final ViewHold vh = (ViewHold) convertView.getTag();
        final UserGiftCodeBean.DataBean.GiftListBean gb = gift_list.get(position);
        if (gb.getCode() != null && gb.getCode() != "") {
            vh.tv_game_user_code.setText(gb.getCode());
        }

        vh.tv_game_name.setText(gb.getGiftname());
        vh.tv_start_time.setText("起止时间" + gb.getStarttime() + " " + gb.getEnttime());
        if (gb.getIcon() != null) {
//            ImgUtil.getBitmapUtils(parent.getContext()).display(vh.iv_game_icon, gb.getIcon());
            ImgUtil.loadImage(gb.getIcon() + "",vh.iv_game_icon);
        }
        vh.tv_game_code.setText(gb.getRemain() + "");
        vh.tv_gift_detail.setText(gb.getContent());
        vh.tv_copy_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  gift_diDialog = new Dialog(context, R.style.customDialog);
                final View gift_View = LayoutInflater.from(context).inflate(R.layout.gift_code_dialog, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Global.dp2px(315, context), ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tv_code = (TextView) gift_View.findViewById(R.id.tv_gamegift_code);
                tv_code.setText(gb.getContent());
                TextView tv_copy = (TextView) gift_View.findViewById(R.id.tv_copy);
                final String text = tv_code.getText().toString();
                if (tv_copy.getText().equals("复制")) {
                    tv_copy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            copy(text, context);
                            gift_diDialog.dismiss();
                        }
                    });
                }
                //关闭对话框
                gift_View.findViewById(R.id.tv_dialog_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (gift_diDialog != null && gift_diDialog.isShowing()) {
                            gift_diDialog.dismiss();
                        }
                    }
                });
                gift_diDialog.setContentView(gift_View, lp);
                gift_diDialog.show();*/

                String giftcode = vh.tv_game_user_code.getText().toString().trim();
                copy(giftcode, context);
            }
        });

        return convertView;
    }

    /**
     * 游戏礼包页面点击的监听器
     */

    static class ViewHold {
        ImageView iv_game_icon;
        TextView tv_game_name, tv_gift_detail, tv_start_time, tv_copy_code, tv_game_code, tv_game_user_code;
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

}

