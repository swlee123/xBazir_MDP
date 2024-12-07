package com.example.xbazir.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Food",
        foreignKeys = @ForeignKey(
                entity = GroceryList.class,
                parentColumns = "list_id",
                childColumns = "grocery_list_id",
                onDelete = ForeignKey.SET_NULL
        )
        // when ForeignKey grocery_list_id is deleted, the food associated to the list remains
)
public class Food {
    @PrimaryKey(autoGenerate = true)
    public int food_id; // uniqiue id for each food
    public Integer grocery_list_id; // Foreign key to GroceryList

    public String food_name; // food name
    public int food_quantity; // number of food
    public String remarks; // description of food
    public boolean is_bought; // whether food is bought or not
    public String expiry_date; // expiry date of food
    public String bought_date; // bought date of food
    public int notification_days_before; // day before expiry to start notify user

    public int used =0 ; // used quantity of food
    public int spoiled = 0; // spoiled quantity of food


}
