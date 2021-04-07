package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miniproject21.ApiHelper.ApiInterface;
import com.example.miniproject21.ApiHelper.FoodRecommendationResult;
import com.example.miniproject21.R;
import com.example.miniproject21.TopTenCard.TopTenModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultsActivity extends AppCompatActivity {

    static ViewPager viewPager;
    static BottomNavigationView bottomNavigationView;
    static Menu menu;
    public static String item;
    public static boolean cloudLiked;
    public static ArrayList<Integer> currentIngredients;
    public static String vLink;

    ArrayList<String> stringArrayList;
    @SuppressLint("StaticFieldLeak")
    public static CustomCardAdapter mAdapter;

    public static Menu getMenu() {
        return menu;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        item = intent.getStringExtra("item");

        stringArrayList = new ArrayList<>();
        currentIngredients = new ArrayList<>();

        Log.i("INTENT", item);

        viewPager = findViewById(R.id.resultsViewPager);
        ResultFragmentAdapter resultFragmentAdapter = new ResultFragmentAdapter(getSupportFragmentManager());

        viewPager.setAdapter(resultFragmentAdapter);
        viewPager.setCurrentItem(0);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.generalOption:
                        viewPager.setCurrentItem(0);
                        menu.getItem(0).setChecked(true);
                        break;

                    case R.id.recipeOption:
                        viewPager.setCurrentItem(1);
                        menu.getItem(1).setChecked(true);
                        break;

                    case R.id.nutritionOption:
                        viewPager.setCurrentItem(2);
                        menu.getItem(2).setChecked(true);
                        break;

                    case R.id.videoOption:
                        viewPager.setCurrentItem(3);
                        menu.getItem(3).setChecked(true);
                        break;

                    case R.id.recommendationOption:
                        viewPager.setCurrentItem(4);
                        menu.getItem(4).setChecked(true);
                        break;
                }

                return false;
            }
        });

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = firebaseAuth.getCurrentUser();

        final HashMap<Integer, String> mMap = new HashMap<>();
        mMap.put(0, "Jain");
        mMap.put(1, "Vegetarian");
        mMap.put(2, "Non Vegetarian");

        assert mUser != null;
        DocumentReference docRef = db.collection("Users").document(mUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot userDocument = task.getResult();
                    if (userDocument != null && userDocument.exists()) {

                        ArrayList<String> likedItems = (ArrayList<String>) userDocument.get("liked");
                        if (likedItems.contains(item)) {
                            cloudLiked = true;

                        } else {
                            cloudLiked = false;

                        }

                        DocumentReference docRef = db.collection("foodItems").document(ResultsActivity.item);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {

                                        vLink = (String) document.get("v_link");

                                        String result = "";

                                        long foodCat = (long) document.get("category");
                                        long foodSpice = (long) document.get("spice_level");
                                        ArrayList<String> foodContents = (ArrayList<String>) document.get("nv_ingredients");
                                        Log.i("FOOD INFO", "" + foodCat + " " + foodSpice + " " + foodContents);

                                        long userCat = (long) userDocument.get("preference");
                                        long userSpice = (long) userDocument.get("spice");
                                        ArrayList<String> userAllergen = (ArrayList<String>) userDocument.get("allergens");
                                        Log.i("USER INFO", "" + userCat + " " + userSpice + " " + userAllergen);

                                        if (userCat < foodCat) {
                                            result += "The food is marked " + mMap.get((int) foodCat) + "!\n";
                                        }
                                        if (userSpice + 2 < foodSpice) {
                                            result += "The food might be spicy for you!\n";
                                        }
                                        int count=0;

                                        currentIngredients.clear();
                                        Log.i("PRE FETCHED", "" + HomePage.allItems);
                                        for (String s1 : foodContents) {
                                            Log.i("~", s1);
                                            currentIngredients.add(HomePage.allItems.indexOf(s1));

                                            for (String s2 : userAllergen) {
                                                if (s1.equals(s2)) {
                                                    if (count>0)
                                                        result += ", "+s1;
                                                    else {
                                                        result+="This dish may contain ";
                                                        result += s1 + " ";
                                                        count=1;
                                                    }
                                                }
                                            }
                                        }

                                        if (!result.equals("")) {
                                            new AlertDialog.Builder(ResultsActivity.this,R.style.AlertDialog)
                                                    .setTitle("Alert!")
                                                    .setMessage(result)
                                                    .setCancelable(false)
                                                    .setPositiveButton("I Understand, Proceed", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();

                                                        }
                                                    })
                                                    .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            ResultsActivity.this.onBackPressed();

                                                        }
                                                    })
                                                    .create()
                                                    .show();

                                        }

                                    }
                                }

                            }
                        });
                    }
                }
            }
        });

        mAdapter = new CustomCardAdapter(this, stringArrayList);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL_RECOMMENDATION)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<FoodRecommendationResult> mCall = apiInterface.getSimilarFoodItems(ResultsActivity.item);

        mCall.enqueue(new Callback<FoodRecommendationResult>() {
            @Override
            public void onResponse(Call<FoodRecommendationResult> call, Response<FoodRecommendationResult> response) {
                FoodRecommendationResult mResult = response.body();

                assert mResult != null;
                ArrayList<String> similarItems = mResult.getRecommendation();

                for (String s : similarItems) {
                    stringArrayList.add(s);

                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<FoodRecommendationResult> call, Throwable t) {
                Log.i("ERROR", t.getMessage());

            }
        });

    }
}
