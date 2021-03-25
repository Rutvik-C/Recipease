package com.example.miniproject21;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EditProfileAdapter extends ArrayAdapter<String> {

    Context mContext;
    ArrayList<String> mArrayList;

    public EditProfileAdapter(@NonNull Context context, ArrayList<String> arrayList) {
        super(context, R.layout.myprofile_card, arrayList);

        this.mContext = context;
        this.mArrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        @SuppressLint("ViewHolder") View view = mLayoutInflater.inflate(R.layout.myprofile_card, null, true);

        TextView mTextView = view.findViewById(R.id.allegen);
        ImageView mImageView = view.findViewById(R.id.imageViewDismiss);

        mImageView.setTag(position);
        mTextView.setText(mArrayList.get(position));

        return view;
    }
}
