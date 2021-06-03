package com.example.miniproject21;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ButtonListAdapter extends ArrayAdapter<String> {

    Context mContext;
    ArrayList<String> mArrayList;

    public ButtonListAdapter(@NonNull Context context, ArrayList<String> arrayList) {
        super(context, R.layout.button_list, arrayList);

        this.mContext = context;
        this.mArrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        @SuppressLint("ViewHolder") View view = mLayoutInflater.inflate(R.layout.button_list, null, true);

        Button button = view.findViewById(R.id.listButton);

        button.setText(mArrayList.get(position));

        return view;
    }
}
