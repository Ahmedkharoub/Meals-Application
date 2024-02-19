package com.mahdy.mealsapplication.model;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;

import com.mahdy.mealsapplication.db.MealsDatabase;
import com.mahdy.mealsapplication.db.UserDao;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class UserInfoRepository {
    private UserDao userDao;
    private LiveData<List<UserInfo>> allUsers;

    public UserInfoRepository(Application application) {
        MealsDatabase database = MealsDatabase.getInstance(application);
        userDao = database.userDao();
        allUsers = userDao.getAllUsers();
    }

    public Single<Long> insert(UserInfo user) {
        return userDao.insert(user);
    }

    public LiveData<List<UserInfo>> getAllUsers() {
        return allUsers;
    }

    public LiveData<UserInfo> getUserByNameAndPassword(String name, String password) {
        return userDao.getUserByNameAndPassword(name, password);
    }
}
