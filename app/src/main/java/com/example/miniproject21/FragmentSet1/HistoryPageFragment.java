package com.example.miniproject21.FragmentSet1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.miniproject21.CustomCardAdapter;
import com.example.miniproject21.EditProfile;
import com.example.miniproject21.MainActivity;
import com.example.miniproject21.R;
import com.example.miniproject21.ResultsActivity;
import com.example.miniproject21.TopTenCard.TopTenModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;


public class HistoryPageFragment extends Fragment {
    ArrayList<TopTenModel> historyArrayList;
    ArrayList<String> historyNameArrayList;
    ListView historyListView;
    CustomCardAdapter mCustomCardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        historyListView = view.findViewById(R.id.historyArrayList);
        historyArrayList = new ArrayList<>();
        historyNameArrayList = new ArrayList<>();
        mCustomCardAdapter = new CustomCardAdapter(getContext(), historyNameArrayList, historyArrayList, 0);
        historyListView.setAdapter(mCustomCardAdapter);

        final Button editProfileButton=requireView().findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);

                startActivity(intent);
            }
        });

        Button logoutButton = requireActivity().findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

                assert getActivity() != null;
                getActivity().finish();
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        assert mUser != null;
        DocumentReference mDocumentReference = db.collection("Users").document(mUser.getUid());

        mDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    if (value != null && value.exists()) {
                        Log.i("DATA", value.getData().toString());

                        historyNameArrayList.clear();
                        historyArrayList.clear();
                        for (String name : (ArrayList<String>) Objects.requireNonNull(value.get("history"))) {
                            historyNameArrayList.add(name);
                            historyArrayList.add(new TopTenModel(name, "", R.drawable.rose));

                            mCustomCardAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.i("RES", "Data is NULL");

                    }

                } else {
                    Log.i("ERR", error.toString());

                }

            }
        });

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ResultsActivity.class);
                intent.putExtra("item", historyNameArrayList.get(i));
                startActivity(intent);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

                // Increasing search count
                db.collection("foodItems").document(historyNameArrayList.get(i)).update("search_count", FieldValue.increment(1));

            }
        });

    }

}
