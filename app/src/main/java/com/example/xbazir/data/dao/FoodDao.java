package com.example.xbazir.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.xbazir.data.entities.Food;

import java.util.List;

/**
 * Data Access Object for performing CRUD operations on the Food table in the database.
 */
@Dao
public interface FoodDao {

    /**
     * Inserts a new food item into the database.
     *
     * @param food The food obj to be inserted.
     * @return The ID of the newly inserted food item.
     */
    @Insert
    long insertFood(Food food);

    /**
     * Updates the 'used' field of a food item in the database.
     *
     * @param foodId The ID of the food item to be updated.
     * @param used   The new value for the 'used' field.
     */
    @Query("UPDATE Food SET used = :used WHERE food_id = :foodId")
    void updateFoodUsed(int foodId, int used);

    /**
     * Updates the 'spoiled' field of a food item in the database.
     *
     * @param foodId  The ID of the food item to be updated.
     * @param spoiled The new value for the 'spoiled' field.
     */
    @Query("UPDATE Food SET spoiled = :spoiled WHERE food_id = :foodId")
    void updateFoodSpoiled(int foodId, int spoiled);

    /**
     * Retrieves a list of food items that have been marked as bought.
     *
     * @return A list of food items with 'is_bought' set to 1.
     */
    @Query("SELECT * FROM Food WHERE is_bought = 1")
    List<Food> getBoughtFoodsForExpiryTracker();

    /**
     * Retrieves a list of food items with remaining quantities (quantity > used).
     *
     * @return A list of food items with available quantities.
     */
    @Query("SELECT food_name FROM food WHERE food_quantity >  used and is_bought = 1")
    List<String> getFoodWithRemainingQuantity();

    /**
     * Updates the quantity of a food item in the database.
     *
     * @param foodId   The ID of the food item to be updated.
     * @param quantity The new quantity value.
     */
    @Query("UPDATE Food SET food_quantity = :quantity WHERE food_id = :foodId")
    void updateFoodQuantity(int foodId, int quantity);

    /**
     * Deletes a food item from the database based on its ID.
     *
     * @param foodId The ID of the food item to be deleted.
     */
    @Query("DELETE FROM Food WHERE food_id = :foodId")
    void deleteFood(int foodId);

    /**
     * Updates the number of days before a notification is triggered for a food item.
     *
     * @param foodId The ID of the food item to be updated.
     * @param days   The number of notification days before expiry.
     */
    @Query("UPDATE Food SET notification_days_before = :days WHERE food_id = :foodId")
    void updateFoodNotificationDays(int foodId, int days);

    /**
     * Retrieves a list of food items associated with a specific grocery list.
     *
     * @param groceryListId The ID of the grocery list.
     * @return A list of food items in the specified grocery list.
     */
    @Query("SELECT * FROM Food WHERE grocery_list_id = :groceryListId")
    List<Food> getFoodForGroceryList(int groceryListId);

    /**
     * Retrieves a list of food items based on their 'is_bought' status.
     *
     * @param isBought The 'is_bought' status to filter by (true/false).
     * @return A list of food items matching the specified 'is_bought' status.
     */
    @Query("SELECT * FROM Food WHERE is_bought = :isBought")
    List<Food> getFoodsByBoughtStatus(boolean isBought);

    /**
     * Updates the expiry date, 'is_bought' status, and bought date of a food item.
     *
     * @param foodId     The ID of the food item to be updated.
     * @param expiryDate The new expiry date.
     * @param isBought   The new 'is_bought' status.
     * @param boughtDate The new bought date.
     */
    @Query("UPDATE Food SET expiry_date = :expiryDate, is_bought = :isBought, bought_date = :boughtDate WHERE food_id = :foodId")
    void updateFoodExpiryDateAndStatus(int foodId, String expiryDate, boolean isBought, String boughtDate);

    /**
     * Retrieves a list of food items from a specific grocery list and with a specific 'is_bought' status.
     *
     * @param groceryListId The ID of the grocery list.
     * @param isBought      The 'is_bought' status to filter by (true/false).
     * @return A list of food items matching the specified criteria.
     */
    @Query("SELECT * FROM Food WHERE grocery_list_id = :groceryListId AND is_bought = :isBought")
    List<Food> getFoodForGroceryListAndBoughtStatus(int groceryListId, boolean isBought);
}
