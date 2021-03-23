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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ResultsActivity extends AppCompatActivity {

    static ViewPager viewPager;
    static BottomNavigationView bottomNavigationView;
    static Menu menu;
    public static String item;

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

    }
}
