package com.example.xbazir.ui.GroceryList;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xbazir.data.database.AppDatabase;
import com.example.xbazir.data.entities.Food;
import com.example.xbazir.data.entities.GroceryList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddFoodViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Food>> foodList = new MutableLiveData<>(new ArrayList<>());
    private final AppDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public AddFoodViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<Food>> getFoodList() {
        return foodList;
    }

    public void addFood(Food food) {
        List<Food> currentList = foodList.getValue();
        if (currentList != null) {
            currentList.add(food);
            foodList.setValue(currentList); // Notify observers
        }
    }

    public void createGroceryList(String listName, String dueDate) {
        executorService.execute(() -> {
            // Create a new GroceryList and save to database
            GroceryList groceryList = new GroceryList();
            groceryList.list_name = listName;
            groceryList.food_count = foodList.getValue() != null ? foodList.getValue().size() : 0;
            long groceryListId = db.groceryListDao().insertGroceryList(groceryList);

            // Save each food item to the database
            for (Food food : foodList.getValue()) {
                food.grocery_list_id = (int) groceryListId;
                db.foodDao().insertFood(food);
            }
        });
    }
}
