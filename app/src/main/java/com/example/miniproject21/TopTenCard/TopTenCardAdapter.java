package com.example.miniproject21.TopTenCard;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject21.R;

import java.util.ArrayList;
public class TopTenCardAdapter extends RecyclerView.Adapter<TopTenCardAdapter.Viewholder> {

    private Context context;
    private ArrayList<TopTenModel> toptenArrayList;

    // Constructor
    public TopTenCardAdapter(Context context, ArrayList<TopTenModel> toptenArrayList) {
        this.context = context;
        this.toptenArrayList = toptenArrayList;
    }

    @NonNull
    @Override
    public TopTenCardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopTenCardAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        TopTenModel model = toptenArrayList.get(position);
        holder.dishName.setText(model.getDishname());
        holder.dishCount.setText("" + model.getDishcount());
        holder.dishImage.setImageResource(model.getDishimage());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return toptenArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView dishImage;
        private TextView dishName,dishCount;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            dishImage= itemView.findViewById(R.id.dishImagetopten);
            dishName = itemView.findViewById(R.id.nutrientName);
            dishCount = itemView.findViewById(R.id.nutrientValue);
        }
    }
}