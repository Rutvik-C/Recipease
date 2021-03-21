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
import androidx.viewpager.widget.ViewPager;

import com.example.miniproject21.TopTenCard.TopTenModel;

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
        LayoutInflater mLayoutInflater =LayoutInflater.from(mContext);
        @SuppressLint("ViewHolder") View view = mLayoutInflater.inflate(R.layout.custom_card, null, true);

        TextView mTextView1 = view.findViewById(R.id.dishNameTextView);
        TextView mTextView2 = view.findViewById(R.id.dishCountTextView);
        ImageView mImageView = view.findViewById(R.id.dishImageView);

        mTextView1.setText(mArrayList.get(position).getDishname());

        if (mCategory == 1) {
            mTextView2.setText(mArrayList.get(position).getDishcount());
        }

        mImageView.setImageResource(mArrayList.get(position).getDishimage());

        return view;
    }
}
