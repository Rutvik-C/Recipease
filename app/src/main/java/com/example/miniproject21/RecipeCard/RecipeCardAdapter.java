package com.example.miniproject21.RecipeCard;

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
public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.Viewholder> {

    private Context context;
    private ArrayList<RecipeCardModel> recipeArrayList;

    // Constructor
    public RecipeCardAdapter(Context context, ArrayList<RecipeCardModel> recipeArrayList) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
    }

    @NonNull
    @Override
    public RecipeCardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCardAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        RecipeCardModel model = recipeArrayList.get(position);
        holder.stepName.setText(model.getStepno());
        holder.stepDetail.setText("" + model.getStepDetail());
        //holder.courseIV.setImageResource(model.getCourse_image());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return recipeArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView courseIV;
        private TextView stepName, stepDetail;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            stepName = itemView.findViewById(R.id.idStepName);
            stepDetail = itemView.findViewById(R.id.idStepDetail);
        }
    }
}