package com.mahdy.mealsapplication.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.adapter.FavoriteMealAdapter;
import com.mahdy.mealsapplication.adapter.MealAdapter;
import com.mahdy.mealsapplication.listener.FavoriteListener;
import com.mahdy.mealsapplication.model.FavoriteMeal;
import com.mahdy.mealsapplication.model.FavoriteMealRepository;
import com.mahdy.mealsapplication.model.MealRepository;
import com.mahdy.mealsapplication.presenter.FavoritePresenter;
import com.mahdy.mealsapplication.presenter.MealPresenter;

import java.util.Collections;
import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteListener {

    private FavoritePresenter presenter;

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new FavoritePresenter(this, new FavoriteMealRepository(getActivity().getApplication()));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Integer userId = sharedPreferences.getInt("userId", -1);
        presenter.getUserFavoriteMeals(userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.favorite_meals_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Integer userId = sharedPreferences.getInt("userId", -1);
        FavoriteMealAdapter mealAdapter = new FavoriteMealAdapter(presenter,userId, Collections.emptyList());
        recyclerView.setAdapter(mealAdapter);
        return view;
    }

    @Override
    public void onReceiveFavoriteMeals(List<FavoriteMeal> favoriteMeals) {
        if (!isAdded()) return;
        RecyclerView recyclerView = requireView().findViewById(R.id.favorite_meals_list);
        FavoriteMealAdapter adapter = (FavoriteMealAdapter) recyclerView.getAdapter();;
        if (adapter != null) {
            adapter.updateData(favoriteMeals);
        }
    }

    @Override
    public void onFailedFavoriteMeals(String message) {
    }
}