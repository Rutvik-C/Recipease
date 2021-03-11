package com.example.miniproject21.FragmentSet1;

import android.app.Activity;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    public static RecyclerView toptenRecyclerView;
    public ArrayList<TopTenModel> toptenArrayList;


    ArrayList<String> keys;
    HashMap<String, Integer> foodAndCount = new HashMap<String, Integer>();
    Map<String, Integer> foodAndCount1;
    String[] foodArray;

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> arrayAdapter1;

    /*TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    TextView text6;
    TextView text7;
    TextView text8;
    TextView text9;
    TextView text10;

    CardView cardView1;
    CardView cardView2;
    CardView cardView3;
    CardView cardView4;
    CardView cardView5;
    CardView cardView6;
    CardView cardView7;
    CardView cardView8;
    CardView cardView9;
    CardView cardView10;

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;
    ImageView imageView9;
    ImageView imageView10;*/

    TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_page, container, false);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);

        toptenRecyclerView = getActivity().findViewById(R.id.idtopten);
        // here we have created new array list and added data to it.
        toptenArrayList = new ArrayList<>();
        /*text1 = (TextView) view.findViewById(R.id.text1);
        text2 = (TextView) view.findViewById(R.id.text2);
        text3 = (TextView) view.findViewById(R.id.text3);
        text4 = (TextView) view.findViewById(R.id.text4);
        text5 = (TextView) view.findViewById(R.id.text5);
        text6 = (TextView) view.findViewById(R.id.text6);
        text7 = (TextView) view.findViewById(R.id.text7);
        text8 = (TextView) view.findViewById(R.id.text8);
        text9 = (TextView) view.findViewById(R.id.text9);
        text10 = (TextView) view.findViewById(R.id.text10);

        cardView1 = (CardView) view.findViewById(R.id.cardView1);
        cardView2 = (CardView) view.findViewById(R.id.cardView2);
        cardView3 = (CardView) view.findViewById(R.id.cardView3);
        cardView4 = (CardView) view.findViewById(R.id.cardView4);
        cardView5 = (CardView) view.findViewById(R.id.cardView5);
        cardView6 = (CardView) view.findViewById(R.id.cardView6);
        cardView7 = (CardView) view.findViewById(R.id.cardView7);
        cardView8 = (CardView) view.findViewById(R.id.cardView8);
        cardView9 = (CardView) view.findViewById(R.id.cardView9);
        cardView10 = (CardView) view.findViewById(R.id.cardView10);

        imageView1 = (ImageView) view.findViewById(R.id.img1);
        imageView2 = (ImageView) view.findViewById(R.id.img2);
        imageView3 = (ImageView) view.findViewById(R.id.img3);
        imageView4 = (ImageView) view.findViewById(R.id.img4);
        imageView5 = (ImageView) view.findViewById(R.id.img5);
        imageView6 = (ImageView) view.findViewById(R.id.img6);
        imageView7 = (ImageView) view.findViewById(R.id.img7);
        imageView8 = (ImageView) view.findViewById(R.id.img8);
        imageView9 = (ImageView) view.findViewById(R.id.img9);
        imageView10 = (ImageView) view.findViewById(R.id.img10);*/

        text = (TextView) view.findViewById(R.id.textSearch);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toptenRecyclerView = getActivity().findViewById(R.id.idtopten);
        toptenArrayList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String[] s = new String[10];

        db.collection("foodItems")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document:task.getResult()){
                                if(document.getData().get("count")!=null)
                                foodAndCount.put(document.getId(),Integer.parseInt(document.getData().get("count").toString()));
                            }
                            foodAndCount1 = sortByValue(foodAndCount);
                            keys = new ArrayList<String>(foodAndCount1.keySet());
                            //for(int i=keys.size()-1;i>=keys.size()-10;i--){
                            // toptenArrayList.add(new TopTenModel(keys.get(i), foodAndCount1,R.drawable.rose));
                            //s[keys.size()-i-1]=keys.get(i);
                            //}
                            int i=1;
                            for (Map.Entry<String, Integer> set : foodAndCount1.entrySet()) {
                                toptenArrayList.add(new TopTenModel(set.getKey(), String.valueOf(set.getValue()),R.drawable.rose));
                                i++;
                                if(i>10)
                                    break;
                            }
                            // we are initializing our adapter class and passing our arraylist to it.
                            TopTenCardAdapter toptenAdapter = new TopTenCardAdapter(requireContext(), toptenArrayList);

                            // below line is for setting a layout manager for our recycler view.
                            // here we are creating vertical list so we will provide orientation as vertical
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

                            // in below two lines we are setting layoutmanager and adapter to our recycler view.
                            toptenRecyclerView.setLayoutManager(linearLayoutManager);
                            toptenRecyclerView.setAdapter(toptenAdapter);
                            Collections.sort(keys);
                        } else {
                            Log.i("error", "Error getting documents.", task.getException());
                        }
                        foodArray = new String[keys.size()];
                        foodArray = keys.toArray(foodArray);
                        arrayAdapter1 = new ArrayAdapter<String>(view.getContext(),simple_list_item_1,foodArray);
                        autoCompleteTextView.setAdapter(arrayAdapter1);
                        autoCompleteTextView.setThreshold(1);


                    }
                });
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
        toptenRecyclerView.setVisibility(View.INVISIBLE);
        /*text1.setVisibility(View.INVISIBLE);
        text2.setVisibility(View.INVISIBLE);
        text3.setVisibility(View.INVISIBLE);
        text4.setVisibility(View.INVISIBLE);
        text5.setVisibility(View.INVISIBLE);
        text6.setVisibility(View.INVISIBLE);
        text7.setVisibility(View.INVISIBLE);
        text8.setVisibility(View.INVISIBLE);
        text9.setVisibility(View.INVISIBLE);
        text10.setVisibility(View.INVISIBLE);

        cardView1.setVisibility(View.INVISIBLE);
        cardView2.setVisibility(View.INVISIBLE);
        cardView3.setVisibility(View.INVISIBLE);
        cardView4.setVisibility(View.INVISIBLE);
        cardView5.setVisibility(View.INVISIBLE);
        cardView6.setVisibility(View.INVISIBLE);
        cardView7.setVisibility(View.INVISIBLE);
        cardView8.setVisibility(View.INVISIBLE);
        cardView9.setVisibility(View.INVISIBLE);
        cardView10.setVisibility(View.INVISIBLE);

        imageView1.setVisibility(View.INVISIBLE);
        imageView2.setVisibility(View.INVISIBLE);
        imageView3.setVisibility(View.INVISIBLE);
        imageView4.setVisibility(View.INVISIBLE);
        imageView5.setVisibility(View.INVISIBLE);
        imageView6.setVisibility(View.INVISIBLE);
        imageView7.setVisibility(View.INVISIBLE);
        imageView8.setVisibility(View.INVISIBLE);
        imageView9.setVisibility(View.INVISIBLE);
        imageView10.setVisibility(View.INVISIBLE);*/
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
