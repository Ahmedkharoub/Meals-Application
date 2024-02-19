package com.mahdy.mealsapplication.listener;

import com.mahdy.mealsapplication.model.FavoriteMeal;

import java.util.List;

public interface FavoriteListener {
    void onReceiveFavoriteMeals(List<FavoriteMeal> planMeals);
    void onFailedFavoriteMeals(String message);
}
