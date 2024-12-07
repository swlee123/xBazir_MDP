package com.example.xbazir.ui.GroceryList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xbazir.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


// done mvc
public class ViewGroceryFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_grocery, container, false);

        // Get groceryListId from arguments
        int groceryListId = getArguments() != null ? getArguments().getInt("groceryListId") : -1;

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);

        // Set up ViewPager2 adapter with groceryListId
        ViewGroceryPageAdapter adapter = new ViewGroceryPageAdapter(this, groceryListId);
        viewPager.setAdapter(adapter);

        // Link the TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Not Bought");
                    } else {
                        tab.setText("Bought");
                    }
                }).attach();

        return view;
    }

}
