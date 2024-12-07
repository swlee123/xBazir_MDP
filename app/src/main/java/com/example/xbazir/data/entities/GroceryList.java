package com.example.xbazir.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "GroceryList") // Specifies the table name in the database.
public class GroceryList {

    /**
     * The primary key for the grocery list.
     * It is auto-generated by the database.
     */
    @PrimaryKey(autoGenerate = true)
    public int list_id;

    /**
     * Name of the grocery list.
     * This field allows users to identify and organize their lists.
     */
    public String list_name;

    /**
     * The number of food items in the grocery list.
     * This field helps track the total count of items within the list.
     */
    public int food_count;

    /**
     * The ID of the user associated with this grocery list.
     * Useful for distinguishing grocery lists in a multi-user system.
     */
    public int user_id;
}