package com.mahdy.mealsapplication.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mahdy.mealsapplication.model.FavoriteMeal;
import com.mahdy.mealsapplication.model.PlanMeal;
import com.mahdy.mealsapplication.model.UserInfo;

@Database(entities = {UserInfo.class, PlanMeal.class, FavoriteMeal.class}, version = 4, exportSchema = false)
public abstract class MealsDatabase extends RoomDatabase {
    private static MealsDatabase instance = null;

    public abstract UserDao userDao();

    public abstract PlanMealDao planMealDao();
    public abstract FavoriteMealDao favoriteMealDao();

    public static synchronized MealsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MealsDatabase.class, "userdb")
                    .addMigrations(new Migration(1, 2) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {
                            database.execSQL("CREATE TABLE IF NOT EXISTS `plan_meal` " +
                                    "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                    "`mealId` INTEGER, " +
                                    "`weekDay` INTEGER, " +
                                    "`daySlot` INTEGER)");
                        }
                    }, new Migration(2, 3) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {
                            database.execSQL("CREATE TABLE IF NOT EXISTS `plan_meal_temp` " +
                                    "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                    "`mealId` INTEGER, " +
                                    "`weekDay` INTEGER, " +
                                    "`daySlot` INTEGER, " +
                                    "`title` TEXT, " +
                                    "`imageUrl` TEXT)");
                            database.execSQL("INSERT INTO plan_meal_temp (id, mealId, weekDay, daySlot) " +
                                    "SELECT id, mealId, weekDay, daySlot FROM plan_meal");
                            database.execSQL("DROP TABLE plan_meal");
                            database.execSQL("ALTER TABLE plan_meal_temp RENAME TO plan_meal");
                        }
                    }, new Migration(3,4) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {
                            database.execSQL("CREATE TABLE IF NOT EXISTS `favorite_meal` " +
                                    "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                    "`mealId` INTEGER," +
                                    "`userId` INTEGER," +
                                    "`title` TEXT," +
                                    "`imageUrl` TEXT)");
                        }
                    })
                    .build();
        }
        return instance;
    }
}
