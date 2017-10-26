package com.huosuapp.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * <pre>
 * ViewPager的tab指示器，tab可滑动
 * 使用：
 * 设置tab背景颜色{@link #setTabBackground(int, int)}
 * 设置tab背景Drawable{@link #setTabBackground(Drawable, Drawable)}
 * 设置tab文字颜色{@link #setTabTextColor(int, int)}
 * 设置指示器颜色{@link #setIndicatorColor(int)}
 * </pre>
 */
public class PagerTab extends ViewGroup {

	private ViewPager mViewPager;
	private View currentTag;
	private PageListener mPageListener = new PageListener();//用于注册给ViewPager监听选择和滚动状态
	private OnPageChangeListener mDelegatePageListener;//用于通知外界ViewPager的状态和滚动
	private Context context;

	private int mDividerPadding = 12;// 分割线上下的padding
	private int mDividerWidth = 1;// 分割线的宽度
	private int mDividerColor = 0x1A000000;//分割线颜
	private Paint mDividerPaint;//分割线的画笔

	private int mIndicatorHeight = 3;//指示器的高度
	private int mIndicatorWidth;//指示器的宽度
	private int mIndicatorLeft;
	private Paint mIndicatorPaint; //指示器的画笔

	private int mContentWidth;//记录自身内容的宽
	private int mContentHeight;//记录自身内容的高

	private int mTabPadding = 24;// tab左右的内边距
	private int mTabTextSize = 15; //tab文字大小
	
	private int mTabCount;//tab的个数

	private int mCurrentPosition = 0;
	private float mCurrentOffsetPixels;
	private int mSelectedPosition = 0; //当前被选中的tab，用于记录手指点击tab的position

	private boolean mIsBeingDragged = false;//是否处于拖动
	private float mLastMotionX;//上一次手指触摸的x坐标
	private VelocityTracker mVelocityTracker;//用于记录速度的帮助类
	private int mMinimumVelocity;//系统默认的最小满足fling的速率
	private int mMaximumVelocity;//系统默认最大的fling速率
	private int mTouchSlop;//系统默认满足滑动的最小位

	private ScrollerCompat mScroller;//处理滚动的帮助类
	private int mLastScrollX;//记录上一次滚动的x位置，这是用于处理overScroll，实际位置可能会受到限制

	private int mMaxScrollX = 0;// 控件最大可滚动的距离
	private int mSplitScrollX = 0;

	private EdgeEffectCompat mLeftEdge;//处理overScroll的反馈效果
	private EdgeEffectCompat mRightEdge;
	private ColorStateList tabTextColor;
	private Drawable pressedDrawable;
	private Drawable normalDrawable;
	private int indicatorColor;

	public PagerTab(Context context) {
		this(context, null);
	}

	public PagerTab(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PagerTab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
		initPaint();
	}

	/** 初始化一些变量*/
	private void init() {
		mIndicatorHeight = dp2px(mIndicatorHeight);
		mDividerPadding = dp2px(mDividerPadding);
		mTabPadding = dp2px(mTabPadding);
		mDividerWidth = dp2px(mDividerWidth);
		mTabTextSize = dp2px(mTabTextSize);
		mScroller = ScrollerCompat.create(context);
		//获取系统关于View的常量配置类
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		//获取滑动的最小距离
		mTouchSlop = configuration.getScaledTouchSlop();
		//获取fling的最小速率
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		//获取fling的最大速率
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

		mLeftEdge = new EdgeEffectCompat(context);
		mRightEdge = new EdgeEffectCompat(context);
		
		Drawable background = getBackground();
		if (background == null) {
			// 如果在xml中没有设置背景，则设置一个空的背景，因为如果不设置背景的话就看不到指示器
			setBackgroundDrawable(new ColorDrawable());
			
			
		}
	}

	/** 初始化笔 */
	private void initPaint() {
		mIndicatorPaint = new Paint();
		mIndicatorPaint.setAntiAlias(true);
		mIndicatorPaint.setStyle(Paint.Style.FILL);
		mIndicatorPaint.setColor(Color.parseColor("#1a9df5"));

		mDividerPaint = new Paint();
		mDividerPaint.setAntiAlias(true);
		mDividerPaint.setStrokeWidth(mDividerWidth);
		mDividerPaint.setColor(Color.parseColor("#1a9df5"));
	}

	/** 设置ViewPager */
	public void setViewPager(ViewPager viewPager) {
		if (viewPager == null || viewPager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager is null or ViewPager does not have adapter instance.");
		}
		mViewPager = viewPager;
		onViewPagerChanged();
	}
	
	/**
	 * 设置Tab的背景
	 * @param pressedColor 按下状态时背景的颜色值，必须是8位的16进制值，如半透明红色为：0x66FF0000
	 * @param normalColor 正常状态时背景的颜色值，必须是8位的16进制值，如半透明红色为：0x66FF0000
	 */
	public void setTabBackground(int pressedColor, int normalColor) {
		setTabBackground(new ColorDrawable(pressedColor), new ColorDrawable(normalColor));
	}
	
	/**
	 * 
	 * @param pressedDrawable 按下状态的Drawable
	 * @param normalDrawable 正常状态的Drawable，不需要可填null
	 */
	public void setTabBackground(Drawable pressedDrawable, Drawable normalDrawable) {
		this.pressedDrawable = pressedDrawable;
		this.normalDrawable = normalDrawable;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child instanceof TextView) {
				// StateListDrawable不可复用，所以每次都要创建新的对象
				StateListDrawable stateListDrawable = createDrawableSelector(pressedDrawable, normalDrawable);
				child.setBackgroundDrawable(stateListDrawable);
			}
		}
		invalidate();
	}
	
	/**
	 * 设置Tab文字的颜色
	 * @param selectedColor 选择状态时文字的颜色值，必须是8位的16进制值，如半透明红色为：0x66FF0000
	 * @param normalColor 正常状态时文字的颜色值，必须是8位的16进制值，如半透明红色为：0x66FF0000
	 */
	public void setTabTextColor(int selectedColor, int normalColor) {
		tabTextColor = createColorStateList(selectedColor, normalColor);
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child instanceof TextView) {
				((TextView) child).setTextColor(tabTextColor);
			}
		}
		invalidate();
	}
	
	/**
	 * 设置指示器的颜色
	 * @param color 必须是8位16进制颜色，如半透明红色：0x66FF0000
	 */
	public void setIndicatorColor(int color) {
		mIndicatorPaint.setColor(Color.parseColor("#1a9df5"));
		invalidate();
	}

	private void onViewPagerChanged() {
		mViewPager.setOnPageChangeListener(mPageListener);//给ViewPager设置监听
		mTabCount = mViewPager.getAdapter().getCount();
		for (int i = 0; i < mTabCount; i++) {
			if (mViewPager.getAdapter() instanceof IconTabProvider) {//如果想要使用icon作为tab，则该adapter必须实现IconTabProvider接口
				addIconTab(i, ((IconTabProvider) mViewPager.getAdapter()).getPageIconResId(i));
			} else {
				addTextTab(i, mViewPager.getAdapter().getPageTitle(i).toString());
			}
		}
		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
		if (viewTreeObserver != null) {//监听第一个的全局layout事件，来设置当前的mCurrentPosition，显示对应的tab
			viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					getViewTreeObserver().removeGlobalOnLayoutListener(this);//只需要监听一次，之后通过listener回调即可
					mCurrentPosition = mViewPager.getCurrentItem();
					if (mDelegatePageListener != null) {
						mDelegatePageListener.onPageSelected(mCurrentPosition);
					}
				}
			});
		}
	}

	/** 设置监听，因为Tab会监听ViewPager的状态，所以不要给ViewPager设置监听了，设置给Tab，由Tab转发 */
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mDelegatePageListener = listener;
	}

	/** 添加文字tab */
	@SuppressWarnings("deprecation")
	private void addTextTab(final int position, String title) {
		TextView tab = new TextView(context);
		tab.setText(title);
		tab.setGravity(Gravity.CENTER);
		tab.setSingleLine();
		tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
		//tab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		
		if (tabTextColor == null) {
			// 默认选择状态为蓝色，正常状态为深灰色
			//tabTextColor = createColorStateList(0xff0084ff, 0xff737373);
			tabTextColor=createColorStateList(Color.parseColor("#1a9df5"),Color.parseColor("#464646"));
		}
		
		tab.setTextColor(tabTextColor);
		
		// 创建一个Selector类型的Drawable
		Drawable tabBackground;
		if (pressedDrawable == null) {
			// 按下默认按下状态是一个带透明度的蓝色背景
			pressedDrawable = new ColorDrawable(0x6633b5e5);
			normalDrawable = null;
			tabBackground = createDrawableSelector(pressedDrawable, null);
		} else {
			// 注:tabTextColor是可以复用的，而tabBackground是不可以复用的，所以需要每次都创建一个新的tabBackground
			tabBackground = createDrawableSelector(pressedDrawable, normalDrawable);
		}
		tab.setBackgroundDrawable(tabBackground);
		tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		/*
		 * ben
		 */
		if(position == 0){
			currentTag = tab;
			currentTag.setSelected(true);
		}
		/*
		 * ben
		 */
		addTab(position, tab);
	}

	/**
	 * 创建一个颜色选择器
	 * @param selectedColor 选择状态时文字的颜色值，必须是8位的16进制值，如半透明红色为：0x66FF0000
	 * @param normalColor 正常状态时文字的颜色值，必须是8位的16进制值，如半透明红色为：0x66FF0000
	 * @return
	 */
	private ColorStateList createColorStateList(int selectedColor, int normalColor) {
		// 创建一个颜色选择器
		int[] state1 = {android.R.attr.state_pressed};		// 第一个状态，按下状态
		int[] state2 = {android.R.attr.state_selected};		// 第二个状态，选择状态
		int[] state3 = {};									// 第三个状态，其它任何状态
		int[][] states = new int[3][1];
		states[0] = state1;
		states[1] = state2;
		states[2] = state3;
		
		int[] colors = {
				selectedColor, 	// 按下状态的颜色
				selectedColor, 	// 选中状态的颜色
				normalColor  	// 未选状态的颜色
		};
		ColorStateList colorStateList = new ColorStateList(states, colors);
		return colorStateList;
	}
	
	/**
	 * 创建Selector
	 * @param pressedDrawable 按下状态的Drawable
	 * @param normalDrawable 正常状态的Drawable，不需要可填null
	 * @return
	 */
    public StateListDrawable createDrawableSelector(Drawable pressedDrawable, Drawable normalDrawable) {  
		StateListDrawable stateDrawable = new StateListDrawable();  
        // View.PRESSED_ENABLED_STATE_SET  
        stateDrawable.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressedDrawable);  
        // View.ENABLED_FOCUSED_STATE_SET  
//        bg.addState(new int[] { android.R.attr.state_selected, android.R.attr.state_enabled }, selectedDrawable);  
        // View.ENABLED_STATE_SET  
//        bg.addState(new int[] { android.R.attr.state_enabled }, normal);  
        // View.FOCUSED_STATE_SET  
//        bg.addState(new int[] { android.R.attr.state_focused }, focused);  
        // View.WINDOW_FOCUSED_STATE_SET  
//        bg.addState(new int[] { android.R.attr.state_window_focused }, unable);  
        // View.EMPTY_STATE_SET  正常状态
        if (normalDrawable != null) {
        	stateDrawable.addState(new int[] {}, normalDrawable);  
		}
        return stateDrawable;  
    }  
    
    /**
     * 创建Selector
     * @param pressedColor 按下状态时背景的颜色值，必须是8位的16进制值，如半透明红色为：0x66FF0000
     * @param normalColor 正常状态时背景的颜色值，必须是8位的16进制值，如半透明红色为：0x66FF0000
     * @return
     */
    public StateListDrawable createDrawableSelector(int pressedColor, int normalColor) { 
    	return createDrawableSelector(new ColorDrawable(pressedColor), new ColorDrawable(normalColor));
    }

	/** 添加图片icon */
	private void addIconTab(final int position, int resId) {
		ImageButton tab = new ImageButton(context);
		tab.setImageResource(resId);
		tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addTab(position, tab);
	}

	private void addTab(final int position, View tab) {
		tab.setFocusable(true);
		//设置tab的点击事件，当tab被点击时候切换pager的页面
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(position);
			}
		});
		tab.setPadding(mTabPadding, 0, mTabPadding, 0);
		addView(tab, position);
	}

	/** 测量时的回调 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获取控件自身的宽高模式
		int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingBottom();
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int totalWidth = 0;
		int highest = 0;
		int goneChildCount = 0;
		for (int i = 0; i < mTabCount; i++) {
			final View child = getChildAt(i);
			if (child == null || child.getVisibility() == View.GONE) {
				goneChildCount--;
				continue;
			}
			int childWidthMeasureSpec;
			int childHeightMeasureSpec;

			LayoutParams childLayoutParams = child.getLayoutParams();
			if (childLayoutParams == null) {
				childLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			}

			if (childLayoutParams.width == LayoutParams.MATCH_PARENT) {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
			} else if (childLayoutParams.width == LayoutParams.WRAP_CONTENT) {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
			} else {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.width, MeasureSpec.EXACTLY);
			}

			if (childLayoutParams.height == LayoutParams.MATCH_PARENT) {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
			} else if (childLayoutParams.height == LayoutParams.WRAP_CONTENT) {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
			} else {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.height, MeasureSpec.EXACTLY);
			}

			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			totalWidth += childWidth;
			highest = highest < childHeight ? childHeight : highest;
		}

		if (totalWidth <= widthSize) {//如果子Tab的宽度小于PagerTab，则采用平分模式
			int splitWidth = (int) (widthSize / (mTabCount - goneChildCount + 0.0f) + 0.5f);
			for (int i = 0; i < mTabCount; i++) {
				final View child = getChildAt(i);
				if (child == null || child.getVisibility() == View.GONE) {
					continue;
				}
				int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(splitWidth, MeasureSpec.EXACTLY);
				int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.EXACTLY);
				child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
			}
			mMaxScrollX = 0;
			mSplitScrollX = 0;
		} else {//如果所有子View大于控件的宽
			mMaxScrollX = totalWidth - widthSize;
			mSplitScrollX = (int) (mMaxScrollX / (mTabCount - goneChildCount - 1.0f) + 0.5f);
		}

		if (widthMode == MeasureSpec.EXACTLY) {
			mContentWidth = widthSize;
		} else {
			mContentWidth = totalWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			mContentHeight = heightSize;
		} else {
			mContentHeight = highest;
		}

		int measureWidth = mContentWidth + getPaddingLeft() + getPaddingRight();
		int measureHeight = mContentHeight + getPaddingTop() + getPaddingBottom();
		setMeasuredDimension(measureWidth, measureHeight);
	}

	/** 布局时的回调 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int height = b - t;//控件供子View显示的高
			int left = l;
			for (int i = 0; i < mTabCount; i++) {
				final View child = getChildAt(i);
				if (child == null || child.getVisibility() == View.GONE) {
					continue;
				}
				int top = (int) ((height - child.getMeasuredHeight()) / 2.0f + 0.5f);//如果控件比tab要高，则居中显示
				int right = left + child.getMeasuredWidth();
				child.layout(left, top, right, top + child.getMeasuredHeight());//摆放tab
				left = right;//因为是水平摆放的，所以为下一个准备left
			}
		}
	}

	/** 绘制时的回调 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final int height = getHeight();
		//画指示器
		canvas.drawRect(mIndicatorLeft, height - mIndicatorHeight, mIndicatorLeft + mIndicatorWidth, height, mIndicatorPaint);

		// 画分割线
		for (int i = 0; i < mTabCount - 1; i++) {//分割线的个数比tab的个数少1
			final View child = getChildAt(i);
			if (child == null || child.getVisibility() == View.GONE) {
				continue;
			}
			if (child != null) {
				canvas.drawLine(child.getRight(), mDividerPadding, child.getRight(), mContentHeight - mDividerPadding, mDividerPaint);
			}
		}
		// 因为overScroll效果是一个持续效果，
		boolean needsInvalidate = false;
		if (!mLeftEdge.isFinished()) {//如果效果没停
			final int restoreCount = canvas.save();//先保存当前画面
			final int heightEdge = getHeight() - getPaddingTop() - getPaddingBottom();
			final int widthEdge = getWidth();
			canvas.rotate(270);
			canvas.translate(-heightEdge + getPaddingTop(), 0);
			mLeftEdge.setSize(heightEdge, widthEdge);
			needsInvalidate |= mLeftEdge.draw(canvas);
			canvas.restoreToCount(restoreCount);
		}
		if (!mRightEdge.isFinished()) {
			final int restoreCount = canvas.save();
			final int widthEdge = getWidth();
			final int heightEdge = getHeight() - getPaddingTop() - getPaddingBottom();
			canvas.rotate(90);
			canvas.translate(-getPaddingTop(), -(widthEdge + mMaxScrollX));
			mRightEdge.setSize(heightEdge, widthEdge);
			needsInvalidate |= mRightEdge.draw(canvas);
			canvas.restoreToCount(restoreCount);
		}
		if (needsInvalidate) {
			postInvalidate();
		}
	}

	/** 触摸事件是否拦截的方 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if (mIsBeingDragged && action == MotionEvent.ACTION_MOVE) {//当已经处于拖动，并且当前事件是MOVE，直接消费掉
			return true;
		}
		switch (action) {
			case MotionEvent.ACTION_DOWN: {
				final float x = ev.getX();
				mLastMotionX = x; //记录住当前的x坐标
				mIsBeingDragged = !mScroller.isFinished();//如果按下的时候还在滚动，则把状设置为处于拖动状态
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				final float x = ev.getX();
				final int xDiff = (int) Math.abs(x - mLastMotionX);//计算两次的差
				if (xDiff > mTouchSlop) {
					mIsBeingDragged = true;
					mLastMotionX = x;
					ViewParent parent = getParent();//并请求父View不要再拦截自己触摸事件，交给自己处理
					if (parent != null) {
						parent.requestDisallowInterceptTouchEvent(true);
					}
				}
				break;
			}
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				mIsBeingDragged = false;
				break;
		}
		return mIsBeingDragged;//如果是拖动状态，则拦截事件，交给自己的onTouch处理
	}

	/** 触摸事件的处理方法*/
	public boolean onTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		final int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN: {//如果是down事件，记录住当前的x坐标
				final float x = ev.getX();
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}
				mLastMotionX = x;
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				final float x = ev.getX();
				final float deltaX = x - mLastMotionX;
				if (!mIsBeingDragged) {//如果还没有处于拖动，则判断两次的差是否大于mTouchSlop
					if (Math.abs(deltaX) > mTouchSlop) {
						mIsBeingDragged = true;
					}
				}
				if (mIsBeingDragged) {//如果处于拖动状态，记录住x坐标
					mLastMotionX = x;
					onMove(deltaX);
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				if (mIsBeingDragged) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					//先对速度进行调整，第1个参数是时间单位，1000毫秒，第二个参数是最大速率
					velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
					float velocity = velocityTracker.getXVelocity();//获取水平方向上的速度
					onUp(velocity);
				}
			}
			case MotionEvent.ACTION_CANCEL: {
				mIsBeingDragged = false;
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				break;
			}
		}
		return true;
	}

	private void onMove(float x) {
		if (mMaxScrollX <= 0) {
			if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
				mViewPager.fakeDragBy(x);
			}
		} else {
			int scrollByX = -(int) (x + 0.5);
			if (getScrollX() + scrollByX < 0) {
				scrollByX = 0 - getScrollX();
				mLeftEdge.onPull(Math.abs(x) / getWidth());
			}
			if (getScrollX() + scrollByX > mMaxScrollX) {
				scrollByX = mMaxScrollX - getScrollX();
				mRightEdge.onPull(Math.abs(x) / getWidth());
			}
			scrollBy(scrollByX, 0);
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	private void onUp(float velocity) {
		if (mMaxScrollX <= 0) {
			if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
		} else {
			if (Math.abs(velocity) <= mMinimumVelocity) {
				return;
			}
			mScroller.fling(getScrollX(), 0, -(int) (velocity + 0.5), 0, 0, mMaxScrollX, 0, 0, 270, 0);
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldX = mLastScrollX;
			mLastScrollX = mScroller.getCurrX();
			if (mLastScrollX < 0 && oldX >= 0) {
				mLeftEdge.onAbsorb((int) mScroller.getCurrVelocity());
			} else if (mLastScrollX > mMaxScrollX && oldX <= mMaxScrollX) {
				mRightEdge.onAbsorb((int) mScroller.getCurrVelocity());
			}
			int x = mLastScrollX;
			if (mLastScrollX < 0) {
				x = 0;
			} else if (mLastScrollX > mMaxScrollX) {
				x = mMaxScrollX;
			}
			scrollTo(x, 0);
		}
		ViewCompat.postInvalidateOnAnimation(this);
	}

	private void checkAndcalculate() {
		// 如果指示器起始位置比第一个tab的起始位置还要小，纠正为第一个tab的起始位置，指示器宽度就是firstTab的宽
		final View firstTab = getChildAt(0);
		if (mIndicatorLeft < firstTab.getLeft()) {
			mIndicatorLeft = firstTab.getLeft();
			mIndicatorWidth = firstTab.getWidth();
		}
		// 如果指示器起始位置比lastTab的起始位置还要大，纠正为lastTab的起始位置，指示器宽度就是最后一个tab的宽
		View lastTab = getChildAt(mTabCount - 1);
		if (mIndicatorLeft > lastTab.getLeft()) {
			mIndicatorLeft = lastTab.getLeft();
			mIndicatorWidth = lastTab.getWidth();
		}
		// 通过指示器的起始位置计算出当前处于第几个position，并且计算出已经偏移了多少，偏移量是以当前所处的tab的宽度的百分
		for (int i = 0; i < mTabCount; i++) {
			View tab = getChildAt(i);
			if (mIndicatorLeft < tab.getLeft()) {
				mCurrentPosition = i - 1;
				View currentTab = getChildAt(mCurrentPosition);
				mCurrentOffsetPixels = (mIndicatorLeft - currentTab.getLeft()) / (currentTab.getWidth() + 0.0f);
				break;
			}
		}
	}

	/** 滚动到指定的child */
	public void scrollSelf(int position, float offset) {
		if (position >= mTabCount) {
			return;
		}
		final View tab = getChildAt(position);
		mIndicatorLeft = (int) (tab.getLeft() + tab.getWidth() * offset + 0.5);
		int rightPosition = position + 1;
		if (offset > 0 && rightPosition < mTabCount) {
			View rightTab = getChildAt(rightPosition);
			mIndicatorWidth = (int) (tab.getWidth() * (1 - offset) + rightTab.getWidth() * offset + 0.5);
		} else {
			mIndicatorWidth = tab.getWidth();
		}
		checkAndcalculate();

		int newScrollX = position * mSplitScrollX + (int) (offset * mSplitScrollX + 0.5);
		if (newScrollX < 0) {
			newScrollX = 0;
		}
		if (newScrollX > mMaxScrollX) {
			newScrollX = mMaxScrollX;
		}
		//scrollTo(newScrollX, 0);//滑动
		int duration = 100;
		if (mSelectedPosition != -1) {
			duration = (Math.abs(mSelectedPosition - position)) * 100;
		}
		mScroller.startScroll(getScrollX(), 0, (newScrollX - getScrollX()), 0, duration);
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/** ViewPager的OnPageChangeListener实现类，因为我们在PagerTab中获取PagerView的监听，以便可以调整tab */
	private class PageListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset, final int positionOffsetPixels) {
			//根据VierPager的偏移来滚动tab
			scrollSelf(position, positionOffset);
			if (mDelegatePageListener != null) {
				mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				mSelectedPosition = -1;
			}
			if (mDelegatePageListener != null) {
				mDelegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			if (currentTag != null) {
				currentTag.setSelected(false);
			}
			
			currentTag = getChildAt(position);
			currentTag.setSelected(true);
			mSelectedPosition = position;
			if (mDelegatePageListener != null) {
				mDelegatePageListener.onPageSelected(position);
			}
		}
	}

	/** 如果指示器希望是图片，则继承该接口 */
	public interface IconTabProvider {
		public int getPageIconResId(int position);
		public int getPageSelectedIconResId();
	}
	
	/** 把dp单位的值转换为px单位的值 */
	public int dp2px(int dp) {
		// 获取手机的屏幕密度，不同手机的屏幕密度可能不一样
		float density = getResources().getDisplayMetrics().density;
		return (int) (dp * density + 0.5);	// 加0.5是为了把结果四舍五入
	}
	
	/** 把px单位的值转换为dp单位的值 */
	public int px2dp(int px) {
		float density = getResources().getDisplayMetrics().density;
		return (int) (px / density + 0.5);	// 加0.5是为了把结果四舍五入
	}
}
