package com.huosuapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huosuapp.text.R;


public class LoadMoreListView extends ListView implements OnScrollListener {

	private ProgressBar progressBar;
	private TextView tv_info;
	/**  加载更多的状态 */
	public static final int STATE_LOADING_MORE = 0;
	/**  加载失败的状态 */
	public static final int STATE_LOAD_ERROR = 1;
	/**  没有更多数据的状态 */
	public static final int STATE_NO_MORE_DATA = 2;
	/** 当前状态 */
	public int currentState = STATE_LOADING_MORE;
	private OnLoadingMoreListener mOnLoadingMoreListener;
	/** 当前是否正在做加载更多的操作，如果是则已经正在加载了则用true表示*/
	private boolean isLoadingMore;
	public View footerView;
	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		footerView = View.inflate(getContext(), R.layout.footer_view, null);
		progressBar = (ProgressBar) footerView.findViewById(R.id.progress_bar);
		tv_info = (TextView) footerView.findViewById(R.id.tv_info);
		setOnScrollListener(this);
		footerView.setOnClickListener(new MyOnClickListener());
		LoadMoreListView.this.addFooterView(footerView);
	}

	public class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// 当FooterView被单击的时候，这个方法就会被执行
			if (currentState == STATE_LOAD_ERROR			// 当前是加载失败状态
					&& mOnLoadingMoreListener != null) {	// 监听不等于null
				showLoadingMore();
				mOnLoadingMoreListener.onClickLoadErroView(LoadMoreListView.this);
			}
		}
	}
	
	
	/** OnScrollListener接口中的方法，当ListView滚动状态发生改变的时候会调用这个方法 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE	// ListView处于空闲状态
				&& getLastVisiblePosition() == getCount() - 1	// ListView最后一条item是可见的
					// ListView处于"加载更多"的状态
				&& mOnLoadingMoreListener != null				// 监听器不能为空
				&& !isLoadingMore								// 当前没有做加载更多的操作
				) {
			isLoadingMore = true;
			mOnLoadingMoreListener.onLoadingMore(this,scrollState);
		}
		
	}

	
	/** 通知ListView加载更多完成了 */
	public void onLoadingMoreComplete() {
		isLoadingMore = false;
	}

	/** OnScrollListener接口吕的方法，当ListView滚动的时候会调用这个方法 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}
	
	/** 显示“正在加载更多。。。” */
	public void showLoadingMore() {
		footerView.setVisibility(View.VISIBLE);
		currentState = STATE_LOADING_MORE;
		progressBar.setVisibility(View.VISIBLE);
		tv_info.setText("正在加载更多。。。");
	}
	
	/** 显示“加载失败，点击重试” */
	public void showLoadError() {
		footerView.setVisibility(View.VISIBLE);
		currentState = STATE_LOAD_ERROR;
		progressBar.setVisibility(View.GONE);
		tv_info.setText("加载失败，点击重试");
	}
	
	/** 显示“没有更多的数据了” */
	public void showNoMoreData() {
		footerView.setVisibility(View.VISIBLE);
		currentState = STATE_NO_MORE_DATA;
		progressBar.setVisibility(View.GONE);
		tv_info.setText("没有更多的数据了");
	}
	
	/** 设置加载更多的监听器 */
	public void setOnLoadingMoreListener(OnLoadingMoreListener onLoadingMoreListener) {
		mOnLoadingMoreListener = onLoadingMoreListener;
	}
	
	/** 更加载更多的监听器，当ListView处于可以加载更多的时候就会告诉这个监听器，即调用onLoadingMore方法 */
	public interface OnLoadingMoreListener {
		/** 当ListView处于可以加载更多的时候 */
		void onLoadingMore(LoadMoreListView listView,int scrollState);
		
		/** 当处于加载失败，并且用户点击了重新的时候 */
		void onClickLoadErroView(LoadMoreListView listView);
	}

}
