package com.example.miniproject21.ApiHelper;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    String BASE_URL_PREDICTOR = "http://52.186.83.93:5000/";
    String BASE_URL_RECOMMENDATION = "https://rutvik-food-recommendation.herokuapp.com/";

    @Multipart
    @POST("food-predictor")
    Call<FoodPredictorResult> sendImage(@Part MultipartBody.Part image);

    @GET("similar-recommendation")
    Call<FoodRecommendationResult> getSimilarFoodItems(@Query("item-name") String itemName);

    @GET("user-recommendation")
    Call<FoodRecommendationResult> getUserRecommendation(@Query("user-uid") String userUid);
}
