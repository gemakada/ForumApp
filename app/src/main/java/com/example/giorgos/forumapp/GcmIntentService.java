package com.example.giorgos.forumapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.giorgos.forumapp.R;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by makan on 11/2/2017.
 */

public class GcmIntentService extends IntentService {
    private static final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService() {
        super(TAG);
    }

    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";


    @Override
    protected void onHandleIntent(Intent intent) {
        // String key = intent.getStringExtra(KEY);
        //  switch (key) {
        //     case SUBSCRIBE:
        // subscribe to a topic
        // String topic = intent.getStringExtra(TOPIC);

        //   break;
        //   case UNSUBSCRIBE:
        //   String topic1 = intent.getStringExtra(TOPIC);

        //   break;
        //  default:
        // if key is not specified, register with GCM
        registerGCM();
    }



    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private void registerGCM() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = null;


        try {
            //sundesi me google API
            InstanceID instanceID = InstanceID.getInstance(this);

            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

           // MyGlobal.key=token;

            Log.e(TAG, "GCM Registration Token: " + token);
            MyGlobal.key=token;
            //tupwsi registration key efoson liftei

            // sending the registration id to our server
            //  sendRegistrationToServer(token);


        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);


        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.

    }

    private void sendRegistrationToServer(final String token) {
        // Send the registration token to our server
        // to keep it in MySQL

    }

    /**
     * Subscribe to a topic
     */


}