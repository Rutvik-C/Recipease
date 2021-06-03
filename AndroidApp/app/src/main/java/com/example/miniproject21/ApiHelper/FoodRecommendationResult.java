package com.example.miniproject21.ApiHelper;

import java.util.ArrayList;

public class FoodRecommendationResult {
    final ArrayList<String> recommendation;

    public FoodRecommendationResult(ArrayList<String> recommendation) {
        this.recommendation = recommendation;
    }

    public ArrayList<String> getRecommendation() {
        return recommendation;
    }
}
