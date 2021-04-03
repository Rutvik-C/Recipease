package com.example.miniproject21.ApiHelper;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    String BASE_URL_PREDICTOR = "http://52.186.150.233:5000/";
    String BASE_URL_RECOMMENDATION = "https://rutvik-food-recommendation.herokuapp.com/";

    @Multipart
    @POST("food-predictor")
    Call<FoodPredictorResult> sendImage(@Part MultipartBody.Part image);
}
