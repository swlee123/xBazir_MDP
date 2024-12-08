package com.example.xbazir;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.app.Application;
import android.icu.util.Calendar;

import com.example.xbazir.ui.ExpiryTracker.ExpiryTrackerViewModel;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UnitTest {

    @Test
    public void testCalculateRemainingDays() throws Exception {
        // Mock the Application context
        Application mockApplication = Mockito.mock(Application.class);

        // Create an instance of ExpiryTrackerViewModel
        ExpiryTrackerViewModel viewModel = new ExpiryTrackerViewModel(mockApplication);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Define the current date for testing
        Date currentDate = new Date();

        // Add 24 hours to the current date to get the expiry date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        Date expiryDate = calendar.getTime();
        String expiryDateString = format.format(expiryDate);

        // Expected remaining days is 1
        int expectedDays = 1;
        int actualDays = viewModel.calculateRemainingDays(expiryDateString);
        assertEquals(expectedDays, actualDays);
    }
}
