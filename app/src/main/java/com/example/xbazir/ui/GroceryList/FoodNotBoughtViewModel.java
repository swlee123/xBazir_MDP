package com.example.xbazir.ui.GroceryList;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xbazir.data.database.AppDatabase;
import com.example.xbazir.data.entities.Food;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodNotBoughtViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Food>> foodList = new MutableLiveData<>();
    private final AppDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public FoodNotBoughtViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<Food>> getFoodList() {
        return foodList;
    }

    public void loadFood(int groceryListId) {
        executorService.execute(() -> {
            List<Food> foods = db.foodDao().getFoodForGroceryListAndBoughtStatus(groceryListId, false);
            foodList.postValue(foods); // Post value to LiveData
        });
    }
}
