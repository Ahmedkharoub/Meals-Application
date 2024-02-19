package com.mahdy.mealsapplication.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mahdy.mealsapplication.model.FavoriteMeal;


import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavoriteMealDao {
    @Insert
    Completable insertFavMeal(FavoriteMeal favoriteMeal);

    @Query("SELECT * FROM favorite_meal WHERE userId = :id")
    LiveData<List<FavoriteMeal>> getUserFavoriteMeals(Integer id);

    @Query("DELETE FROM favorite_meal WHERE id = :id")
    Completable deleteFavoriteMeal(Integer id);

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_meal WHERE userId = :userId AND mealId = :mealId);")
    Single<Boolean> isMealFavorited(Integer userId, Integer mealId);

    @Query("SELECT * FROM favorite_meal WHERE userId = :userId AND mealId = :mealId;")
    Single<FavoriteMeal> getUserFavoriteMeal(Integer userId, Integer mealId);
}
