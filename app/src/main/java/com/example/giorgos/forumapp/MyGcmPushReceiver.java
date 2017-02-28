package com.example.giorgos.forumapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Random;

/**
 * Created by makan on 11/2/2017.
 */

public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    // private NotificationUtils notificationUtils;

    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */

    @Override
    public void onMessageReceived(String from, Bundle bundle) {                                     //Listener gia Notification
       //String title = bundle.getString("title");
        String msg=bundle.getString("message");
        Log.e(TAG,msg);

        Intent pushNotification = new Intent("push");
        pushNotification.putExtra("message",msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }

    // else {











}