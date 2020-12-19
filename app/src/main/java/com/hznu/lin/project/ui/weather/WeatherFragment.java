package com.hznu.lin.project.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hznu.lin.project.R;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {
    private View contextView;// 总视图
    private TabLayout tabLayout;
    private ViewPager viewpager;
    List<Fragment> fragmentList;
    String[] titles = {"今日","推荐"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.fragment_weather, container, false);
        tabLayout = contextView.findViewById(R.id.tab_layout);
        viewpager = contextView.findViewById(R.id.viewPage);
        return contextView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fragmentList = new ArrayList<>();
        fragmentList.add(new TodayFragment());
        fragmentList.add(new RecommendFragment());

        FragmentAdapter adapter = new FragmentAdapter(fragmentList, titles,getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
    }

}