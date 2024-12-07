package com.example.xbazir;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.example.xbazir.data.entities.GroceryList;
import com.example.xbazir.ui.GroceryList.GroceryListAdapter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GroceryListAdapterTest {

    private GroceryListAdapter adapter;
    private List<GroceryList> initialData;


    // setup before starting unit test
    @Before
    public void setUp() {


        initialData = new ArrayList<>();

        // Create a sample GroceryList
        GroceryList groceryList = new GroceryList();
        groceryList.list_id = 1;
        groceryList.list_name = "Groceries";
        groceryList.food_count = 5;
        groceryList.user_id = 1;

        initialData.add(groceryList);

        // Pass mocked context
        adapter = mock(GroceryListAdapter.class);;
        adapter.updateData(initialData);
    }



    // unit test to test void removeGroceryList(int position)
    @Test
    public void testRemoveGroceryList() {
        // Verify initial item count
        assertEquals(0, adapter.getItemCount());

        // Remove the item at position 0
        adapter.removeGroceryList(0);

        // Verify that the list is now empty
        assertEquals(0, adapter.getItemCount());
    }


}
