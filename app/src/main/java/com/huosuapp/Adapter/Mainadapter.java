package com.huosuapp.Adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Mainadapter extends FragmentPagerAdapter {
	 private ArrayList<Fragment> list;

	public Mainadapter(FragmentManager fm,ArrayList<Fragment> list) {
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

}
