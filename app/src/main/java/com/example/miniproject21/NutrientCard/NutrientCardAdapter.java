package com.example.miniproject21.NutrientCard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject21.R;
import com.example.miniproject21.RecipeCard.RecipeCardAdapter;
import com.example.miniproject21.RecipeCard.RecipeCardModel;

import java.util.ArrayList;

public class NutrientCardAdapter extends RecyclerView.Adapter<NutrientCardAdapter.Viewholder> {

    private Context context;
    private ArrayList<NutrientCardModel> nutrientArrayList;

    // Constructor
    public NutrientCardAdapter(Context context, ArrayList<NutrientCardAdapter> nutrientArrayListArrayList) {
        this.context = context;
        this.nutrientArrayList = nutrientArrayList;
    }

    @NonNull
    @Override
    public NutrientCardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrient_card, parent, false);
        return new NutrientCardAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NutrientCardAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        NutrientCardModel model = nutrientArrayList.get(position);
        holder.nutrientName.setText(model.getNutrientName());
        holder.nutrientValue.setText("" + model.getNutrientValue());
        //holder.courseIV.setImageResource(model.getCourse_image());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return nutrientArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView nutrientName, nutrientValue;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nutrientName = itemView.findViewById(R.id.nutrientName);
            nutrientValue= itemView.findViewById(R.id.nutrientValue);
        }
    }
}
