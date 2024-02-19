package com.mahdy.mealsapplication.model;

import com.google.gson.annotations.SerializedName;

public class MealInfo {
    @SerializedName("idMeal")
    Integer mealId;

    @SerializedName("strMeal")
    String title;
    @SerializedName("strArea")
    String country;
    @SerializedName("strMealThumb")
    String imageUrl;
    @SerializedName("strCategory")
    String category;
    @SerializedName("strInstructions")
    String instructions;

    public MealInfo(Integer mealId, String title, String country, String imageUrl, String category, String instructions) {
        this.mealId = mealId;
        this.title = title;
        this.country = country;
        this.imageUrl = imageUrl;
        this.category = category;
        this.instructions = instructions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String description) {
        country = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getMealId() {
        return mealId;
    }

    public void setMealId(Integer mealId) {
        this.mealId = mealId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
