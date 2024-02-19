package com.mahdy.mealsapplication.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mahdy.mealsapplication.db.FavoriteMealDao;
import com.mahdy.mealsapplication.db.MealsDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class FavoriteMealRepository {
    private FavoriteMealDao favoriteMealDao;

    public FavoriteMealRepository(Application application) {
        MealsDatabase database = MealsDatabase.getInstance(application);
        favoriteMealDao = database.favoriteMealDao();
    }

    public Completable insertFavoriteMeal(FavoriteMeal favoriteMeal) {
        return favoriteMealDao.insertFavMeal(favoriteMeal);
    }

    public LiveData<List<FavoriteMeal>> getUserFavoriteMeals(Integer userId) {
        return favoriteMealDao.getUserFavoriteMeals(userId);
    }

    public Completable deleteFavoriteMeal(int id) {
        return favoriteMealDao.deleteFavoriteMeal(id);
    }

    public Single<Boolean> isMealFavorited(Integer userId, Integer mealId) {
        return favoriteMealDao.isMealFavorited(userId, mealId);
    }

    public Single<FavoriteMeal> getUserFavoriteMeal(Integer userId, Integer mealId) {
        return favoriteMealDao.getUserFavoriteMeal(userId, mealId);
    }
}
