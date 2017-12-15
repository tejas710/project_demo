package com.gonext.callreminder.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gonext.callreminder.fragment.CallNotesFragment;
import com.gonext.callreminder.fragment.ReminderFragment;

public class CallReminderViewPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[]{"Reminder", "Call Notes"};
    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 2;
    Context context;
    Fragment fragment;

    public CallReminderViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fragment = ReminderFragment.newInstance(position);
                    break;
                case 1:
                    fragment = CallNotesFragment.newInstance(position);
                    break;
            }
            return fragment;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}