package com.example.xbazir;

import com.example.xbazir.ui.ExpiryTracker.ExpiryTrackerAdapter;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {


    // Expiry Tracker Unit Test
    @Test
    // Test 1 : Test usage of testCalculateRemainingDays() to give number of days to certain date in future.
    public void testCalculateRemainingDays() {
        ExpiryTrackerAdapter adapter = new ExpiryTrackerAdapter(null, null, null);

        String futureDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date(System.currentTimeMillis() + (5 * 24 * 60 * 60 * 1000)));
        // 5th date starting from today's date , so answer should be 4 days  , because today count too



    }

    // Grocery List Unit Test

    // Recipe Recommender Unit Test

    // Home Unit Test
}

