package com.example.miniproject21;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}