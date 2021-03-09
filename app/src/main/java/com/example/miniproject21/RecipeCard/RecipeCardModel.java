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

public class RecipeCardModel {

        private String course_name;
        private int course_rating;
        private int course_image;

        // Constructor
        public RecipeCardModel(String course_name, int course_rating, int course_image) {
            this.course_name = course_name;
            this.course_rating = course_rating;
            this.course_image = course_image;
        }

        // Getter and Setter
        public String getCourse_name() {
            return course_name;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public int getCourse_rating() {
            return course_rating;
        }

        public void setCourse_rating(int course_rating) {
            this.course_rating = course_rating;
        }

        public int getCourse_image() {
            return course_image;
        }

        public void setCourse_image(int course_image) {
            this.course_image = course_image;
        }
    }