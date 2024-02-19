package com.mahdy.mealsapplication.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mahdy.mealsapplication.R;
import com.mahdy.mealsapplication.listener.PlanListener;
import com.mahdy.mealsapplication.model.MealInfo;
import com.mahdy.mealsapplication.model.PlanMeal;
import com.mahdy.mealsapplication.model.PlanMealRepository;
import com.mahdy.mealsapplication.presenter.PlanPresenter;

import java.util.List;


public class BottomSheetFragment extends BottomSheetDialogFragment implements PlanListener {

    private static final String MEAL_ID_PARAM = "bottom_sheet_meal_id";
    private static final String MEAL_TITLE_PARAM = "bottom_sheet_meal_title";
    private static final String MEAL_IMAGE_PARAM = "bottom_sheet_meal_imgage_url";
    RadioGroup daysGroup;
    RadioButton day1;
    RadioButton day2;
    RadioButton day3;
    RadioButton day4;
    RadioButton day5;
    RadioButton day6;
    RadioButton day7;
    RadioGroup mealsGroup;
    RadioButton meal1;
    RadioButton meal2;
    RadioButton meal3;
    Button saveButton;
    private PlanPresenter planPresenter;

    public static BottomSheetFragment newInstance(MealInfo mealInfo) {

        Bundle args = new Bundle();
        args.putInt(MEAL_ID_PARAM, mealInfo.getMealId());
        args.putString(MEAL_TITLE_PARAM, mealInfo.getTitle());
        args.putString(MEAL_IMAGE_PARAM, mealInfo.getImageUrl());
        BottomSheetFragment fragment = new BottomSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BottomSheetFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        planPresenter = new PlanPresenter(this, new PlanMealRepository(getActivity().getApplication()));

        daysGroup = view.findViewById(R.id.daily_days_button_view);
        day1 = view.findViewById(R.id.day1);
        day2 = view.findViewById(R.id.day2);
        day3 = view.findViewById(R.id.day3);
        day4 = view.findViewById(R.id.day4);
        day5 = view.findViewById(R.id.day5);
        day6 = view.findViewById(R.id.day6);
        day7 = view.findViewById(R.id.day7);
        mealsGroup = view.findViewById(R.id.daily_meal_button_view);
        meal1 = view.findViewById(R.id.meal1);
        meal2 = view.findViewById(R.id.meal2);
        meal3 = view.findViewById(R.id.meal3);
        saveButton = view.findViewById(R.id.bottom_sheet_save_button);

        daysGroup.setOnCheckedChangeListener((group, checkedId) -> enableMealsGroup(true));

        mealsGroup.setOnCheckedChangeListener((group, checkedId) -> enableSaveButton(true));

        saveButton.setOnClickListener(v -> {
            int checkedDay = daysGroup.getCheckedRadioButtonId();
            int checkedMeal = mealsGroup.getCheckedRadioButtonId();
            if (checkedDay == -1 || checkedMeal == -1) {
                System.out.println("One of them was not selected");
                return;
            }

            if (day1.isChecked()) {
                checkedDay = 1;
            } else if (day2.isChecked()) {
                checkedDay = 2;
            } else if (day3.isChecked()) {
                checkedDay = 3;
            } else if (day4.isChecked()) {
                checkedDay = 4;
            } else if (day5.isChecked()) {
                checkedDay = 5;
            } else if (day6.isChecked()) {
                checkedDay = 6;
            } else if (day7.isChecked()) {
                checkedDay = 7;
            }

            if (meal1.isChecked()) {
                checkedMeal = 1;
            } else if (meal2.isChecked()) {
                checkedMeal = 2;
            } else if (meal3.isChecked()) {
                checkedMeal = 3;
            }

            System.out.println(checkedDay + ", " + checkedMeal);

            Bundle arguments = getArguments();
            if (arguments != null) {
                if (arguments.containsKey(MEAL_ID_PARAM) && arguments.containsKey(MEAL_TITLE_PARAM) && arguments.containsKey(MEAL_IMAGE_PARAM)) {
                    int mealId = arguments.getInt(MEAL_ID_PARAM);
                    String mealTitle = arguments.getString(MEAL_TITLE_PARAM);
                    String mealImageUrl = arguments.getString(MEAL_IMAGE_PARAM);
                    planPresenter.updatePlanMealSlot(checkedDay, checkedMeal, mealId, mealTitle, mealImageUrl);
                    dismiss();
                }
            }

        });

        enableMealsGroup(false);
        enableSaveButton(false);
        return view;
    }

    private void enableSaveButton(boolean enable) {
        if (enable) {
            saveButton.setEnabled(true);
            saveButton.setFocusable(true);
            saveButton.setClickable(true);
        } else {
            saveButton.setEnabled(false);
            saveButton.setFocusable(false);
            saveButton.setClickable(false);
        }
    }

    private void enableMealsGroup(boolean enable) {
        if (enable) {
            meal1.setEnabled(true);
            meal2.setEnabled(true);
            meal3.setEnabled(true);
            meal1.setFocusable(true);
            meal2.setFocusable(true);
            meal3.setFocusable(true);
            meal1.setClickable(true);
            meal2.setClickable(true);
            meal3.setClickable(true);
        } else {
            meal1.setEnabled(false);
            meal2.setEnabled(false);
            meal3.setEnabled(false);
            meal1.setFocusable(false);
            meal2.setFocusable(false);
            meal3.setFocusable(false);
            meal1.setClickable(false);
            meal2.setClickable(false);
            meal3.setClickable(false);
        }
    }

    @Override
    public void onReceivePlanMeals(List<PlanMeal> planMeals) {
    }

    @Override
    public void onFailedPlanMeals(String message) {
    }
}