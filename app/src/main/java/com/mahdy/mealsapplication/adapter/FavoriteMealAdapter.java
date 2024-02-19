package com.mahdy.mealsapplication.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.listener.FavoriteListener;
import com.mahdy.mealsapplication.model.FavoriteMeal;
import com.mahdy.mealsapplication.presenter.FavoritePresenter;
import com.mahdy.mealsapplication.ui.MealDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteMealAdapter extends RecyclerView.Adapter<FavoriteMealAdapter.FavoriteViewHolder> {

    private static final String MEAL_ID_PARAM = "meal_details_meal_id";
    public static final String TAG = FavoriteMealAdapter.class.getSimpleName();
    private final FavoritePresenter favoritePresenter;
    private List<FavoriteMeal> userFavorites;
    public static final String MAIN_STACK = "MAIN_STACK";
    private Integer userId;
    public FavoriteMealAdapter(FavoritePresenter presenter, Integer userId, List<FavoriteMeal> userFavorites) {
        this.favoritePresenter = presenter;
        this.userId = userId;
        this.userFavorites = userFavorites;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        Log.i(TAG, "onCreateViewHolder: Fetching user favorite meals (ID " + userId + ")");
        favoritePresenter.getUserFavoriteMeals(userId);

        MaterialCardView materialCard = itemView.findViewById(R.id.random_meal_card_content);
        materialCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id = (Integer) itemView.getTag();
                Intent intent = new Intent(parent.getContext(), MealDetailsActivity.class);
                intent.putExtra(MEAL_ID_PARAM, id);
                parent.getContext().startActivity(intent);
            }
        });

        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {

        Log.i(TAG, "onBindViewHolder: Loading meal in position " + position);
        FavoriteMeal meal = userFavorites.get(position);
        holder.titleTextView.setText(meal.getTitle());
        Picasso.get().load(meal.getImageUrl()).into(holder.imageView);
        holder.itemView.setTag(meal.getMealId());
    }

    @Override
    public int getItemCount() {
        return userFavorites == null ? 0 : userFavorites.size();
    }

    public void updateData(List<FavoriteMeal> favoriteMeals) {
        this.userFavorites = favoriteMeals;
        notifyDataSetChanged();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView conutryTextView;
        ImageView imageView;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
           titleTextView = itemView.findViewById(R.id.random_meal_title);
           imageView = itemView.findViewById(R.id.random_meal_img_card_image);
           conutryTextView = itemView.findViewById(R.id.random_meal_country);
        }
    }
}
