package com.mahdy.mealsapplication.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mahdy.mealsapplication.model.PlanMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface PlanMealDao {
    @Insert
    Completable insertPlanMeal(PlanMeal planMeal);

    @Query("SELECT * FROM plan_meal")
    LiveData<List<PlanMeal>> getAllPlanMeals();

    @Query("DELETE FROM plan_meal WHERE id > 0")
    Completable deleteWeekPlan();

    @Query("UPDATE plan_meal SET mealId = :mealId, title = :mealTitle, imageUrl = :mealImageUrl WHERE weekDay = :weekDay AND daySlot = :mealSlot")
    Completable updateWeekPlanSlot(int weekDay, int mealSlot, int mealId, String mealTitle, String mealImageUrl);

    @Query("UPDATE  plan_meal SET mealId = NULL, title = NULL, imageUrl = NULL WHERE id = :id")
    Completable removePlanSlotMeal(Integer id);
}
