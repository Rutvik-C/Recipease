package com.example.miniproject21.FragmentSet1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproject21.ApiHelper.ApiInterface;
import com.example.miniproject21.ApiHelper.FoodPredictorResult;
import com.example.miniproject21.ButtonListAdapter;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadPageFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private static final int STORAGE_REQUEST = 7;
    private static final int SELECT_FILE = 8;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    static Interpreter interpreter;
    Bitmap bitmap;
    ImageView imageView;
    TextView messageTextView;
    Button retryButton, gotoResultButton;
    Uri currentImageUri;

    static int LEN_CLASSES;
    ArrayList<String> CLASSES;
    ArrayList<String> predictedSubTypes;
    boolean subTypesReady = false;

    boolean predicted = false;
    @SuppressLint("StaticFieldLeak")
    static ProgressBar progressBar;


    public void addToHistoryAndProceed(String itemName) {
        Intent intent = new Intent(getContext(), ResultsActivity.class);
        intent.putExtra("item", itemName);
        startActivity(intent);

    }

    public void getSubTypes(String itemName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("predictableItems").document(itemName);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    predictedSubTypes.clear();
                    predictedSubTypes.addAll((ArrayList<String>) document.get("items"));
                    subTypesReady = true;

                    Log.i("HERE", "Fetched!");

                } else {
                    Toast.makeText(getContext(), "Unable to find this item", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void makePredictions() {

        if (bitmap != null) {
            subTypesReady = false;

            progressBar.setVisibility(View.VISIBLE);
            try {
                Predictor predictor = new Predictor();

                int index = predictor.execute(bitmap).get();

                predicted = true;

                getSubTypes(CLASSES.get(index));
                gotoResultButton.setText(CLASSES.get(index));
                gotoResultButton.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.INVISIBLE);

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

    public Uri saveBitmapImage(Context inContext, Bitmap inImage) {
        Log.i("SAVE", "saving image...");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "recipease", null);
        return Uri.parse(path);
    }

    public String getFilePathFromUri(Uri uri) {
        String path = "";
        if (HomePage.contextOfApplication.getContentResolver() != null) {
            Cursor cursor = HomePage.contextOfApplication.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }

        return path;
    }

    public void getPredictionsFromServer() {
        progressBar.setVisibility(View.VISIBLE);
        messageTextView.setVisibility(View.INVISIBLE);

        try {
            Bitmap photo = MediaStore.Images.Media.getBitmap(HomePage.contextOfApplication.getContentResolver(), currentImageUri);
            imageView.setImageBitmap(photo);

            Uri tempUri = saveBitmapImage(getContext(), photo);
            String filePath = getFilePathFromUri(tempUri);

            final File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL_PREDICTOR)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<FoodPredictorResult> mCall = apiInterface.sendImage(body);

            mCall.enqueue(new Callback<FoodPredictorResult>() {
                @Override
                public void onResponse(Call<FoodPredictorResult> call, Response<FoodPredictorResult> response) {
                    FoodPredictorResult mResult = response.body();

                    if (mResult.isFood()) {
                        Log.i("CATEGORY", mResult.getCategory() + " " + mResult.getConfidence());

                        String text = "Results:-";
                        messageTextView.setText(text);

                        getSubTypes(mResult.getCategory());
                        gotoResultButton.setText(mResult.getCategory());
                        gotoResultButton.setVisibility(View.VISIBLE);
                        retryButton.setVisibility(View.INVISIBLE);

                    } else {
                        String text = "The image does not contain any food item";
                        messageTextView.setText(text);

                        Log.i("NOT", "FOOD");

                    }

                    messageTextView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);



                    if (file.exists()) {
                        file.delete();
                    }
                }

                @Override
                public void onFailure(Call<FoodPredictorResult> call, Throwable t) {
                    Log.i("ERROR", "There was an error " + t.getMessage());

                    String text = "There was some error";
                    messageTextView.setText(text);
                    messageTextView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                    retryButton.setVisibility(View.VISIBLE);

                    if (file.exists()) {
                        file.delete();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();

            String text = "There was some error";
            messageTextView.setText(text);
            messageTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            retryButton.setVisibility(View.VISIBLE);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Image using camera
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            Uri tempUri = saveBitmapImage(getContext(), photo);

            CropImage.activity(tempUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);

//            if (photo.getHeight() >= photo.getWidth()) {
//                bitmap = Bitmap.createBitmap(photo, 0, photo.getHeight() / 2 - photo.getWidth() / 2, photo.getWidth(), photo.getWidth());
//            } else {
//                bitmap = Bitmap.createBitmap(photo, photo.getWidth() / 2 - photo.getHeight() / 2, 0, photo.getHeight(), photo.getHeight());
//            }

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
            currentImageUri = result.getUri();
            Log.i("IMG CROPPER", "In cropper");

            getPredictionsFromServer();

        }
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_page, container, false);

        predictedSubTypes = new ArrayList<>();
        messageTextView = view.findViewById(R.id.messageTextView);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference mDocumentReference = db.collection("predictableItems").document("#PredItem");
        mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot mDocumentSnapshot = task.getResult();
                    if (mDocumentSnapshot != null && mDocumentSnapshot.exists()) {
                        CLASSES = (ArrayList<String>) mDocumentSnapshot.get("items");

                        assert CLASSES != null;
                        LEN_CLASSES = CLASSES.size();

                        Log.i("CLASSES", LEN_CLASSES + " --> " + CLASSES);
                    }

                } else {
                    Log.i("ERR", "" + task.getException());
                }
            }
        });

        retryButton = view.findViewById(R.id.buttonDetect);
        gotoResultButton = view.findViewById(R.id.gotoResultButton);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

//        try {
//            interpreter = new Interpreter(loadModel());
//            Log.i("Done", "Model loaded");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        imageView = view.findViewById(R.id.imageViewSelectImage);

        ImageView camera = view.findViewById(R.id.imageViewCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryButton.setVisibility(View.INVISIBLE);

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
                retryButton.setVisibility(View.INVISIBLE);

                gotoResultButton.setVisibility(View.INVISIBLE);

                if (HomePage.contextOfApplication.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST);

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_FILE);
                }

            }
        });

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPredictionsFromServer();

                retryButton.setVisibility(View.INVISIBLE);

            }
        });

        gotoResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subTypesReady) {
                    if (predictedSubTypes.size() == 1) {
                        Intent intent = new Intent(getContext(), ResultsActivity.class);
                        intent.putExtra("item", predictedSubTypes.get(0));
                        startActivity(intent);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

                        // Increasing search count
                        db.collection("foodItems").document(predictedSubTypes.get(0)).update("search_count", FieldValue.increment(1));

                        // Adding to user history
                        assert mUser != null;
                        db.collection("Users").document(mUser.getUid()).update("history", FieldValue.arrayUnion(predictedSubTypes.get(0)));

                    } else {
                        Log.i("INFO", "MULTIPLE");

                        View mView = LayoutInflater.from(getContext()).inflate(R.layout.choice_alert_box, null);
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

                        mBuilder.setView(mView);

                        final AlertDialog mAlertDialog = mBuilder.create();
                        mAlertDialog.show();

                        ListView listView = mView.findViewById(R.id.buttonsListView);
                        assert getContext() != null;
                        ButtonListAdapter buttonListAdapter = new ButtonListAdapter(getContext(), predictedSubTypes);
                        listView.setAdapter(buttonListAdapter);

                    }


                } else {
                    Toast.makeText(getContext(), "Fetching information...\nRetry uploading image...", Toast.LENGTH_SHORT).show();

                }

            }
        });

        return view;
    }

//    private MappedByteBuffer loadModel() throws IOException {
//        assert getContext() != null;
//        AssetFileDescriptor assetFileDescriptor = getContext().getAssets().openFd("FoodClassifier40_81.tflite");
//        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
//        FileChannel fileChannel = fileInputStream.getChannel();
//        long startOffset = assetFileDescriptor.getStartOffset();
//        long declaredLength = assetFileDescriptor.getDeclaredLength();
//
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
//    }

}