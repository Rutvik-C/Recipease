package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerVeg;
    int spice;

    AutoCompleteTextView autoAllergen;

    // String[] allergens={"None", "Egg","Peanuts","Soy","Wheat","Nuts","Shellfish","Sesame Seeds","Garlic","Maze","Poultry Meat"};
    String[] veg={"Any", "Jain", "Vegetarian", "Non-Vegetarian"};
    ArrayList<String> allergens;

    ArrayList<String> list = new ArrayList<String>();

    ListView allergenListView;
    EditProfileAdapter mAdapter;
    ArrayAdapter<String> adapterAllergens;

    String strVeg,strSpice;

    public void dismissAllergen(View view) {
        ImageView mImageView = (ImageView) view;

        int index = Integer.parseInt(mImageView.getTag().toString());

        list.remove(index);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Log.i("INIT", "Edit profile");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        // FETCHING ALLERGENS FROM CLOUD
        DocumentReference mDocumentReference1 = db.collection("predictableItems").document("#Allergens");
        mDocumentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot mDocumentSnapshot = task.getResult();
                    if (mDocumentSnapshot != null && mDocumentSnapshot.exists()) {
                        allergens = (ArrayList<String>) mDocumentSnapshot.get("items");

                        Log.i("ALLERGENS", allergens + "");
                    }

                } else {
                    Log.i("ERR", "" + task.getException());
                }
            }
        });

        assert mUser != null;
        DocumentReference mDocumentReference = db.collection("Users").document(mUser.getUid());
        mDocumentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    if (value != null && value.exists()) {
                        Log.i("DATA EP", value.getData().toString());

                        if (value.get("spice") != null && value.get("veg") != null && value.get("allergens") != null) {
                            spice = Integer.parseInt(value.get("spice").toString());
                            String veg = value.get("veg").toString();
                            Log.i("spice", String.valueOf(spice));
                            ArrayList<String> mArrayList = (ArrayList<String>) value.get("allergens");

                            // PREF FETCHED HERE
                        }

                    } else {
                        Log.i("RES EP", "Data is NULL");

                    }

                } else {
                    Log.i("ERR EP", error.toString());

                }
            }
        });

        autoAllergen = findViewById(R.id.autoAllergens);
        adapterAllergens = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allergens);
        autoAllergen.setAdapter(adapterAllergens);
        autoAllergen.setThreshold(1);


        spinnerVeg = (Spinner) findViewById(R.id.spinnerVeg);
        ArrayAdapter<String> adapterVeg = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, veg );
        adapterVeg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVeg.setAdapter(adapterVeg);
        spinnerVeg.setOnItemSelectedListener(this);

        mAdapter = new EditProfileAdapter(this, list);

        allergenListView = findViewById(R.id.listViewAllergens);
        allergenListView.setAdapter(mAdapter);

        autoAllergen.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
                if(list.contains(arg0.getItemAtPosition(arg2).toString())){
                    Toast.makeText(EditProfile.this, "Item already present in the list", Toast.LENGTH_SHORT).show();
                    adapterAllergens.notifyDataSetChanged();
                }
                else{
                    list.add(arg0.getItemAtPosition(arg2).toString());
                    autoAllergen.setText("");
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        SeekBar s = (SeekBar) findViewById(R.id.seekBar);
        s.setProgress(5);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                spice =i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.spinnerVeg:
                Log.i("Hi","2");
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                strVeg=veg[i];
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void saveChanges(View view) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> mMap = new HashMap<>();
        mMap.put("spice", strSpice);
        mMap.put("veg", strVeg);
        if (list.size() != 0 && !list.get(0).equals("None")) {
            mMap.put("allergens", list);
        }

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        db.collection("Users").document(mUser.getUid()).set(mMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(EditProfile.this, HomePage.class);
                    startActivity(intent);
                    finish();
                } else {
                    assert task.getException() != null;
                    Toast.makeText(EditProfile.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void goBack(View view){
        //go to history page
    }
}

//allergens
//veg non veg
//spicy
//change password
