package com.example.miniproject21.HistoryCard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject21.R;
import com.example.miniproject21.TopTenCard.TopTenCardAdapter;
import com.example.miniproject21.TopTenCard.TopTenModel;

import java.util.ArrayList;

public class HistoryCardAdapter extends RecyclerView.Adapter<HistoryCardAdapter.Viewholder> {

    private Context context;
    private ArrayList<HistoryModel> historyArrayList;

    // Constructor
    public HistoryCardAdapter(Context context, ArrayList<HistoryModel> historyArrayList) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    @NonNull
    @Override
    public HistoryCardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_card, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryCardAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        HistoryModel model = historyArrayList.get(position);
        holder.dishName.setText(model.getDishname());
        holder.dishCount.setText("" + model.getDishcount());
        holder.dishImage.setImageResource(model.getDishimage());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return historyArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public static class Viewholder extends RecyclerView.ViewHolder {
        private ImageView dishImage;
        private TextView dishName,dishCount;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            dishImage= itemView.findViewById(R.id.dishImageHistory);
            dishName = itemView.findViewById(R.id.dishNameHistory);
            dishCount = itemView.findViewById(R.id.dishCountHistory);
        }
    }
}