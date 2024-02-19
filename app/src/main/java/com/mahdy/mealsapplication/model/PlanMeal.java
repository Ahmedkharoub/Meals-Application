package com.mahdy.mealsapplication.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "plan_meal")
public class PlanMeal {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    private Integer mealId;

    private Integer weekDay;

    private Integer daySlot;

    @SerializedName("strMeal")
    private String title;

    @SerializedName("strMealThumb")
    private String imageUrl;

    public PlanMeal() {
    }

    public PlanMeal(Integer id, Integer mealId, Integer weekDay, Integer daySlot, String title, String imageUrl) {
        this.id = id;
        this.mealId = mealId;
        this.weekDay = weekDay;
        this.daySlot = daySlot;
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

    public Integer getMealId() {
        return mealId;
    }

    public void setMealId(Integer mealId) {
        this.mealId = mealId;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getDaySlot() {
        return daySlot;
    }

    public void setDaySlot(Integer daySlot) {
        this.daySlot = daySlot;
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
}
