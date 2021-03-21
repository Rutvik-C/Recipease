package com.example.miniproject21.FragmentSet1;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
    //FirebaseAuth firebaseAuth;
    //FirebaseUser user = firebaseAuth.getCurrentUser();
    // public static RecyclerView toptenRecyclerView;
    ArrayList<TopTenModel> topTenArrayList;
    ArrayList<String> topNameArrayList;
    ListView topTenListView;
    CustomCardAdapter mCustomCardAdapter;

    ArrayList<String> keys;
    HashMap<String, Integer> foodAndCount = new HashMap<String, Integer>();
    Map<String, Integer> foodAndCount1;
    String[] foodArray;

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> arrayAdapter1;
    TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_page, container, false);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);

        //toptenRecyclerView = getActivity().findViewById(R.id.idtopten);
        // here we have created new array list and added data to it.
        text = (TextView) view.findViewById(R.id.textSearch);

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

        topTenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ResultsActivity.class);
                intent.putExtra("item", topNameArrayList.get(i));

                startActivity(intent);

                // LOGIC TO ADD IN HISTORY
            }
        });


//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        final String[] s = new String[10];
//
//        db.collection("foodItems")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document:task.getResult()){
//                                if(document.getData().get("count")!=null)
//                                foodAndCount.put(document.getId(),Integer.parseInt(document.getData().get("count").toString()));
//                            }
//                            foodAndCount1 = sortByValue(foodAndCount);
//                            keys = new ArrayList<String>(foodAndCount1.keySet());
//                            //for(int i=keys.size()-1;i>=keys.size()-10;i--){
//                            // toptenArrayList.add(new TopTenModel(keys.get(i), foodAndCount1,R.drawable.rose));
//                            //s[keys.size()-i-1]=keys.get(i);
//                            //}
//                            int i=1;
//                            for (Map.Entry<String, Integer> set : foodAndCount1.entrySet()) {
//                                toptenArrayList.add(new TopTenModel(set.getKey(), String.valueOf(set.getValue()),R.drawable.rose));
//                                i++;
//                                if(i>10)
//                                    break;
//                            }
//                            // we are initializing our adapter class and passing our arraylist to it.
//                            TopTenCardAdapter toptenAdapter = new TopTenCardAdapter(requireContext(), toptenArrayList);
//
//                            // below line is for setting a layout manager for our recycler view.
//                            // here we are creating vertical list so we will provide orientation as vertical
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
//
//                            // in below two lines we are setting layoutmanager and adapter to our recycler view.
//                            toptenRecyclerView.setLayoutManager(linearLayoutManager);
//                            toptenRecyclerView.setAdapter(toptenAdapter);
//                            Collections.sort(keys);
//                        } else {
//                            Log.i("error", "Error getting documents.", task.getException());
//                        }
//                        foodArray = new String[keys.size()];
//                        foodArray = keys.toArray(foodArray);
//                        arrayAdapter1 = new ArrayAdapter<String>(view.getContext(),simple_list_item_1,foodArray);
//                        autoCompleteTextView.setAdapter(arrayAdapter1);
//                        autoCompleteTextView.setThreshold(1);
//
//
//                    }
//                });


        autoCompleteTextView.setOnTouchListener(autoOnTouch);
        //autoCompleteTextView.setOnKeyListener(autoOnKey);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.i("selected",arg0.getItemAtPosition(arg2).toString());
                Toast.makeText(view.getContext(), arg0.getItemAtPosition(arg2).toString()+" details will be displayed ",  Toast.LENGTH_SHORT).show();

            }
        });




    }

    private View.OnTouchListener autoOnTouch = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_UP) {
                hideCardAndText();
            }
            return false;
        }
    };

//    private static View.OnKeyListener autoOnKey = new View.OnKeyListener() {
//        public boolean onKey(View v, int keyCode, KeyEvent event) {
//            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
//                //your code
//                return true;
//            } else {
//                return false;
//            }
//        }
//    };


    public void hideCardAndText(){
        text.setVisibility(View.INVISIBLE);
        //toptenRecyclerView.setVisibility(View.INVISIBLE);
    }
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
