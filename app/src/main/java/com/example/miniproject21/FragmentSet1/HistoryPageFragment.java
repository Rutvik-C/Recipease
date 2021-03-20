package com.example.miniproject21.FragmentSet1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.miniproject21.EditProfile;
import com.example.miniproject21.HistoryCard.HistoryCardAdapter;
import com.example.miniproject21.HistoryCard.HistoryModel;
import com.example.miniproject21.HomePage;
import com.example.miniproject21.MainActivity;
import com.example.miniproject21.R;
import com.example.miniproject21.RecipeCard.RecipeCardAdapter;
import com.example.miniproject21.RecipeCard.RecipeCardModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;


public class HistoryPageFragment extends Fragment {
    public static RecyclerView historyRecyclerView;
    public ArrayList<HistoryModel> historyArrayList;
    ListView mListview;
    ArrayAdapter<String> mArrayAdapter;
    ArrayList<String> mArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        historyRecyclerView = getActivity().findViewById(R.id.idhistory);
        historyArrayList = new ArrayList<>();
        historyArrayList.add(new HistoryModel("Idli", "4",R.drawable.rose));
        historyArrayList.add(new HistoryModel("Dosa", "4",R.drawable.rose));
        historyArrayList.add(new HistoryModel("Maggi", "4",R.drawable.rose));
        HistoryCardAdapter recipeAdapter = new HistoryCardAdapter(requireContext(), historyArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        historyRecyclerView.setLayoutManager(linearLayoutManager);
        historyRecyclerView.setAdapter(recipeAdapter);


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

        //mDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
           // @Override
            //public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
          //      if (error == null) {
            //        if (value != null && value.exists()) {
              //          Log.i("DATA", value.getData().toString());

                        //mArrayList.clear();
                        //mArrayList.addAll((ArrayList<String>) Objects.requireNonNull(value.get("history")));
                        //mArrayAdapter.notifyDataSetChanged();

                //    } else {
                  //      Log.i("RES", "Data is NULL");

                    //}

               // } else {
                 //   Log.i("ERR", error.toString());

                //}

         //   }
       // });

    }

}
