package com.example.miniproject21.FragmentSet1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproject21.ApiHelper.ApiInterface;
import com.example.miniproject21.ApiHelper.FoodRecommendationResult;
import com.example.miniproject21.CustomCardAdapter;
import com.example.miniproject21.HomePage;
import com.example.miniproject21.R;
import com.example.miniproject21.ResultsActivity;
import com.example.miniproject21.TopTenCard.TopTenModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.layout.simple_list_item_1;

public class SearchPageFragment extends Fragment {
    ArrayList<String> topNameArrayList;
    ListView topTenListView;
    CustomCardAdapter mCustomCardAdapter;
    ProgressBar mProgressBar;
    TextView fetchTextView;

    ArrayList<String> keys = new ArrayList<String>();
    String[] foodArray;
    ArrayAdapter arrayAdapter;

    AutoCompleteTextView autoCompleteTextView;

    TextView text;

    FrameLayout frameLayout;

    public void addToHistoryIncreaseCountAndProceed(String item) {
        Intent intent = new Intent(getContext(), ResultsActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        // Increasing search count
        db.collection("foodItems").document(item).update("search_count", FieldValue.increment(1));

        // Adding to user history
        assert mUser != null;
        db.collection("Users").document(mUser.getUid()).update("history", FieldValue.arrayUnion(item));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_page, container, false);

        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        text = (TextView) view.findViewById(R.id.textSearch);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame);

        // Inflate the layout for this fragment
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = view.findViewById(R.id.recommendationProgressBar);
        fetchTextView = view.findViewById(R.id.fetchingTextView);

        topNameArrayList = new ArrayList<>();
        topTenListView = view.findViewById(R.id.topTenListView);
        mCustomCardAdapter = new CustomCardAdapter(requireContext(), topNameArrayList, new ArrayList<Boolean>(), false);
        topTenListView.setAdapter(mCustomCardAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("foodItems")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                keys.add(document.getId());
                            }
                        } else {
                            Log.i("error", "Error getting documents.", task.getException());
                        }

                        foodArray = new String[keys.size()];
                        foodArray = keys.toArray(foodArray);
                        arrayAdapter = new ArrayAdapter<String>(view.getContext(), simple_list_item_1, foodArray);
                        autoCompleteTextView.setAdapter(arrayAdapter);
                        autoCompleteTextView.setThreshold(1);
                    }
                });

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        mProgressBar.setVisibility(View.VISIBLE);
        fetchTextView.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL_RECOMMENDATION)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        assert mUser != null;
        Call<FoodRecommendationResult> mCall = apiInterface.getUserRecommendation(mUser.getUid());
        mCall.enqueue(new Callback<FoodRecommendationResult>() {
            @Override
            public void onResponse(Call<FoodRecommendationResult> call, Response<FoodRecommendationResult> response) {
                FoodRecommendationResult mResult = response.body();

                assert mResult != null;
                ArrayList<String> similarItems = mResult.getRecommendation();

                topNameArrayList.clear();
                for (String s : similarItems) {
                    topNameArrayList.add(s);

                    mCustomCardAdapter.notifyDataSetChanged();
                }

                mProgressBar.setVisibility(View.INVISIBLE);
                fetchTextView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<FoodRecommendationResult> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching recommendations", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
                fetchTextView.setVisibility(View.INVISIBLE);
            }
        });

//        CollectionReference mCollectionReference = db.collection("foodItems");
//        mCollectionReference.orderBy("search_count", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    topNameArrayList.clear();
//
//                    for (QueryDocumentSnapshot mDocument : Objects.requireNonNull(task.getResult())) {
//                        // topTenArrayList.add(new TopTenModel(mDocument.getId(), Objects.requireNonNull(mDocument.get("search_count")).toString(), R.drawable.rose));
//                        topNameArrayList.add(mDocument.getId());
//                        mCustomCardAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });

        frameLayout.setOnTouchListener(autoOnTouch);

        autoCompleteTextView.setOnTouchListener(autoOnTouch);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                addToHistoryIncreaseCountAndProceed(arg0.getItemAtPosition(arg2).toString());

            }
        });

        topTenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addToHistoryIncreaseCountAndProceed(topNameArrayList.get(i));

            }
        });

    }

    private final View.OnTouchListener autoOnTouch = new View.OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View v, MotionEvent event) {
            if(autoCompleteTextView.getText().toString().length()==0){
                text.setVisibility(View.VISIBLE);
                topTenListView.setVisibility(View.VISIBLE);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                text.setVisibility(View.INVISIBLE);
                topTenListView.setVisibility(View.INVISIBLE);
            }
            return false;
        }
    };


}