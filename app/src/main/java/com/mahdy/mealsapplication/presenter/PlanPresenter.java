package com.mahdy.mealsapplication.presenter;

import android.util.Log;

import com.mahdy.mealsapplication.listener.PlanListener;
import com.mahdy.mealsapplication.model.PlanMeal;
import com.mahdy.mealsapplication.model.PlanMealRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanPresenter {
    private PlanListener listener;
    private final PlanMealRepository repository;
    private static final String TAG = PlanPresenter.class.getSimpleName();

    public PlanPresenter(PlanListener listener, PlanMealRepository repository) {
        this.listener = listener;
        this.repository = repository;
    }

    public void insertWeeklyPlanMeal(PlanMeal planMeal) {
        repository.insertPlanMeal(planMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.i(TAG, "insertWeeklyPlanMean: inserted ??? records");
                }, throwable -> {
                    Log.e(TAG, "insertWeeklyPlanMean: failed to insert records", throwable);
                });
    }

    public Disposable deleteWeekPlan() {
        return repository.deleteWeekPlan()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.i(TAG, "insertWeeklyPlanMean: deleted ??? records");
                }, throwable -> {
                    Log.e(TAG, "insertWeeklyPlanMean: failed to delete records", throwable);
                });
    }

    public void resetWeeklyPlan() {
        List<Completable> insertOperations = new ArrayList<>();
        for (int dayIndex = 1; dayIndex <= 7; ++dayIndex) {
            for (int slotIndex = 1; slotIndex <= 3; ++slotIndex) {
                PlanMeal currentMeal = new PlanMeal(null, null, dayIndex, slotIndex, null, null);
                insertOperations.add(repository.insertPlanMeal(currentMeal));
            }
        }
        repository.deleteWeekPlan()
                .andThen(Completable.mergeArray(insertOperations.toArray(new Completable[0])))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.i(TAG, "insertWeeklyPlanMean: successfully reset weekly plan");
                }, throwable -> {
                    Log.e(TAG, "insertWeeklyPlanMean: failed to reset weekly plan", throwable);
                });
    }

    public void getWeeklyPlan() {
        repository.getAllPlanMeals()
                .observeForever(planMeals -> {
                    Log.i(TAG, "onChanged: WE GOT PLAN MEALS: " + planMeals.size());
                    listener.onReceivePlanMeals(planMeals);
                });
    }

    public void updatePlanMealSlot(int weekDay, int mealSlot, int mealId, String mealTitle, String mealImageUrl) {
        Log.i(TAG, "updatePlanMealSlot: updating plan slot " + weekDay + " -> " + mealSlot + " to add " + mealTitle + " (" + mealImageUrl + ")");
        repository.updateWeekPlanSlot(weekDay, mealSlot, mealId, mealTitle, mealImageUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.i(TAG, "insertWeeklyPlanMean: added to plan " + mealTitle);
                }, throwable -> {
                    Log.e(TAG, "insertWeeklyPlanMean: failed to add " + mealTitle + " to plan", throwable);
                });
    }

    public void removePlanSlotMeal(Integer id) {
        Log.i(TAG, "updatePlanMealSlot: removing plan slot " + id);
        repository.removePlanSlotMeal(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.i(TAG, "insertWeeklyPlanMean: removed plan slot " + id);
                }, throwable -> {
                    Log.e(TAG, "insertWeeklyPlanMean: failed to remove plan slot " + id, throwable);
                });
    }
}
