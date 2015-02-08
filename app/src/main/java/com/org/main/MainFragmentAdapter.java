package com.org.main;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentAdapter extends FragmentPagerAdapter {
	List<Fragment> fragmentList;
	FragmentManager fm;
	public MainFragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fm = fm;
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		if(fragmentList!=null){
			if(arg0<fragmentList.size()){
				return fragmentList.get(arg0);
			}
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(fragmentList!=null){
			return fragmentList.size();
		}
		return 0;
	}

}
