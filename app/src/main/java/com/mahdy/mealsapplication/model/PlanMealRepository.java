package com.mahdy.mealsapplication.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mahdy.mealsapplication.db.MealsDatabase;
import com.mahdy.mealsapplication.db.PlanMealDao;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class PlanMealRepository {
    private PlanMealDao planMealDao;

    public PlanMealRepository(Application application) {
        MealsDatabase database = MealsDatabase.getInstance(application);
        planMealDao = database.planMealDao();
    }

    public Completable insertPlanMeal(PlanMeal planMeal) {
        return planMealDao.insertPlanMeal(planMeal);
    }

    public LiveData<List<PlanMeal>> getAllPlanMeals() {
        return planMealDao.getAllPlanMeals();
    }

    public Completable deleteWeekPlan() {
        return planMealDao.deleteWeekPlan();
    }

    public Completable updateWeekPlanSlot(int weekDay, int mealSlot, int mealId, String mealTitle, String mealImageUrl) {
        return planMealDao.updateWeekPlanSlot(weekDay, mealSlot, mealId, mealTitle, mealImageUrl);
    }

    public Completable removePlanSlotMeal(Integer id) {
        return planMealDao.removePlanSlotMeal(id);
    }
}
