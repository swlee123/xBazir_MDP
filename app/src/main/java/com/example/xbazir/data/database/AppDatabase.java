package com.example.xbazir.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.xbazir.data.dao.FoodDao;
import com.example.xbazir.data.dao.GroceryListDao;
import com.example.xbazir.data.entities.Food;
import com.example.xbazir.data.entities.GroceryList;

@Database(entities = { GroceryList.class , Food.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract GroceryListDao groceryListDao();
    public abstract FoodDao foodDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "grocery_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
