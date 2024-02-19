package com.mahdy.mealsapplication.model;

import com.mahdy.mealsapplication.network.MealApiService;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRepository {
    public static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    public MealApiService mealApiService;

    public MealRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        mealApiService = retrofit.create(MealApiService.class);
    }

    public Single<MealResponse> getRandomMeal() {
        return mealApiService.getRandomMeal();
    }
    public Single<MealResponse> getMealById(Integer id) {
        return mealApiService.getMealById(id);
    }
}
