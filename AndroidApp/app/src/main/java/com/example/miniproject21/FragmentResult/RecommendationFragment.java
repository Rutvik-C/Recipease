package com.example.miniproject21.FragmentResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.miniproject21.ApiHelper.ApiInterface;
import com.example.miniproject21.ApiHelper.FoodRecommendationResult;
import com.example.miniproject21.CustomCardAdapter;
import com.example.miniproject21.R;
import com.example.miniproject21.ResultsActivity;
import com.example.miniproject21.TopTenCard.TopTenModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecommendationFragment extends Fragment {



    ListView mListView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            ResultsActivity.getMenu().getItem(4).setChecked(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommendation_result, container, false);


        mListView = view.findViewById(R.id.similarItemsListView);
        mListView.setAdapter(ResultsActivity.mAdapter);



        return view;
    }
}
