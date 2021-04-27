package com.example.miniproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

public class HomePage extends AppCompatActivity {

    public static Context contextOfApplication;
    public static ArrayList<String> allItems;
    public static ArrayList<String> userLikedItems;


    public void takeToResult(View view) {
        Button mButton = (Button) view;

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("item", mButton.getText().toString());
        startActivity(intent);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        // Increasing search count
        db.collection("foodItems").document(mButton.getText().toString()).update("search_count", FieldValue.increment(1));

        // Adding to user history
        assert mUser != null;
        db.collection("Users").document(mUser.getUid()).update("history", FieldValue.arrayUnion(mButton.getText().toString()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ViewPager viewPager = (ViewPager)findViewById(R.id.pager);

        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);

        contextOfApplication = getApplicationContext();

        allItems = new ArrayList<>();
        userLikedItems = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference allergenDocumentRef = db.collection("predictableItems").document("#Allergens");
        allergenDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot mDocSnap = task.getResult();
                    if (mDocSnap.exists()) {
                        ArrayList<String> arrayList = (ArrayList<String>) mDocSnap.get("items");

                        assert arrayList != null;
                        allItems.clear();
                        allItems.addAll(arrayList);

                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
