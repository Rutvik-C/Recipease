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

        // code here
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        final ImageView mImageView = view.findViewById(R.id.dishImage);

        StorageReference mRef =FirebaseStorage.getInstance().getReference("icons").child(ResultsActivity.item + ".png");
        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
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
        if (ResultsActivity.cloudLiked) {
            imageView.setImageResource(R.drawable.ic_liked);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ResultsActivity.cloudLiked){
                    ResultsActivity.cloudLiked=false;
                    Toast.makeText(getContext(), "Removed from liked items", Toast.LENGTH_SHORT).show();

                    assert mUser != null;
                    db.collection("Users").document(mUser.getUid()).update("liked", FieldValue.arrayRemove(ResultsActivity.item));

                    imageView.setImageResource(R.drawable.ic_like);
                }else{
                    ResultsActivity.cloudLiked=true;
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
