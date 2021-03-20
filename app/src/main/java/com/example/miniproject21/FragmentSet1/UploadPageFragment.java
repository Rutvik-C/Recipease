package com.example.miniproject21.FragmentSet1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproject21.HomePage;
import com.example.miniproject21.R;
import com.example.miniproject21.ResultsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UploadPageFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private static final int STORAGE_REQUEST = 7;
    private static final int SELECT_FILE = 8;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    static Interpreter interpreter;
    Bitmap bitmap;
    ImageView imageView;
    Button getInformationButton, gotoResultButton;

    static int LEN_CLASSES;
    ArrayList<String> CLASSES;

    boolean predicted = false;
    @SuppressLint("StaticFieldLeak")
    static ProgressBar progressBar;


    public void makePredictions() {
        if (bitmap != null) {
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            try {
                Predictor predictor = new Predictor();

                int index = predictor.execute(bitmap).get();

                predicted = true;

                gotoResultButton.setText(CLASSES.get(index));
                gotoResultButton.setVisibility(View.VISIBLE);
                getInformationButton.setVisibility(View.INVISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity(), "Please select an Image", Toast.LENGTH_SHORT).show();

        }
    }

    public static class Predictor extends AsyncTask<Bitmap, Void, Integer> {

        @Override
        protected Integer doInBackground(Bitmap... bitmaps) {
            Bitmap bitmap = bitmaps[0];

            Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder());
            for (int y = 0; y < 224; y++) {
                for (int x = 0; x < 224; x++) {
                    int px = finalBitmap.getPixel(x, y);

                    float r = Color.red(px) / 255.0f;
                    float g = Color.green(px) / 255.0f;
                    float b = Color.blue(px) / 255.0f;

                    byteBuffer.putFloat(r);
                    byteBuffer.putFloat(g);
                    byteBuffer.putFloat(b);
                }
            }

            Log.i("Reached", "pixels adjusted!");

            int bufferSize = LEN_CLASSES * Float.SIZE / Byte.SIZE;
            ByteBuffer modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
            interpreter.run(byteBuffer, modelOutput);

            modelOutput.rewind();
            FloatBuffer probabilities = modelOutput.asFloatBuffer();

            Log.i("HERE", "Reached before try " + probabilities.capacity());

            int maxIndex = 0;
            float max = -1;

            for (int i = 0; i < probabilities.capacity(); i++) {
                if (probabilities.get(i) > max) {
                    max = probabilities.get(i);
                    maxIndex = i;
                }
            }

            return maxIndex;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            } else {
                Toast.makeText(getActivity(), "Provide permission to access camera!", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == STORAGE_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_FILE);

            } else {
                Toast.makeText(getActivity(), "Provide permission to access your images!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Image using camera
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            if (photo.getHeight() >= photo.getWidth()) {
                bitmap = Bitmap.createBitmap(photo, 0, photo.getHeight() / 2 - photo.getWidth() / 2, photo.getWidth(), photo.getWidth());
            } else {
                bitmap = Bitmap.createBitmap(photo, photo.getWidth() / 2 - photo.getHeight() / 2, 0, photo.getHeight(), photo.getHeight());
            }

            imageView.setImageBitmap(bitmap);
            // BITMAP READY

        }

        // Image from gallery
        if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            Uri imageLocation = data.getData();

            CropImage.activity(imageLocation)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);

            Log.i("WILL", "will call cropper");
        }

        // Image cropper
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri uri = result.getUri();
            Log.i("IMG CROPPER", "In cropper");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(HomePage.contextOfApplication.getContentResolver(), uri);

                imageView.setImageBitmap(bitmap);
                Log.i("IMG CROPPER TRY", "Image set");
                // BITMAP READY

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_page, container, false);

        // CLASSES = new String[] { "Aloo Paratha", "Bhel", "Biryani", "Burger", "Butter Naan", "Tea", "Chapati", "Chicken Wings", "Chole Bhature", "Club Sandwich", "Cup Cakes", "Dal Makhani", "Dhokla", "French Fries", "Fried Rice", "Gajar Halwa", "Garlic Bread", "Grilled Sandwich", "Gulab Jamun", "Hot Dog", "Idli", "Jalebi", "Kaathi Rolls", "Kadai Paneer", "Kulfi", "Masala Dosa", "Momos", "Noodles", "Omelette", "Paani Puri", "Pakode", "Pav Bhaji", "Pizza", "Poha", "Samosa", "Soup", "Spring Roll", "Strawberry Cake", "Vada Pav", "Waffles" };

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference mDocumentReference = db.collection("foodItems").document("#PredictableItems");
        mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot mDocumentSnapshot = task.getResult();
                    if (mDocumentSnapshot != null && mDocumentSnapshot.exists()) {
                        CLASSES = (ArrayList<String>) mDocumentSnapshot.get("itemList");

                        assert CLASSES != null;
                        LEN_CLASSES = CLASSES.size();

                        Log.i("CLASSES", LEN_CLASSES + " --> " + CLASSES);
                    }

                } else {
                    Log.i("ERR", "" + task.getException());
                }
            }
        });

        getInformationButton = view.findViewById(R.id.buttonDetect);
        gotoResultButton = view.findViewById(R.id.gotoResultButton);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        try {
            interpreter = new Interpreter(loadModel());
            Log.i("Done", "Model loaded");

        } catch (Exception e) {
            e.printStackTrace();
        }

        imageView = view.findViewById(R.id.imageViewSelectImage);

        ImageView camera = view.findViewById(R.id.imageViewCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationButton.setVisibility(View.VISIBLE);
                gotoResultButton.setVisibility(View.INVISIBLE);

                if (HomePage.contextOfApplication.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);

                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        ImageView gallery = view.findViewById(R.id.imageViewGallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationButton.setVisibility(View.VISIBLE);
                gotoResultButton.setVisibility(View.INVISIBLE);

                if (HomePage.contextOfApplication.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST);

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_FILE);
                }

            }
        });

        getInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePredictions();

            }
        });

        gotoResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ResultsActivity.class);
                intent.putExtra("item", gotoResultButton.getText());

                startActivity(intent);

//                if (predicted) {
//                    Intent intent = new Intent(getContext(), ResultsActivity.class);
//                    intent.putExtra("item", textViewResult.getText());
//
//                    startActivity(intent);
//
//                    // Adding to user history
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
//
//                    assert mUser != null;
//                    db.collection("Users").document(mUser.getUid()).update("history", FieldValue.arrayUnion(textViewResult.getText()));
//
//                }
            }
        });

        return view;
    }

    private MappedByteBuffer loadModel() throws IOException {
        assert getContext() != null;
        AssetFileDescriptor assetFileDescriptor = getContext().getAssets().openFd("FoodClassifier40_81.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long declaredLength = assetFileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}