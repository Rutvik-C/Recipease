package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerAllergens;
    Spinner spinnerVeg;
    Spinner spinnerSpice;

    String[] allergens={"None", "Egg","Peanuts","Soy","Wheat","Nuts","Shellfish","Sesame Seeds","Garlic","Maze","Poultry Meat"};
    String[] veg={"None","Jain","Vegetarian", "Non-Vegetarian"};
    String[] spice={"None","Spicy","Medium", "Sweet"};

    List<String> list = new ArrayList<String>();

    ListView allergenListView;
    ArrayAdapter<String> adapterList;

    String strVeg,strSpice;

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        spinnerAllergens = (Spinner) findViewById(R.id.spinnerAllergens);
        ArrayAdapter<String> adapterAllergens = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allergens );
        adapterAllergens.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAllergens.setAdapter(adapterAllergens);
        spinnerAllergens.setOnItemSelectedListener(this);


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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.spinnerAllergens:
                Log.i("Hi","1");
                if(list.contains("None")){
                    list.remove(0);
                    list.add(allergens[i]);
                    adapterList.notifyDataSetChanged();
                    Toast.makeText(this, "Added Allergen",  Toast.LENGTH_SHORT).show();
                }
                else if(allergens[i]=="None" && list.size()!=0){
                    Toast.makeText(this, "Allergens already present so cannot add none",  Toast.LENGTH_SHORT).show();
                    //none is selected
                }
                else if(list.contains(allergens[i])){
                    Toast.makeText(this, "Allergen already present!",  Toast.LENGTH_SHORT).show();
                    //allergen already present
                }
                else{
                    list.add(allergens[i]);
                    adapterList.notifyDataSetChanged();
                    Toast.makeText(this, "Added Allergen",  Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.spinnerVeg:
                Log.i("Hi","2");
                strVeg=veg[i];
                break;
            case R.id.spinnerSpice:
                Log.i("Hi","3");
                strSpice=spice[i];
                break;
            default:
                break;
        }
        //Toast.makeText(getApplicationContext(),allergens[i], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void saveChanges(View view){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).child("Spice").setValue(strSpice);
        databaseReference.child(user.getUid()).child("Veg").setValue(strVeg);
        if(!list.get(0).equals("None")){
            for(int i=0;i<list.size();i++){
                databaseReference.child(user.getUid()).child("allergens").child(String.valueOf(i+1)).setValue(list.get(i));
            }
        }
        Intent intent = new Intent(EditProfile.this, HomePage.class);
        startActivity(intent);
        finish();
    }
    public void goBack(View view){
        //go to history page
    }
}

//allergens
//veg non veg
//spicy
//change password
