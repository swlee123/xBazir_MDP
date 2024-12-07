package com.example.xbazir.ui.GroceryList;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xbazir.data.database.AppDatabase;
import com.example.xbazir.data.entities.GroceryList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroceryListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<GroceryList>> groceryLists = new MutableLiveData<>();
    private final AppDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public GroceryListViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<GroceryList>> getGroceryLists() {
        return groceryLists;
    }

    public void loadGroceryLists() {
        executorService.execute(() -> {
            List<GroceryList> lists = db.groceryListDao().getAllGroceryLists();
            groceryLists.postValue(lists);
        });
    }

    public void deleteGroceryList(GroceryList groceryList) {
        executorService.execute(() -> {
            db.groceryListDao().deleteGroceryList(groceryList);
            loadGroceryLists(); // Refresh data after deletion
        });
    }
}
