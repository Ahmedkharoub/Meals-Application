package com.mahdy.mealsapplication.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "favorite_meal")

public class FavoriteMeal {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    private Integer mealId;
    private Integer userId;
    @SerializedName("strMeal")
    private String title;

    @SerializedName("strMealThumb")
    private String imageUrl;

    public FavoriteMeal(@NonNull Integer id,Integer mealId,Integer userId, String title, String imageUrl) {
        this.id = id;
        this.mealId = mealId;
        this.userId = userId;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
