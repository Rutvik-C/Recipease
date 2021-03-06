package com.example.miniproject21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.miniproject21.R;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        String item = intent.getStringExtra("item");

        Log.i("INTENT", item);

        ViewPager viewPager = findViewById(R.id.resultsViewPager);
        ResultFragmentAdapter resultFragmentAdapter = new ResultFragmentAdapter(getSupportFragmentManager());

        viewPager.setAdapter(resultFragmentAdapter);
        viewPager.setCurrentItem(0);

    }
}
