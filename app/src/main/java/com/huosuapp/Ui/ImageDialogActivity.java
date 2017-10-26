package com.huosuapp.Ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.huosuapp.Util.DimensionUtil;
import com.huosuapp.Util.Global;
import com.huosuapp.Util.ImgUtil;
import com.huosuapp.text.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author janecer
 * 2014-3-25下午3:44:45
 */
public class ImageDialogActivity extends Activity {
	protected static final String TAG = "IomageDialogActivity";
	private ViewPager vp_img;
	private LinearLayout ll_dots;//保存原点的容器
	private List<View> mDots = new ArrayList<View>();// 存放圆点视图的集合
    private ArrayList<String> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ttw_game_detailimg_dialog);
		list=getIntent().getStringArrayListExtra("inna");
		vp_img = (ViewPager)findViewById(R.id.vp_detail_img);
		ll_dots = (LinearLayout)findViewById(R.id.ll_dots);
		vp_img.setAdapter(new ImgAdapter());
		vp_img.setPageMargin(Global.dp2px(0,this));
		
		vp_img.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				int count = mDots.size();
				for (int i = 0; i < count; i++) {
					if (position == i) {
						mDots.get(position).setBackgroundResource(R.drawable.dot_focus);
					} else {
						mDots.get(i).setBackgroundResource(R.drawable.dot_normal);
					}
				}
			}
			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				
			}
			@Override
			public void onPageScrollStateChanged(int position) {
				
			}
		});
		}

	@Override
	protected void onStart() {
		super.onStart();
		int position =getIntent().getIntExtra("position",0);
		vp_img.setCurrentItem(position);//设置到当前选中页面
		dot_init(position);
	}
	
    private void dot_init(int position) {
		ll_dots.removeAllViews();
		mDots.clear();
		for (int i = 0; i < list.size(); i++) {
			View dot = new View(ImageDialogActivity.this);
			int size = DimensionUtil.dip2px(Global.getContext(), 10);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
			params.leftMargin = size;
			if (i == 0) {
				dot.setBackgroundResource(R.drawable.dot_focus);// 默认选择第1个点
			} else {
				dot.setBackgroundResource(R.drawable.dot_normal);
			}
			dot.setLayoutParams(params);
			ll_dots.addView(dot);
			mDots.add(dot);
		}
    }
    
	/**
	 * 图片详细页面信息的适配器
	 * @author Administrator
	 */
    class ImgAdapter extends PagerAdapter{
        private List<ImageView> imgs=null;
		public ImgAdapter(){
			//this.bitmaps=bitmaps;
			//初始化四张图片
			this.imgs=new ArrayList<ImageView>();
			for (int i = 0; i <list.size(); i++) {
				final ImageView iv=new ImageView(ImageDialogActivity.this);
				iv.setScaleType(ScaleType.FIT_XY);
//				ImgUtil.getBitmapUtils(ImageDialogActivity.this).display(iv,list.get(i));
				ImgUtil.loadImage(list.get(i),iv);
				imgs.add(iv);
			}
		}
    	
		@Override
		public int getCount() {
			return imgs.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
    	
		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			container.removeView(this.imgs.get(position));
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view=this.imgs.get(position);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageDialogActivity.this.finish();
				}
			});
			container.addView(view);
			return this.imgs.get(position);
		}
    }
}
