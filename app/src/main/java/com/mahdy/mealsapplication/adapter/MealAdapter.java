package com.mahdy.mealsapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.model.MealInfo;
import com.mahdy.mealsapplication.presenter.MealPresenter;
import com.mahdy.mealsapplication.ui.MealDetailsActivity;
import com.mahdy.mealsapplication.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private static final int RANDOM_MEALS_COUNT = 16;
    private static final String MEAL_ID_PARAM = "meal_details_meal_id";
    public static final String TAG = MealAdapter.class.getSimpleName();
    private MealPresenter mealPresenter;
    private final List<MealInfo> meals = new ArrayList<>(RANDOM_MEALS_COUNT);
    private Context context;
    public static final String MAIN_STACK = "MAIN_STACK";
    public MealAdapter(MealPresenter presenter,Context context) {
        this.mealPresenter = presenter;
        this.context = context;
    }

    @NonNull
    @Override
    public MealAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        Log.i(TAG, "onCreateViewHolder: Fetching a new random meal");

        boolean isConnected = NetworkUtil.isNetworkConnected(context);
        if (isConnected) {
            mealPresenter.getRandomMeal();
        } else {
            Toast.makeText(context, "You are not connected to the Internet", Toast.LENGTH_SHORT).show();
        }

        MaterialCardView materialCard = itemView.findViewById(R.id.random_meal_card_content);
        materialCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id = (Integer) itemView.getTag();
                Intent intent = new Intent(context, MealDetailsActivity.class);
                intent.putExtra(MEAL_ID_PARAM, id);
                context.startActivity(intent);
            }
        });

        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.MealViewHolder holder, int position) {
        if (meals.isEmpty()) {
            Log.i(TAG, "onBindViewHolder: Exiting early, empty meals list");
            return;
        }
        if (position > meals.size() - 1) {
            Log.i(TAG, "onBindViewHolder: Exiting early, position " + position +  " is more than meals size " + meals.size());
            return;
        }

        Log.i(TAG, "onBindViewHolder: Loading meal in position " + position);
        MealInfo meal = meals.get(position);
        holder.titleTextView.setText(meal.getTitle());
        Picasso.get().load(meal.getImageUrl()).into(holder.imageView);
        holder.conutryTextView.setText(meal.getCountry());
        holder.itemView.setTag(meal.getMealId());
    }

    @Override
    public int getItemCount() {
        return RANDOM_MEALS_COUNT;
    }

    public void updateData(MealInfo meal) {
        if (meals.size() > RANDOM_MEALS_COUNT - 1) {
            return;
        }
        meals.add(meal);
        notifyDataSetChanged();
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView conutryTextView;
        ImageView imageView;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
           titleTextView = itemView.findViewById(R.id.random_meal_title);
           imageView = itemView.findViewById(R.id.random_meal_img_card_image);
           conutryTextView = itemView.findViewById(R.id.random_meal_country);
        }
    }
}
