package com.mahdy.mealsapplication.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.adapter.PlanMealAdapter;
import com.mahdy.mealsapplication.listener.PlanListener;
import com.mahdy.mealsapplication.model.PlanMeal;
import com.mahdy.mealsapplication.model.PlanMealRepository;
import com.mahdy.mealsapplication.presenter.PlanPresenter;

import java.util.Calendar;
import java.util.List;

public class PlanFragment extends Fragment implements PlanListener {
    private PlanPresenter presenter;
    private static final String TAG = PlanFragment.class.getSimpleName();
    public static final String WEEK_KEY = "stored_week_key";
    PlanMealAdapter planMealAdapter;
    RecyclerView recyclerView;

    public PlanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        presenter = new PlanPresenter(this, new PlanMealRepository(getActivity().getApplication()));
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        Log.i(TAG, "onReceivePlanMeals: dayOfWeek = " + dayOfWeek);
        Log.i(TAG, "onReceivePlanMeals: first day of week = " + calendar.getFirstDayOfWeek());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Integer storedWeek = sharedPreferences.getInt(WEEK_KEY, -1);
        if (storedWeek == -1) {
            Log.i(TAG, "onCreate: There's no stored week, deleting plan and repopulating it");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(WEEK_KEY, currentWeek);
            editor.apply();
            presenter.resetWeeklyPlan();
        } else {
            Log.i(TAG, "onCreate: Found stored week: " + currentWeek);
            if (storedWeek != currentWeek) {
                Log.i(TAG, "onCreate: Weeks do not match, flushing plan");
                presenter.resetWeeklyPlan();
            } else {
                Log.i(TAG, "onCreate: Weeks match. Returning weekly plan");
                presenter.getWeeklyPlan();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_plan, container, false);
        planMealAdapter = new PlanMealAdapter(getActivity().getApplication());
        recyclerView = view.findViewById(R.id.weekly_plan_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(planMealAdapter);
        return view;
    }

    @Override
    public void onReceivePlanMeals(List<PlanMeal> planMeals) {
        Log.i(TAG, "onReceivePlanMeals: Received " + planMeals.size() + " plan meals");
        if (planMeals != null) {
            if (planMeals.isEmpty()) {
                presenter.resetWeeklyPlan();

            } else {
                Calendar calendar = Calendar.getInstance();
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                planMealAdapter.updateData(planMeals);
            }
        }
    }

    @Override
    public void onFailedPlanMeals(String message) {
        Log.i(TAG, "onFailedPlanMeals: " + message);
    }
}
