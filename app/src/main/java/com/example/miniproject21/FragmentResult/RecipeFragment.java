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

import com.example.miniproject21.R;
import com.example.miniproject21.RecipeCard.RecipeCardAdapter;
import com.example.miniproject21.RecipeCard.RecipeCardModel;
import com.example.miniproject21.ResultsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class RecipeFragment extends Fragment {

    public static RecyclerView recipeRecyclerView;
    public ArrayList<RecipeCardModel> recipeArrayList;

    public static RecyclerView infoRecyclerView;
    public ArrayList<RecipeCardModel> infoArrayList;

    ArrayList<String> ingredients;
    ArrayList<String> steps;
    FirebaseFirestore db;
    FirebaseUser mUser;


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
        db = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipeRecyclerView = getActivity().findViewById(R.id.idRecipe);
        infoRecyclerView=getActivity().findViewById(R.id.idRecipefirst);
        ingredients = new ArrayList<String>();
        steps = new ArrayList<String>();
        infoArrayList=new ArrayList<>();
        // here we have created new array list and added data to it.

        DocumentReference docRef = db.collection("foodItems").document(ResultsActivity.item);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        recipeArrayList = new ArrayList<>();
                        recipeArrayList.add(new RecipeCardModel("Ingredients", ""));
                        ingredients=(ArrayList<String>)document.getData().get("ingredients_recipe");
                        for(int i=0;i<ingredients.size();i++){
                            recipeArrayList.add(new RecipeCardModel("Ingredient no. "+String.valueOf(i+1), ingredients.get(i)));
                        }
                        recipeArrayList.add(new RecipeCardModel("Preparation Time",document.getData().get("r_prep_time").toString()));
                        recipeArrayList.add(new RecipeCardModel("Cooking Time",document.getData().get("r_cook_time").toString()));
                        recipeArrayList.add(new RecipeCardModel("Total Time",document.getData().get("r_total_time").toString()));
                        recipeArrayList.add(new RecipeCardModel("Steps", ""));
                        steps=(ArrayList<String>)document.getData().get("r_steps");
                        for(int i=0;i<steps.size();i++){
                            recipeArrayList.add(new RecipeCardModel("Step no. "+String.valueOf(i+1), steps.get(i)));
                        }
                        // we are initializing our adapter class and passing our arraylist to it.
                        RecipeCardAdapter recipeAdapter = new RecipeCardAdapter(requireContext(), recipeArrayList);

                        // below line is for setting a layout manager for our recycler view.
                        // here we are creating vertical list so we will provide orientation as vertical
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

                        // in below two lines we are setting layoutmanager and adapter to our recycler view.
                        recipeRecyclerView.setLayoutManager(linearLayoutManager);
                        recipeRecyclerView.setAdapter(recipeAdapter);

                        infoArrayList.add(new RecipeCardModel("Test0","Test0"));
                        infoArrayList.add(new RecipeCardModel("Test1","Test1"));
                        RecipeCardAdapter infoAdapter = new RecipeCardAdapter(requireContext(), infoArrayList);
                        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                        infoRecyclerView.setLayoutManager(linearLayoutManager1);
                        infoRecyclerView.setAdapter(infoAdapter);

                    } else {
                        Log.i("ttt", "No such document");
                    }
                } else {
                    Log.i("ppp", "get failed with ", task.getException());
                }
            }
        });

    }
}
