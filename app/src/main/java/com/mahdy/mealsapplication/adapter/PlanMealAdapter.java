package com.mahdy.mealsapplication.adapter;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.model.PlanMeal;
import com.mahdy.mealsapplication.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanMealAdapter extends RecyclerView.Adapter<PlanMealAdapter.PlanViewHolder> {
    private static final String MEAL_ID_PARAM = "meal_details_meal_id";
    public static final String TAG = PlanMealAdapter.class.getSimpleName();
    private Map<Integer, List<PlanMeal>> planMeals;
    private Application application;

    public PlanMealAdapter(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_plan, parent, false);

        return new PlanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        if (planMeals.isEmpty()) {
            Log.i(TAG, "onBindViewHolder: Exiting early, empty meals list");
            return;
        }
        Log.i(TAG, "onBindViewHolder: Loading plan meal in position " + position);
        Calendar calendar = Calendar.getInstance();
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        int currentDay = (firstDayOfWeek + position - 1) % 7 + 1;
        holder.dayLabel.setText(DateUtil.getWeekDayName(currentDay));
        List<PlanMeal> currentDaySlots = planMeals.get(currentDay);
        Log.i(TAG, "onBindViewHolder: Creating a new adapter with " + currentDaySlots.size() + " meal slots");
        for (PlanMeal currentDaySlot : currentDaySlots) {
            System.out.println(currentDaySlot.getDaySlot());
        }
        holder.slotsList.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.slotsList.setAdapter(new PlanMealSlotAdapter(currentDaySlots, application));
    }

    @Override
    public int getItemCount() {
        return planMeals == null ? 0 : planMeals.size();
    }

    public void updateData(List<PlanMeal> planMeals) {
        Map<Integer, List<PlanMeal>> groupedPlanMeals = new HashMap<>();

        for (PlanMeal planMeal : planMeals) {
            List<PlanMeal> mealsForWeekDay = groupedPlanMeals.getOrDefault(planMeal.getWeekDay(), new ArrayList<>());
            mealsForWeekDay.add(planMeal);
            groupedPlanMeals.put(planMeal.getWeekDay(), mealsForWeekDay);
        }

        this.planMeals = groupedPlanMeals;

        notifyDataSetChanged();
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView dayLabel;
        RecyclerView slotsList;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            dayLabel = itemView.findViewById(R.id.plan_meal_day_label);
            slotsList = itemView.findViewById(R.id.plan_meal_day_slots_list);
        }
    }
}
