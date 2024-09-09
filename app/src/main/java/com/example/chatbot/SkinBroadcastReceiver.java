package com.example.chatbot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SkinBroadcastReceiver extends BroadcastReceiver{
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if ("SKIN_TIP_BROADCAST".equals(action)) {
                // Handle skin tip broadcast
                showSkinTipNotification(context);
            } else if ("SUNSCREEN_REMINDER_BROADCAST".equals(action)) {
                // Handle sunscreen reminder broadcast
                showSunscreenReminderNotification(context);
            }
            // Add more conditions for other broadcasts if needed
        }
    }

    private void showSkinTipNotification(Context context) {
        Toast.makeText(context, "Skin Tip: Keep your skin hydrated!", Toast.LENGTH_SHORT).show();
        // You can also show a notification or perform other actions
    }
    private void showSunscreenReminderNotification(Context context) {
        Toast.makeText(context, "Reminder: Apply sunscreen in the morning!", Toast.LENGTH_SHORT).show();
        // You can also show a notification or perform other actions
    }
}
