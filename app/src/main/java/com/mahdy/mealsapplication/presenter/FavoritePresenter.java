package com.mahdy.mealsapplication.presenter;

import android.util.Log;

import com.mahdy.mealsapplication.listener.FavoriteListener;
import com.mahdy.mealsapplication.model.FavoriteMeal;
import com.mahdy.mealsapplication.model.FavoriteMealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenter {
    private FavoriteListener listener;
    private final FavoriteMealRepository repository;
    private static final String TAG = FavoritePresenter.class.getSimpleName();

    public FavoritePresenter(FavoriteListener listener, FavoriteMealRepository repository) {
        this.listener = listener;
        this.repository = repository;
    }

    public void insertFavoriteMeal(FavoriteMeal favoriteMeal) {
        repository.insertFavoriteMeal(favoriteMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.i(TAG, "insertWeeklyPlanMean: inserted favorite meal");
                }, throwable -> {
                    Log.e(TAG, "insertWeeklyPlanMean: failed to insert favorite meal", throwable);
                });
    }

    public Disposable deleteFavoriteMeal(Integer id) {
        return repository.deleteFavoriteMeal(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.i(TAG, "insertWeeklyPlanMean: deleted favorite meal id " + id);
                }, throwable -> {
                    Log.e(TAG, "insertWeeklyPlanMean: failed to delete favorite meal id " + id, throwable);
                });
    }

    public void getUserFavoriteMeals(Integer userId) {
        repository.getUserFavoriteMeals(userId)
                .observeForever(favoriteMeals -> {
                    Log.i(TAG, "onChanged: Fetched: " + favoriteMeals.size() + " favorite meals for user " + userId);
                    listener.onReceiveFavoriteMeals(favoriteMeals);
                });
    }

    public Single<Boolean> isMealFavorited(Integer userId, Integer mealId) {
        return repository.isMealFavorited(userId, mealId);
    }

    public Single<FavoriteMeal> getUserFavoriteMeal(Integer userId, Integer mealId) {
        return repository.getUserFavoriteMeal(userId, mealId);
    }
}
