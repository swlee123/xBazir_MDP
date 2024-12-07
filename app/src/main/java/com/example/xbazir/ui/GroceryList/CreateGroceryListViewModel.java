package com.example.xbazir.ui.GroceryList;

import androidx.lifecycle.ViewModel;

public class CreateGroceryListViewModel extends ViewModel {

    private String listName;
    private String dueDate;

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isInputValid() {
        return listName != null && !listName.isEmpty() && dueDate != null && !dueDate.isEmpty();
    }

    public String getListName() {
        return listName;
    }

    public String getDueDate() {
        return dueDate;
    }
}
