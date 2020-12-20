package com.hznu.lin.project.fragment.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author LIN
 * @date 2020/12/17 14:41
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private List<Fragment> fragments;

    public FragmentAdapter(List<Fragment> fragments, String[] titles, FragmentManager fm) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
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
