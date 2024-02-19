package com.mahdy.mealsapplication.adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.listener.PlanListener;
import com.mahdy.mealsapplication.model.PlanMeal;
import com.mahdy.mealsapplication.model.PlanMealRepository;
import com.mahdy.mealsapplication.presenter.PlanPresenter;
import com.mahdy.mealsapplication.ui.MealDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlanMealSlotAdapter extends RecyclerView.Adapter<PlanMealSlotAdapter.MealViewHolder> {
    private static final String MEAL_ID_PARAM = "meal_details_meal_id";
    public static final String TAG = PlanMealSlotAdapter.class.getSimpleName();
    private final List<PlanMeal> dataList;
    private final PlanPresenter planPresenter;

    public PlanMealSlotAdapter(List<PlanMeal> data, Application application) {
        this.dataList = data;
        this.planPresenter = new PlanPresenter(new PlanListener() {
            @Override
            public void onReceivePlanMeals(List<PlanMeal> planMeals) {
                notifyDataSetChanged();
            }

            @Override
            public void onFailedPlanMeals(String message) {
            }
        }, new PlanMealRepository(application));
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_plan_slot, parent, false);

        MaterialCardView materialCard = itemView.findViewById(R.id.plan_meal_day_slot_image_card);
        materialCard.setOnClickListener(view -> {
            Integer mealId = (Integer) itemView.getTag();
            Intent intent = new Intent(parent.getContext(), MealDetailsActivity.class);
            intent.putExtra(MEAL_ID_PARAM, mealId);
            parent.getContext().startActivity(intent);
        });

        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        PlanMeal currentSlot = dataList.get(position);
        if (currentSlot.getTitle() == null || currentSlot.getTitle().isEmpty()) {
            holder.mealTitle.setText("N/A");
        } else {
            holder.mealTitle.setText(currentSlot.getTitle());
        }
        Picasso.get().load(currentSlot.getImageUrl()).into(holder.mealImage);
        holder.itemView.setTag(currentSlot.getMealId());

        MaterialCardView removeButton = holder.itemView.findViewById(R.id.plan_meal_day_slot_delete);
        removeButton.setOnClickListener(view -> {
            Integer slotId = currentSlot.getId();
            planPresenter.removePlanSlotMeal(slotId);
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealTitle;
        ImageView mealImage;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealTitle = itemView.findViewById(R.id.plan_meal_day_slot_title);
            mealImage = itemView.findViewById(R.id.plan_meal_day_slot_image);
        }
    }
}
