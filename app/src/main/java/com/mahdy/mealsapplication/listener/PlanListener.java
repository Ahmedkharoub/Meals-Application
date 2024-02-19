package com.mahdy.mealsapplication.listener;

import com.mahdy.mealsapplication.model.PlanMeal;

import java.util.List;

public interface PlanListener {
    void onReceivePlanMeals(List<PlanMeal> planMeals);
    void onFailedPlanMeals(String message);
}
