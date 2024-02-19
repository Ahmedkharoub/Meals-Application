package com.mahdy.mealsapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.adapter.MealAdapter;
import com.mahdy.mealsapplication.model.MealInfo;
import com.mahdy.mealsapplication.model.MealRepository;
import com.mahdy.mealsapplication.presenter.MealPresenter;
import com.mahdy.mealsapplication.listener.MealListener;

public class MainFragment extends Fragment implements MealListener {
    private MealPresenter presenter;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        presenter = new MealPresenter(this, new MealRepository());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.random_meals_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MealAdapter mealAdapter = new MealAdapter(presenter,getActivity());
        recyclerView.setAdapter(mealAdapter);

        return view;
    }

    @Override
    public void showRandomMeal(MealInfo meal) {
        if (!isAdded()) return;
        RecyclerView recyclerView = requireView().findViewById(R.id.random_meals_list);
        MealAdapter adapter = (MealAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.updateData(meal);
        }
    }

    @Override
    public void showError(String message) {
    }

    @Override
    public void showMeal(MealInfo detailsMeal) {
    }
}
