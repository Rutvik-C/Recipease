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
import android.widget.Button;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int spice;

    AutoCompleteTextView autoAllergen;

    Spinner spinnerVeg;

    ArrayList<String> veg=new ArrayList<String>();
    ArrayList<String> allergens;
    ArrayList<String> mArrayList;

    ListView allergenListView;
    EditProfileAdapter mAdapter;
    ArrayAdapter<String> adapterAllergens;
    ArrayAdapter<String> adapterVeg;
    Button logoutButton;
    int preference = 0;

    SeekBar s;

    String vegPreference;

    public void dismissAllergen(View view) {
        ImageView mImageView = (ImageView) view;
        int index = Integer.parseInt(mImageView.getTag().toString());
        mArrayList.remove(index);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        veg.add("Any");
        veg.add("Jain");
        veg.add("Vegetarian");
        veg.add("Non-Vegetarian");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        s = (SeekBar) findViewById(R.id.seekBar);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                spice = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        mArrayList = new ArrayList<>();
        mAdapter = new EditProfileAdapter(this, mArrayList);
        allergenListView = findViewById(R.id.listViewAllergens);
        allergenListView.setAdapter(mAdapter);


        spinnerVeg = findViewById(R.id.spinnerVeg);
        adapterVeg = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, veg);
        adapterVeg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVeg.setAdapter(adapterVeg);
        spinnerVeg.setOnItemSelectedListener(this);


        allergens = new ArrayList<>();
        autoAllergen = findViewById(R.id.autoAllergens);
        adapterAllergens = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, allergens);
        autoAllergen.setAdapter(adapterAllergens);
        autoAllergen.setThreshold(1);
        autoAllergen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mArrayList.contains(adapterView.getItemAtPosition(i).toString())) {
                    autoAllergen.setText("");
                    Toast.makeText(EditProfile.this, "Item already present in the list", Toast.LENGTH_SHORT).show();

                }
                else {
                    autoAllergen.setText("");
                    mArrayList.add(adapterView.getItemAtPosition(i).toString());
                    mAdapter.notifyDataSetChanged();

                }
            }
        });

        DocumentReference allergenDocumentRef = db.collection("predictableItems").document("#Allergens");
        allergenDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot mDocSnap = task.getResult();
                    if (mDocSnap.exists()) {
                        ArrayList<String> arrayList = (ArrayList<String>) mDocSnap.get("items");
                        allergens.clear();
                        allergens.addAll(arrayList);

                        adapterAllergens.notifyDataSetChanged();

                    }
                }
            }
        });


        // FETCH USER DATA IF EXISTS
        assert mUser != null;

        TextView mTextView = findViewById(R.id.textViewUserId);
        mTextView.setText(mUser.getEmail());

        final DocumentReference mDocumentReference = db.collection("Users").document(mUser.getUid());
        mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot mDocSnap = task.getResult();
                    if (mDocSnap.exists()) {
                        if (mDocSnap.get("spice") != null && mDocSnap.get("veg") != null && mDocSnap.get("allergens") != null) {
                            spice = Integer.parseInt(mDocSnap.get("spice").toString());
                            s.setProgress(spice);

                            mArrayList.addAll((ArrayList<String>) mDocSnap.get("allergens"));
                            mAdapter.notifyDataSetChanged();

                            String vegg=veg.get(0);
                            veg.set(0,mDocSnap.get("veg").toString());
                            for(int i=1;i<veg.size();i++){
                                if(veg.get(i).equals(mDocSnap.get("veg").toString())){
                                    veg.set(i,vegg);
                                }
                            }
                            adapterVeg.notifyDataSetChanged();

                            preference = Integer.parseInt(mDocSnap.get("preference").toString());
                        }
                    }
                }
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        vegPreference=parent.getItemAtPosition(pos).toString();
        if(vegPreference.equals("Jain")){
            preference=0;
        }
        else if(vegPreference.equals("Vegetarian")){
            preference=1;
        }
        else {
            preference=2;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void saveChanges(View view) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> mMap = new HashMap<>();
        mMap.put("preference", preference);
        mMap.put("spice", spice);
        mMap.put("veg", vegPreference);
        mMap.put("allergens", mArrayList);

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
    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(EditProfile.this, MainActivity.class);
        startActivity(intent);

        assert EditProfile.this != null;
        finish();
    }
}
