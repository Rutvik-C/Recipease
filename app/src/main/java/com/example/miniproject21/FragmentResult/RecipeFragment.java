package com.example.miniproject21.FragmentResult;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject21.R;
import com.example.miniproject21.RecipeCard.RecipeCardAdapter;
import com.example.miniproject21.RecipeCard.RecipeCardModel;
import com.example.miniproject21.ResultsActivity;

import java.util.ArrayList;


public class RecipeFragment extends Fragment {

    public static RecyclerView courseRV;
    public ArrayList<RecipeCardModel> courseModelArrayList;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            ResultsActivity.getMenu().getItem(1).setChecked(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_result, container, false);

        // code here

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courseRV = getActivity().findViewById(R.id.idRVCourse);
        // here we have created new array list and added data to it.
        courseModelArrayList = new ArrayList<>();
        courseModelArrayList.add(new RecipeCardModel("DSA in Java", 4, R.drawable.logo));
        courseModelArrayList.add(new RecipeCardModel("Java Course", 3, R.drawable.logo));
        courseModelArrayList.add(new RecipeCardModel("C++ Course", 4, R.drawable.logo));
        courseModelArrayList.add(new RecipeCardModel("DSA in C++", 4, R.drawable.logo));
        courseModelArrayList.add(new RecipeCardModel("Kotlin for Android", 4, R.drawable.logo));
        courseModelArrayList.add(new RecipeCardModel("Java for Android", 4, R.drawable.logo));
        courseModelArrayList.add(new RecipeCardModel("HTML and CSS", 4, R.drawable.logo));

        // we are initializing our adapter class and passing our arraylist to it.
        RecipeCardAdapter courseAdapter = new RecipeCardAdapter(requireContext(), courseModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);
    }
}
