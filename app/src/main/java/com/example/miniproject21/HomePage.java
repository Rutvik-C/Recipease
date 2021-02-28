package com.example.miniproject21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

public class HomePage extends AppCompatActivity {

    public static Context contextOfApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ViewPager viewPager = (ViewPager)findViewById(R.id.pager);

        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

        contextOfApplication = getApplicationContext();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
