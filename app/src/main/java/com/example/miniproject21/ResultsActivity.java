package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.miniproject21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultsActivity extends AppCompatActivity {

    static ViewPager viewPager;
    static BottomNavigationView bottomNavigationView;
    static Menu menu;
    public static String item;
    public static DocumentSnapshot userDoc;

    public static Menu getMenu() {
        return menu;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        item = intent.getStringExtra("item");

        Log.i("INTENT", item);

        viewPager = findViewById(R.id.resultsViewPager);
        ResultFragmentAdapter resultFragmentAdapter = new ResultFragmentAdapter(getSupportFragmentManager());

        viewPager.setAdapter(resultFragmentAdapter);
        viewPager.setCurrentItem(0);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.generalOption:
                        viewPager.setCurrentItem(0);
                        menu.getItem(0).setChecked(true);
                        break;

                    case R.id.recipeOption:
                        viewPager.setCurrentItem(1);
                        menu.getItem(1).setChecked(true);
                        break;

                    case R.id.nutritionOption:
                        viewPager.setCurrentItem(2);
                        menu.getItem(2).setChecked(true);
                        break;

                    case R.id.videoOption:
                        viewPager.setCurrentItem(3);
                        menu.getItem(3).setChecked(true);
                        break;
                }

                return false;
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = firebaseAuth.getCurrentUser();

        assert mUser != null;
        DocumentReference docRef = db.collection("Users").document(mUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        userDoc = document;
                    }
                }
            }
        });

    }
}
