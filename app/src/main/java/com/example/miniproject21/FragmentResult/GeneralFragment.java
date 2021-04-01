package com.example.miniproject21.FragmentResult;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.miniproject21.HomePage;
import com.example.miniproject21.R;
import com.example.miniproject21.ResultsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneralFragment extends Fragment {

    ImageView imageView;

    Boolean isLike=false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            ResultsActivity.getMenu().getItem(0).setChecked(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_result, container, false);

        final HashMap<Integer, String> mMap = new HashMap<>();
        mMap.put(0, "Jain");
        mMap.put(1, "Vegetarian");
        mMap.put(2, "Non Vegetarian");

        // code here
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        final DocumentReference docRef = db.collection("foodItems").document(ResultsActivity.item);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String result = "";

                        long foodCat = (long) document.get("category");
                        long foodSpice = (long) document.get("spice_level");
                        ArrayList<String> foodContents = (ArrayList<String>) document.get("nv_ingredients");
                        Log.i("FOOD INFO", "" + foodCat + " " + foodSpice + " " + foodContents);

                        long userCat = (long) ResultsActivity.userDoc.get("preference");
                        long userSpice = (long) ResultsActivity.userDoc.get("spice");
                        ArrayList<String> userAllergen = (ArrayList<String>) ResultsActivity.userDoc.get("allergens");
                        Log.i("USER INFO", "" + userCat + " " + userSpice + " " + userAllergen);

                        if (userCat < foodCat) {
                            result += "The food is marked " + mMap.get((int) foodCat) + "!\n";
                        }
                        if (userSpice + 2 < foodSpice) {
                            result += "The food might be spicy for you!\n";
                        }
                        for (String s1 : foodContents) {
                            for (String s2 : userAllergen) {
                                if (s1.equals(s2)) {
                                    result += s1 + ", ";
                                }
                            }
                        }

                        if (!result.equals("")) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Alert!")
                                    .setMessage(result)
                                    .setCancelable(false)
                                    .setPositiveButton("I Understand, Proceed", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();

                                        }
                                    })
                                    .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            getActivity().onBackPressed();

                                        }
                                    })
                                    .create()
                                    .show();

                        }

                    }
                }
            }
        });

        final ImageView mImageView = view.findViewById(R.id.dishImage);

        StorageReference mRef =FirebaseStorage.getInstance().getReference("icons").child(ResultsActivity.item + ".png");
        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("URI", uri.toString());
                Glide.with(getContext())
                        .load(uri)
                        .placeholder(R.drawable.image_progress)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(mImageView);


            }
        });

        TextView mTextView = view.findViewById(R.id.dishName);
        mTextView.setText(ResultsActivity.item);

        imageView=view.findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLike){
                    isLike=false;
                    Toast.makeText(getContext(), "Removed from liked items", Toast.LENGTH_SHORT).show();

                    assert mUser != null;
                    db.collection("Users").document(mUser.getUid()).update("liked", FieldValue.arrayRemove(ResultsActivity.item));

                    imageView.setImageResource(R.drawable.ic_like);
                }else{
                    isLike=true;
                    Toast.makeText(getContext(), "Added to liked items", Toast.LENGTH_SHORT).show();

                    assert mUser != null;
                    db.collection("Users").document(mUser.getUid()).update("liked", FieldValue.arrayUnion(ResultsActivity.item));

                    imageView.setImageResource(R.drawable.ic_liked);
                }
            }
        });

        return view;
    }

}
