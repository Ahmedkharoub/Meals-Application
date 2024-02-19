package com.mahdy.mealsapplication.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.listener.FavoriteListener;
import com.mahdy.mealsapplication.listener.MealListener;
import com.mahdy.mealsapplication.model.FavoriteMeal;
import com.mahdy.mealsapplication.model.FavoriteMealRepository;
import com.mahdy.mealsapplication.model.MealInfo;
import com.mahdy.mealsapplication.model.MealRepository;
import com.mahdy.mealsapplication.presenter.FavoritePresenter;
import com.mahdy.mealsapplication.presenter.MealPresenter;
import com.mahdy.mealsapplication.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsActivity extends AppCompatActivity implements MealListener, FavoriteListener {
    private ImageView mealImage;
    private TextView titleTextView, areaTextView, categoryTextView, instructionsTextView;
    private MealPresenter mealPresenter;
    private FavoritePresenter favoritePresenter;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private static final String MEAL_ID_PARAM = "meal_details_meal_id";
    private MealInfo mealInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        collapsingToolbarLayout = findViewById(R.id.mealdetails_collapsing_toolbar_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mealImage = findViewById(R.id.mealdetails_img);
        titleTextView = findViewById(R.id.mealdetails_title);
        areaTextView = findViewById(R.id.mealdetails_area);
        categoryTextView = findViewById(R.id.mealdetails_category);
        instructionsTextView = findViewById(R.id.mealdetails_instructions);
        mealPresenter = new MealPresenter(this, new MealRepository());
        favoritePresenter = new FavoritePresenter(this, new FavoriteMealRepository(getApplication()));

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.containsKey(MEAL_ID_PARAM)) {
                    int mealId = extras.getInt(MEAL_ID_PARAM, -1);
                    if (mealId != -1) {
                        boolean isConnected = NetworkUtil.isNetworkConnected(this);
                        if (isConnected) {
                            mealPresenter.getMealById(mealId);
                        } else {
                            Toast.makeText(this, "You are not connected to the Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void showRandomMeal(MealInfo meal) {
    }

    @Override
    public void showError(String message) {
    }

    @Override
    @SuppressLint("CheckResult")
    public void showMeal(MealInfo detailsMeal) {
        mealInfo = detailsMeal;
        Picasso.get().load(detailsMeal.getImageUrl()).into(mealImage);

        titleTextView.setText(detailsMeal.getTitle());
        collapsingToolbarLayout.setTitle(detailsMeal.getTitle());
        areaTextView.setText(detailsMeal.getCountry());
        categoryTextView.setText(detailsMeal.getCategory());
        instructionsTextView.setText(detailsMeal.getInstructions());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Integer userId = sharedPreferences.getInt("userId", -1);
        favoritePresenter.isMealFavorited(userId, mealInfo.getMealId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFavorited -> {
                    MenuItem favoriteMenuItem = toolbar.getMenu().findItem(R.id.add_to_fav);
                    if (isFavorited) {
                        favoriteMenuItem.setIcon(R.drawable.love);
                    } else {
                        favoriteMenuItem.setIcon(R.drawable.fav);
                    }
                }, throwable -> {
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_action, menu);
        return true;
    }

    @SuppressLint("CheckResult")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean guest = sharedPreferences.getBoolean("guest", false);
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else {
            if(id == R.id.add_to_plan) {
                if (guest) {
                    Toast.makeText(this, "You are guest", Toast.LENGTH_LONG).show();
                    return false;
                }
                showBottomSheet();
            }
            if (id == R.id.add_to_fav) {
                if (guest) {
                    Toast.makeText(this, "You are guest", Toast.LENGTH_LONG).show();
                    return false;
                }
                Integer userId = sharedPreferences.getInt("userId", -1);
                favoritePresenter.isMealFavorited(userId, mealInfo.getMealId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isFavorited -> {
                            if (isFavorited) {
                                favoritePresenter.getUserFavoriteMeal(userId, mealInfo.getMealId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(favoriteMeal -> {
                                            favoritePresenter.deleteFavoriteMeal(favoriteMeal.getId());
                                            item.setIcon(R.drawable.fav);
                                        });
                            } else {
                                favoritePresenter.insertFavoriteMeal(
                                        new FavoriteMeal(
                                                null,
                                                mealInfo.getMealId(),
                                                userId,
                                                mealInfo.getTitle(),
                                                mealInfo.getImageUrl()
                                        )
                                );
                                item.setIcon(R.drawable.love);
                            }
                        }, throwable -> {
                        });
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void showBottomSheet() {
        BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(mealInfo);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onReceiveFavoriteMeals(List<FavoriteMeal> planMeals) {
    }

    @Override
    public void onFailedFavoriteMeals(String message) {
    }
}
