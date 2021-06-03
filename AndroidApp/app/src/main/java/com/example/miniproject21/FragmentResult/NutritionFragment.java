package com.example.miniproject21.FragmentResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject21.NutrientCard.NutrientCardAdapter;
import com.example.miniproject21.NutrientCard.NutrientCardModel;
import com.example.miniproject21.R;
import com.example.miniproject21.RecipeCard.RecipeCardAdapter;
import com.example.miniproject21.RecipeCard.RecipeCardModel;
import com.example.miniproject21.ResultsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NutritionFragment extends Fragment {
    FirebaseFirestore db;
    FirebaseUser mUser;

    String calories="";
    String fats="";
    String cholesterol="";
    String sodium="";
    String carbohydrates="";
    String protein="";

    public static RecyclerView nutrientRecyclerView;
    public ArrayList<NutrientCardModel> nutrientArrayList;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            ResultsActivity.getMenu().getItem(2).setChecked(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_result, container, false);

        // code here
        db = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nutrientRecyclerView = getActivity().findViewById(R.id.idNutrient);
        // here we have created new array list and added data to it.
        nutrientArrayList = new ArrayList<>();

        DocumentReference docRef = db.collection("foodItems").document(ResultsActivity.item);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        calories=document.getData().get("nv_calories").toString();
                        fats=document.getData().get("nv_fat").toString();
                        cholesterol=document.getData().get("nv_cholesterol").toString();
                        carbohydrates=document.getData().get("nv_carbohydrates").toString();
                        sodium=document.getData().get("nv_sodium").toString();
                        protein=document.getData().get("nv_protein").toString();
                        Log.i("hey",calories);
                        function();

                    } else {
                        Log.i("ttt", "No such document");
                    }
                } else {
                    Log.i("ppp", "get failed with ", task.getException());
                }
            }
        });

    }
    void function(){
        // we are initializing our adapter class and passing our arraylist to it.
        if(nutrientArrayList.size()==0) {
            nutrientArrayList.add(new NutrientCardModel("Calories",calories));
            nutrientArrayList.add(new NutrientCardModel("Cholesterol",cholesterol));
            nutrientArrayList.add(new NutrientCardModel("Carbohydrates",carbohydrates));
            nutrientArrayList.add(new NutrientCardModel("Fats",fats));
            nutrientArrayList.add(new NutrientCardModel("Sodium",sodium));
            nutrientArrayList.add(new NutrientCardModel("Proteins",protein));

        }
        NutrientCardAdapter nutrientAdapter = new NutrientCardAdapter(requireContext(), nutrientArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        nutrientRecyclerView.setLayoutManager(linearLayoutManager);
        nutrientRecyclerView.setAdapter(nutrientAdapter);
    }
}
