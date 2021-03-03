package com.example.miniproject21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class HomePage extends AppCompatActivity {

    public static Context contextOfApplication;
    //final int LEN_CLASSES = 20;
    //String[] CLASSES;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ViewPager viewPager = (ViewPager)findViewById(R.id.pager);

        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

        //CLASSES = new String[] { "burger", "butter_naan", "chai", "chapati", "chole_bhature", "dal_makhani", "dhokla", "fried_rice", "idli", "jalebi", "kaathi_rolls", "kadai_paneer", "kulfi", "masala_dosa", "momos", "paani_puri", "pakode", "pav_bhaji", "pizza", "samosa" };

//        try {
//            interpreter = new Interpreter(loadModel());
//            Log.i("Done", "Model loaded");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        contextOfApplication = getApplicationContext();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    private MappedByteBuffer loadModel() throws IOException {
//        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("FoodClassifierIndian20.tflite");
//        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
//        FileChannel fileChannel = fileInputStream.getChannel();
//        long startOffset = assetFileDescriptor.getStartOffset();
//        long declaredLength = assetFileDescriptor.getDeclaredLength();
//
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
//
//    }
}
