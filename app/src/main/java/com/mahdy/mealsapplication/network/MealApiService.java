package com.mahdy.mealsapplication.network;

import com.mahdy.mealsapplication.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {
    @GET("random.php")
    Single<MealResponse> getRandomMeal();
    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") Integer id);
}
