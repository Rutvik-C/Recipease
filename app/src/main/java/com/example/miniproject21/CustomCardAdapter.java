package com.example.miniproject21;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomCardAdapter extends ArrayAdapter<String> {

    Context mContext;
    ArrayList<String> mArrayList;
    ArrayList<Boolean> likedArrayList;
    boolean showLikes;

    public CustomCardAdapter(@NonNull Context context, ArrayList<String> stringArrayList, ArrayList<Boolean> booleanArrayList, boolean isHistory) {
        super(context, R.layout.custom_card, stringArrayList);

        this.mContext = context;
        this.mArrayList = stringArrayList;
        this.likedArrayList = booleanArrayList;
        this.showLikes = isHistory;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        @SuppressLint("ViewHolder") View view = mLayoutInflater.inflate(R.layout.custom_card, null, true);

        TextView mTextView1 = view.findViewById(R.id.dishNameTextView);
        final ImageView mImageView = view.findViewById(R.id.dishImageView);

        mTextView1.setText(mArrayList.get(position));
        if (showLikes && likedArrayList.get(position)) {
            ImageView isLikedImageView = view.findViewById(R.id.likeHolderImageView);

            isLikedImageView.setImageResource(R.drawable.ic_liked);
        }

        StorageReference mRef = FirebaseStorage.getInstance().getReference("icons").child(mArrayList.get(position) + ".png");
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

        return view;
    }
}
