package com.mahdy.mealsapplication.presenter;

import com.mahdy.mealsapplication.model.MealRepository;
import com.mahdy.mealsapplication.model.MealResponse;
import com.mahdy.mealsapplication.listener.MealListener;
import com.mahdy.mealsapplication.util.NetworkUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealPresenter {
    private MealListener view;
    private final MealRepository repository;

    public MealPresenter(MealListener view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void getRandomMeal() {
        repository.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccess, this::handleError);
    }

    private void handleSuccess(MealResponse mealResponse) {
        if (mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
            view.showRandomMeal(mealResponse.getMeals().get(0));
        } else {
            view.showError("No meal found");
        }
    }

    private void handleError(Throwable throwable) {
        view.showError(throwable.getMessage());
    }

    public void getMealById(Integer id) {
        repository.getMealById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MealResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(MealResponse mealResponse) {
                        if (mealResponse != null && mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
                            view.showMeal(mealResponse.getMeals().get(0));
                        } else {
                            view.showError("Meal not found");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getMessage());
                    }
                });
    }
}
