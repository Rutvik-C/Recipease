package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.miniproject21.FragmentResult.GeneralFragment;
import com.example.miniproject21.FragmentResult.NutritionFragment;
import com.example.miniproject21.FragmentResult.RecipeFragment;
import com.example.miniproject21.FragmentResult.VideoFragment;

public class ResultFragmentAdapter extends FragmentPagerAdapter {

    public ResultFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new GeneralFragment();

        } else if (position == 1) {
            return new RecipeFragment();

        } else if (position == 2) {
            return new NutritionFragment();

        } else {
            return new VideoFragment();

        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
