package com.example.miniproject21.FragmentSet1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miniproject21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;

public class SearchPageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.i("hi1","hi1");
        View view = inflater.inflate(R.layout.fragment_search_page, container, false);
        //Log.i("hi2","hi2");
        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        TextView text3 = (TextView) view.findViewById(R.id.text3);
        TextView text4 = (TextView) view.findViewById(R.id.text4);
        TextView text5 = (TextView) view.findViewById(R.id.text5);
        TextView text6 = (TextView) view.findViewById(R.id.text6);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String[] s = new String[6];
        //Log.i("hi3","hi3");

        db.collection("foodItems")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Log.i("hi4","hi4");
                if (task.isSuccessful()) {

                    int i=0;
                    for (QueryDocumentSnapshot document:task.getResult()){
                        if(i>5){
                            break;
                        }
                        s[i] = "    "+document.getId();
                        i++;
                    }
                } else {
                    Log.i("error", "Error getting documents.", task.getException());
                }
            }
        });
        //Log.i("hi5","hi5");
        text1.setText(s[0]);
        text2.setText(s[1]);
        text3.setText(s[2]);
        text4.setText(s[3]);
        text5.setText(s[4]);
        text6.setText(s[5]);




        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_page, container, false);
    }
}
