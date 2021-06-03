package com.example.miniproject21.ApiHelper;

public class FoodPredictorResult {
    final private boolean isFood;
    final private String category;
    final private String confidence;

    public FoodPredictorResult(boolean isFood, String category, String confidence) {
        this.isFood = isFood;
        this.category = category;
        this.confidence = confidence;
    }

    public boolean isFood() {
        return isFood;
    }

    public String getCategory() {
        return category;
    }

    public String getConfidence() {
        return confidence;
    }
}
