package com.huosuapp.Adapter;

import java.util.ArrayList;

import com.huosuapp.Fragment.Basefragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class
down_load_main_adapter extends FragmentPagerAdapter {
	 private ArrayList<Fragment> list;

	public down_load_main_adapter(FragmentManager fm,ArrayList<Fragment> list) {
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

}
