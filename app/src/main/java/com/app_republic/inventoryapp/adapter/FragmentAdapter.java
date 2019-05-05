package com.app_republic.inventoryapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app_republic.inventoryapp.ItemFragment;
import com.app_republic.inventoryapp.R;

public class FragmentAdapter extends FragmentPagerAdapter {


    private final String[] titles;

    public FragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        titles = context.getResources().getStringArray(R.array.fragment_titles);
    }

    @Override
    public Fragment getItem(int position) {
        ItemFragment itemFragment = new ItemFragment();
        itemFragment.setFragmentItemPosition(position);
        return itemFragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
