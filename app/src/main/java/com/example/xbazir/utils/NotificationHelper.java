package com.example.xbazir.utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.xbazir.MainActivity;
import com.example.xbazir.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationHelper {

    private static final String CHANNEL_ID = "expiry_reminder";

    private static final String PREFS_NAME = "ExpiryTrackerPrefs";
    private static final String LAST_NOTIFIED_DATE_KEY = "lastNotifiedDate";


    // Create Notification Channel
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Expiry Reminder";
            String description = "Reminders for foods about to expire";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    // Send Notification with Action to Open ExpiryTrackerFragment
    public static void sendNotification(Context context, String title, String message, String foodList) {

        // Ensure notifications are sent only once per day
        if (!shouldSendNotification(context)) {
            Log.d("NotificationHelper", "Notification already sent today.");
            return;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d("NotificationHelper", "Notification permission not granted.");
                return; // Exit if permission is not granted
            }
        }



        // Intent to open MainActivity and navigate to ExpiryTrackerFragment
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("navigateTo", "ExpiryTracker"); // Add a flag to indicate navigation to ExpiryTrackerFragment
        intent.putExtra("foodList", foodList); // Optional: Pass the list of expiring foods

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm) // Replace with a valid icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent) // Set the action to open the app
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Add a unique notification ID to differentiate notifications
        int notificationId = (int) System.currentTimeMillis(); // Use the current time as a unique ID
        notificationManager.notify(notificationId, builder.build());
    }

    // Check and Request Notification Permission
    public static void checkAndRequestNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    activity.requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
                }
            }
        }
    }

    // Check if notification should be sent
    private static boolean shouldSendNotification(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastNotifiedDate = preferences.getString(LAST_NOTIFIED_DATE_KEY, "");

        // Get today's date as a String
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        // Return true if the notification hasn't been sent today
        return !lastNotifiedDate.equals(today);
    }

    // Update the last notified date in SharedPreferences
    private static void updateLastNotifiedDate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        preferences.edit().putString(LAST_NOTIFIED_DATE_KEY, today).apply();
    }


}
