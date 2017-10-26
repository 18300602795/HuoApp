package com.huosuapp.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
	
	/** 多行View */
	private ArrayList<ArrayList<View>> multiLines = new ArrayList<ArrayList<View>>();
	private int horizotalSpacing = 36;
	private int verticalSpacing = 36;

	/** 因为只在代码中直接new，所以创建这个构造方法就可以了 */
	public FlowLayout(Context context) {
		super(context);
	}
	
	/**
	 * 测量FlowLayout及它的子View
	 * @param widthMeasureSpec 父容器希望FlowLayout的宽
	 * @param heightMeasureSpec 父容器希望FlowLayout的高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		multiLines.clear();
		// 获取FlowLayout测量的宽
		int parentMeasuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		
		ArrayList<View> oneLineViews = null;	// 一行View
		
		// 遍历所有的子View，测量所有的子View 
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);	// 获取子View
			// 把测量规格传给子View，让子View完成测量
			child.measure(MeasureSpec.makeMeasureSpec(parentMeasuredWidth, MeasureSpec.AT_MOST), 0);
			
			//  获取子View测量的宽
			int childMeasuredWidth = child.getMeasuredWidth();
			
			// 如果是第0个需要创建新行，或者当前View的宽度大于一行中剩余的可用宽度也需要创建新行
			if (i == 0 || childMeasuredWidth > parentMeasuredWidth 
											   - getPaddingLeft() 
					                           - getPaddingRight() 
					                           - getLineWidth(oneLineViews)) {
				oneLineViews = new ArrayList<View>();
				multiLines.add(oneLineViews);
			}
			oneLineViews.add(child);
		}
		
		// 设置FlowLayout的宽和高，宽就用父容器传的宽，高用所有行的高
		setMeasuredDimension(parentMeasuredWidth, getAllLineHeight() + getPaddingTop() + getPaddingBottom());
	}
	
	/** 获取所有行的高 */
	private int getAllLineHeight() {
		if (multiLines.size() < 0) {
			return 0;
		} else {
			int totalSpacing = (multiLines.size() - 1) * verticalSpacing;
			return multiLines.get(0).get(0).getMeasuredHeight() * multiLines.size() + totalSpacing;
		}
	}
	
	/** 获取行宽 */
	private int getLineWidth(ArrayList<View> views) {
		int lineWidth = 0;
		for (int i = 0; i < views.size(); i++) {
			lineWidth = lineWidth + views.get(i).getMeasuredWidth();
		}
		int totalSpacing = (views.size() - 1) * horizotalSpacing;
		return lineWidth + totalSpacing;
	}

	/**
	 * 对FlowLayout的子View进行排版
	 * @param parentLeft FlowLayout的left坐标
	 * @param parentTop FlowLayout的top坐标
	 * @param parentRight FlowLayout的right坐标
	 * @param parentBottom FlowLayout的bottom坐标
	 */
	@Override
	protected void onLayout(boolean changed, int parentLeft, int parentTop, int parentRight, int parentBottom) {
		// 遍历所有的行
		int preLineBottom = 0;
		for (int i = 0; i < multiLines.size(); i++) {
			ArrayList<View> oneLineViews = multiLines.get(i);	// 获取一行
			
			// 一行中可用的宽（没有被使用的宽）
			int usableWidth = getMeasuredWidth() - getPaddingLeft() 
												 - getPaddingRight() 
												 - getLineWidth(oneLineViews);
			// 平均每个View可多分到的宽
			int averageUsableWidth = usableWidth / oneLineViews.size();
			
			// 遍历一行中所有的列
			int preColumnRight = 0;
			for (int j = 0; j < oneLineViews.size(); j++) {
				View column = oneLineViews.get(j);	// 获取一行中的某个子View
				
				// 获取子View的宽和高
				int childWidth = column.getMeasuredWidth();
				int childHeight = column.getMeasuredHeight();
				
				// 设置子View在容器中的位置
				int childLeft;
				if (j == 0) {
					// 如果是第一列，则它的left位置要加上paddingLeft
					childLeft 	= parentLeft + getPaddingLeft() + preColumnRight;	// 显示到上一列右边的位置
				} else {
					// 如果不是第一列，则它的left需要加上spacing
					childLeft 	= parentLeft + preColumnRight + horizotalSpacing;	// 显示到上一列右边的位置
				}
				
				int childTop;
				if (i == 0) {
					// 如果是第一行，则它的top位置要加上paddingTop
					childTop 	= parentTop + getPaddingTop() + preLineBottom;	// 显示到上一行下方的位置
				} else {
					// 如果不是第一行，则它的top位置要加上spacing
					childTop 	= parentTop + preLineBottom + verticalSpacing;	// 显示到上一行下方的位置
				}
				int childRight 	= childLeft + childWidth + averageUsableWidth;
				int childBottom = childTop + childHeight;
				column.layout(childLeft, childTop, childRight, childBottom);
				
				// view的宽高改变了，需要重新测量。如果不重新测量的话宽度显示是没有问题的，但是文本内容水平方向不会居中
				int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childRight - childLeft, MeasureSpec.EXACTLY);
				int heightMeasureSpec = MeasureSpec.makeMeasureSpec(childBottom - childTop, MeasureSpec.EXACTLY);
				column.measure(widthMeasureSpec, heightMeasureSpec);	
				
				// 保存当前列的right坐标，用于下次使用
				preColumnRight = childRight;
			}
			
			// 保存当行行的bottom坐标，用于下次使用
			preLineBottom = oneLineViews.get(0).getBottom();
		}
	}

}
