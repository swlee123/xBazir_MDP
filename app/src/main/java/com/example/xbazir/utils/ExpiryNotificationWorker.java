package com.example.xbazir.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.xbazir.data.database.AppDatabase;
import com.example.xbazir.data.entities.Food;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpiryNotificationWorker extends Worker {

    public ExpiryNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        List<Food> foods = db.foodDao().getBoughtFoodsForExpiryTracker();

        StringBuilder expiringFoods = new StringBuilder();
        for (Food food : foods) {
            int remainingDays = calculateRemainingDays(food.expiry_date);
            int usedSpoiledTotal = food.used + food.spoiled;

            // Check if food meets notification criteria
            if (remainingDays <= 3 && usedSpoiledTotal < food.food_quantity) {
                expiringFoods.append(food.food_name).append(", ");
            }
        }

        if (expiringFoods.length() > 0) {
            // Remove the trailing comma and space
            String foodList = expiringFoods.substring(0, expiringFoods.length() - 2);
            String notificationMessage = "Your " + foodList + " will expire soon!";

            // Send notification with the food list
            NotificationHelper.sendNotification(
                    getApplicationContext(),
                    "Food Expiry Alert",
                    notificationMessage,
                    foodList
            );
        }

        return Result.success();
    }
    private int calculateRemainingDays(String expiryDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date expiry = format.parse(expiryDate);
            Date today = new Date();
            long diff = expiry.getTime() - today.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
