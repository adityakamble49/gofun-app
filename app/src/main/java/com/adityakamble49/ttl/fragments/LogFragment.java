package com.adityakamble49.ttl.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.adityakamble49.ttl.R;

public class LogFragment extends Fragment {
    private final int ELEVATION = 6;
    private OnFragmentInteractionListener mListener;
    private TabLayout mLogTabLayout;
    private String[] mLogTabTitles;
    private ViewPager mLogViewPager;
    private LogPagerAdapter mLogPagerAdapter;

    public LogFragment() {
        // Required empty public constructor
    }

    public static LogFragment newInstance() {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_log, container, false);

        mLogTabTitles = getResources().getStringArray(R.array.log_tab_titles);

        mLogTabLayout = (TabLayout) rootView.findViewById(R.id.v_tl_log_tabs);
        ViewCompat.setElevation(mLogTabLayout, ELEVATION);
        mLogTabLayout.addTab(mLogTabLayout.newTab().setText(mLogTabTitles[0]));
        mLogTabLayout.addTab(mLogTabLayout.newTab().setText(mLogTabTitles[1]));
        mLogTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mLogTabLayout.setOnTabSelectedListener(new LogTabChangeListener());

        mLogPagerAdapter = new LogPagerAdapter(getChildFragmentManager());
        mLogViewPager = (ViewPager) rootView.findViewById(R.id.vg_vp_log_pager);
        mLogViewPager.setAdapter(mLogPagerAdapter);
        mLogViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener
                (mLogTabLayout));

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }

    private class LogPagerAdapter extends FragmentPagerAdapter {

        public LogPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return WeeklyLogFragment.newInstance();
                case 1:
                    return MonthlyLogFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mLogTabTitles[position];
        }
    }

    private class LogTabChangeListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mLogViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
