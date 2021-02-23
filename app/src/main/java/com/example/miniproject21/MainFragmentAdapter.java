package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.miniproject21.FragmentSet1.HistoryPageFragment;

public class MainFragmentAdapter extends FragmentPagerAdapter {

    public MainFragmentAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public MainFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        if (position == 0) {
            return new com.example.miniproject21.UploadPageFragment();

        }
        else if (position == 1) {
            return new com.example.miniproject21.SearchPageFragment();
        }
        else {
            return new HistoryPageFragment();
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}