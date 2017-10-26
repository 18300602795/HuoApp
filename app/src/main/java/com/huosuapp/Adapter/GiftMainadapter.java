package com.huosuapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.huosuapp.Fragment.Basefragment;

import java.util.ArrayList;

public class GiftMainadapter extends FragmentPagerAdapter {
	 private ArrayList<Fragment> list;

	public GiftMainadapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		this.list=list;
	}

	@Override
	public Fragment getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public int getCount() {
		
		return list.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		 Basefragment framFragment=(Basefragment) list.get(position);
		return framFragment.getTitle();
	}
	private int mChildCount = 0;

	@Override
	public void notifyDataSetChanged() {
		mChildCount = getCount();
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object)   {
		if ( mChildCount > 0) {
			mChildCount --;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

}
