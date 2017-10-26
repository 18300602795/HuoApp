package com.huosuapp.Adapter;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huosuapp.Bean.Gift_Code_Bean_Error;
import com.huosuapp.Bean.Gift_Code_Bean_Succeed;
import com.huosuapp.Bean.KeyCodeBean;
import com.huosuapp.Fragment.key_cody_fragment1;
import com.huosuapp.Util.ImgUtil;
import com.huosuapp.text.R;

import java.util.ArrayList;
import java.util.List;

public class UserKeyCodeListadapter extends BaseAdapter {
//    private int[] drawbles = {R.drawable.gift1, R.drawable.gift2, R.drawable.gift3};
    private Dialog gift_diDialog;
    private Context context;
    private List<KeyCodeBean.DataBean.GiftListBean> gift_list;
    private int i;

    private key_cody_fragment1 attachTo;
    public UserKeyCodeListadapter(List<KeyCodeBean.DataBean.GiftListBean> gift_list, Context context, key_cody_fragment1 fragment) {
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
    public void setDataList(List<KeyCodeBean.DataBean.GiftListBean> listBean){
        this.gift_list=listBean;
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
            vh.iv_game_icon=(ImageView) convertView.findViewById(R.id.iv_game_icon);
            vh.tv_game_name=(TextView) convertView.findViewById(R.id.tv_game_name);
            vh.tv_start_time=(TextView) convertView.findViewById(R.id.tv_start_time);
            vh.tv_copy_code=(TextView) convertView.findViewById(R.id.tv_copy_code);
            vh.tv_gift_code=(TextView) convertView.findViewById(R.id.tv_gift_code);
            vh.tv_count_hint=(TextView) convertView.findViewById(R.id.tv_count_hint);
            convertView.setTag(vh);
        }
        final ViewHold vh = (ViewHold) convertView.getTag();
        vh.tv_count_hint.setVisibility(View.GONE);
        final KeyCodeBean.DataBean.GiftListBean gb = gift_list.get(position);
         if ( gb.getCode()!=null&&gb.getCode()!=""){
             vh.tv_gift_code.setText(gb.getCode());
         }
        //点击了领取按钮
        //vh.tv_copy_code.setOnClickListener(onItemClickListener);
//        OnclickLingqu(position, vh.tv_copy_code, vh.tv_gift_code);
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
                String keyCode = vh.tv_gift_code.getText().toString().trim();
                copy(keyCode,context);
            }
        });

        vh.tv_game_name.setText(gb.getGiftname());
        vh.tv_start_time.setText("起止时间" + gb.getStarttime() + " " + gb.getEnttime());
        if (gb.getIcon() != null&&gb.getIcon()!="") {
//            ImgUtil.getBitmapUtils(parent.getContext()).display(vh.iv_game_icon, gb.getIcon());
            ImgUtil.loadImage(gb.getIcon(),vh.iv_game_icon);
        }

        //vh.tv_gift_code.setText(gb.getRemain() + "");  //显示剩余礼包数量
        return convertView;
    }

    static class ViewHold{
        ImageView iv_game_icon;
        TextView tv_game_name,tv_gift_code,tv_start_time,tv_copy_code,tv_count_hint;
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




    private Gift_Code_Bean_Error gift_code_bean_error;
    private Gift_Code_Bean_Succeed gift_code_bean_succeed;

    public void notifyDataChanged(){
        if(context!=null){
            if (attachTo!=null) {
                attachTo.getData();
            }
        }
    }
}

