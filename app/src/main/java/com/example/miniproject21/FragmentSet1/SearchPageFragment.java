package com.example.miniproject21.FragmentSet1;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproject21.CustomCardAdapter;
import com.example.miniproject21.MainActivity;
import com.example.miniproject21.R;
import com.example.miniproject21.RecipeCard.RecipeCardAdapter;
import com.example.miniproject21.RecipeCard.RecipeCardModel;
import com.example.miniproject21.ResultsActivity;
import com.example.miniproject21.TopTenCard.TopTenCardAdapter;
import com.example.miniproject21.TopTenCard.TopTenModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_list_item_1;

public class SearchPageFragment extends Fragment {
    ArrayList<TopTenModel> topTenArrayList;
    ArrayList<String> topNameArrayList;
    ListView topTenListView;
    CustomCardAdapter mCustomCardAdapter;

    ArrayList<String> keys = new ArrayList<String>();
    String[] foodArray;
    ArrayAdapter arrayAdapter;

    AutoCompleteTextView autoCompleteTextView;

    TextView text;

    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_page, container, false);

        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        text = (TextView) view.findViewById(R.id.textSearch);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topTenArrayList = new ArrayList<>();
        topNameArrayList = new ArrayList<>();
        topTenListView = view.findViewById(R.id.topTenListView);
        mCustomCardAdapter = new CustomCardAdapter(getContext(), topNameArrayList, topTenArrayList, 1);
        topTenListView.setAdapter(mCustomCardAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("foodItems")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
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

        CollectionReference mCollectionReference = db.collection("foodItems");
        mCollectionReference.orderBy("search_count", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    topTenArrayList.clear();
                    topNameArrayList.clear();

                    for (QueryDocumentSnapshot mDocument : task.getResult()) {
                        topTenArrayList.add(new TopTenModel(mDocument.getId(), mDocument.get("search_count").toString(), R.drawable.rose));
                        topNameArrayList.add(mDocument.getId());
                        mCustomCardAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        frameLayout.setOnTouchListener(autoOnTouch);

        autoCompleteTextView.setOnTouchListener(autoOnTouch);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.i("selected",arg0.getItemAtPosition(arg2).toString());
                Toast.makeText(view.getContext(), arg0.getItemAtPosition(arg2).toString()+" details will be displayed ",  Toast.LENGTH_SHORT).show();

            }
        });

        topTenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ResultsActivity.class);
                intent.putExtra("item", topNameArrayList.get(i));

                startActivity(intent);

                // LOGIC TO ADD IN HISTORY
            }
        });


    }

    private View.OnTouchListener autoOnTouch = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {
            if(autoCompleteTextView.getText().toString().length()==0){
                topTenListView.setVisibility(View.VISIBLE);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                hideCardAndText();
            }
            return false;
        }
    };

    public void hideCardAndText() {
        text.setVisibility(View.INVISIBLE);
        topTenListView.setVisibility(View.INVISIBLE);
    }

}