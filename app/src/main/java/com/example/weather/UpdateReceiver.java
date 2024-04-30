package com.example.weather;

import static com.example.weather.Constants.ACTION_FETCH_WEATHER_DATA;
import static com.example.weather.Constants.REQUEST_CODE_NOTIFICATION_PERMISSION;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Date;

/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class UpdateReceiver extends BroadcastReceiver {
    private static final String TAG = "UpdateReceiver";
    private static final String CHANNEL_ID = "weather_update_channel";
    private static final int NOTIFICATION_ID = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("UpdateReceiver", "Data fetch triggered at: " + new Date().toString());
        Intent localIntent = new Intent(ACTION_FETCH_WEATHER_DATA);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

        NotificationHelper.sendNotification(context, "Weather Update", "Weather information has been updated successfully!");

    }

}
