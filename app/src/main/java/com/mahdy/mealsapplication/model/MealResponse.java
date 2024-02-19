package com.mahdy.mealsapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealResponse {
    @SerializedName("meals")
    private List<MealInfo> meals;

    public List<MealInfo> getMeals() {
        return meals;
    }


}
