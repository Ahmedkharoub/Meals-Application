package com.mahdy.mealsapplication.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mahdy.mealsapplication.model.UserInfo;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<Long> insert(UserInfo user);

    @Query("SELECT * FROM user")
    LiveData<List<UserInfo>> getAllUsers();

    @Query("SELECT * FROM user WHERE name = :name AND password = :password")
    LiveData<UserInfo> getUserByNameAndPassword(String name, String password);
}
