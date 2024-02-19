package com.mahdy.mealsapplication.listener;

import com.mahdy.mealsapplication.model.MealInfo;

public interface MealListener {
    void showRandomMeal(MealInfo meal);
    void showError(String message);
    void showMeal(MealInfo detailsMeal);
}
