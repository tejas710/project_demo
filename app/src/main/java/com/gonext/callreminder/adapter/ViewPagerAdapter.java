package com.gonext.callreminder.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.gonext.callreminder.fragment.CallnotedialogFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    // Declare the number of ViewPager pages
    int PAGE_COUNT;
	ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

	public ViewPagerAdapter(FragmentManager fm, int PAGE_COUNT) {
		super(fm);
		this.PAGE_COUNT = PAGE_COUNT;
		for (int i = 0; i < PAGE_COUNT; i++) {
			fragmentArrayList.add(null);
		}
	}
 
	@Override
	public Fragment getItem(int position) {
		if (fragmentArrayList.get(position) == null) {
			fragmentArrayList.set(position, CallnotedialogFragment.newInstance(position));
		}
		return fragmentArrayList.get(position);

	}
 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_COUNT;
	}

}