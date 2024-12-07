package com.example.xbazir.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.xbazir.data.entities.GroceryList;

import java.util.List;

/**
 * Data Access Object for performing CRUD operations on the GroceryList table in the database.
 */
@Dao
public interface GroceryListDao {

    /**
     * Inserts a new grocery list into the database.
     *
     * @param groceryList The GroceryList object to be inserted.
     * @return The ID of the newly inserted grocery list.
     */
    @Insert
    long insertGroceryList(GroceryList groceryList);

    /**
     * Deletes a grocery list from the database, but the food object data is still remain.
     *
     * @param groceryList The GroceryList object to be deleted.
     */
    @Delete
    void deleteGroceryList(GroceryList groceryList);

    /**
     * Retrieves all grocery lists stored in the database to show in grocery list fragment.
     *
     * @return A list of all GroceryList objects in the database.
     */
    @Query("SELECT * FROM GroceryList")
    List<GroceryList> getAllGroceryLists();
}
