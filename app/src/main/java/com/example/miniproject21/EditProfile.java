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
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerAllergens;
    Spinner spinnerVeg;
    Spinner spinnerSpice;

    AutoCompleteTextView autoAllergen;

    String[] allergens={"None", "Egg","Peanuts","Soy","Wheat","Nuts","Shellfish","Sesame Seeds","Garlic","Maze","Poultry Meat"};
    String[] veg={"None","Jain","Vegetarian", "Non-Vegetarian"};
    String[] spice={"None","Spicy","Medium", "Sweet"};

    List<String> list = new ArrayList<String>();

    ListView allergenListView;
    ArrayAdapter<String> adapterList;
    ArrayAdapter<String> adapterAllergens;

    String strVeg,strSpice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        assert mUser != null;
        DocumentReference mDocumentReference = db.collection("Users").document(mUser.getUid());
        mDocumentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    if (value != null && value.exists()) {
                        Log.i("DATA EP", value.getData().toString());

                        if (value.get("spice") != null && value.get("veg") != null && value.get("allergens") != null) {
                            String spice = value.get("spice").toString();
                            String veg = value.get("veg").toString();
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
        adapterAllergens = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allergens );
        autoAllergen.setAdapter(adapterAllergens);
        autoAllergen.setThreshold(1);


        spinnerVeg = (Spinner) findViewById(R.id.spinnerVeg);
        ArrayAdapter<String> adapterVeg = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, veg );
        adapterVeg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVeg.setAdapter(adapterVeg);
        spinnerVeg.setOnItemSelectedListener(this);

        spinnerSpice = (Spinner) findViewById(R.id.spinnerSpice);
        ArrayAdapter<String> adapterSpice = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spice );
        adapterSpice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpice.setAdapter(adapterSpice);
        spinnerSpice.setOnItemSelectedListener(this);

        adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        allergenListView = findViewById(R.id.listViewAllergens);
        allergenListView.setAdapter(adapterList);

        allergenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //to delete allergen on clicking
                list.remove(i);
                adapterList.notifyDataSetChanged();
            }
        });
        autoAllergen.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
                if(list.contains(arg0.getItemAtPosition(arg2).toString())){
                    Toast.makeText(EditProfile.this, "Item already present in the list", Toast.LENGTH_SHORT).show();
                    adapterAllergens.notifyDataSetChanged();
                }
                else{
                    list.add(arg0.getItemAtPosition(arg2).toString());
                }
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
            case R.id.spinnerSpice:
                Log.i("Hi","3");
                strSpice=spice[i];
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
