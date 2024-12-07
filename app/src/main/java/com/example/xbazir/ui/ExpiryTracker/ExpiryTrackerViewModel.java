package com.example.xbazir.ui.ExpiryTracker;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xbazir.data.database.AppDatabase;
import com.example.xbazir.data.entities.Food;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpiryTrackerViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Food>> foodList = new MutableLiveData<>();
    private final AppDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ExpiryTrackerViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<Food>> getFoodList() {
        return foodList;
    }

    public void loadFoodData() {
        executorService.execute(() -> {
            List<Food> boughtFoods = db.foodDao().getBoughtFoodsForExpiryTracker();
            foodList.postValue(boughtFoods);
        });
    }

    public void updateUsedQuantity(int foodId, int usedQuantity) {
        executorService.execute(() -> {
            db.foodDao().updateFoodUsed(foodId, usedQuantity);
            loadFoodData(); // Refresh the list after update
        });
    }

    public void updateSpoiledQuantity(int foodId, int spoiledQuantity) {
        executorService.execute(() -> {
            db.foodDao().updateFoodSpoiled(foodId, spoiledQuantity);
            loadFoodData(); // Refresh the list after update
        });
    }

    public int calculateRemainingDays(String expiryDate) {
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
