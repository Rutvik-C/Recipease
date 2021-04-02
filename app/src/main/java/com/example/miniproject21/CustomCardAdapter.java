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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.miniproject21.TopTenCard.TopTenModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomCardAdapter extends ArrayAdapter<String> {

    Context mContext;
    ArrayList<TopTenModel> mArrayList;
    int mCategory;

    public CustomCardAdapter(@NonNull Context context, ArrayList<String> stringArrayList, ArrayList<TopTenModel> arrayList, int category) {
        super(context, R.layout.custom_card, stringArrayList);

        this.mContext = context;
        this.mArrayList = arrayList;
        this.mCategory = category;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        @SuppressLint("ViewHolder") View view = mLayoutInflater.inflate(R.layout.custom_card, null, true);

        TextView mTextView1 = view.findViewById(R.id.dishNameTextView);
        TextView mTextView2 = view.findViewById(R.id.dishCountTextView);
        final ImageView mImageView = view.findViewById(R.id.dishImageView);

        mTextView1.setText(mArrayList.get(position).getDishname());

        if (mCategory == 1) {
            mTextView2.setText(mArrayList.get(position).getDishcount());
        }

        StorageReference mRef = FirebaseStorage.getInstance().getReference("icons").child(mArrayList.get(position).getDishname() + ".png");
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
