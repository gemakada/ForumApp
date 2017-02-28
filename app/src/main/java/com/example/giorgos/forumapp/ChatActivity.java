package com.example.giorgos.forumapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Config;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.example.giorgos.forumapp.R.id.info;

/**
 * Created by makan on 11/2/2017.
 */

public class ChatActivity extends AppCompatActivity {
    private GoogleMap mMap;
    MapView mapview;
    private static final String LOG_TAG = MapActivity.class.getSimpleName();
    //protected static final String LOG_TAG = null;
    private static View view;
    public int postflag=0;
    public ProgressDialog PD;
    public ProgressDialog PD1;
    public AlertDialog alertDialog;
    public LatLng place;
    public String para1;
    public String para2;
    public boolean flag=false;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetBehavior card;
    private Button bottomSheetHeading;
    private View cardview;
    private BroadcastReceiver mBroadcastReceiver;
    private String message;
    private EditText edit;
    FloatingActionButton button;

    private Menu mOptionsMenu;
    public ArrayList<Discussion> Discussion_List= new ArrayList<>();

    //Fragment List=getFragmentManager().findFragmentById(R.id.mapfragment);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()

                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }

        setContentView(R.layout.activity_main);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                     //Mpainei toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Map Interaction");
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragment_container, new ChatFragment(), "Chat").commit();
        FloatingActionButton button=(FloatingActionButton)findViewById(R.id.fab);
        edit=(EditText)findViewById(R.id.message);
        button.setOnClickListener(new View.OnClickListener() {
            String response;

            @Override
            public void onClick(View v) {
                if (edit.getText()!=null) {

                    message=edit.getText().toString();
                    new Post().execute();

                }


            }
        });
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                String message=extras.getString("message");
                Discussion x=new Discussion();
                x.Set(message,"someone");
                Discussion_List.add(x);
                Log.e(LOG_TAG,message);
                FragmentManager fm = getSupportFragmentManager();
                ((ChatFragment)fm.findFragmentByTag("Chat")).Prepare();


            }




        };
    }





    public void onRestart() {
        super.onRestart();
        Log.e(LOG_TAG,"Ekane restart");

        // PedestrianMapFragment fragment = (PedestrianMapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        //fragment

    }
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,new IntentFilter("push") );

        Log.e(LOG_TAG,"Ekane resume");
    }

    public void onDestroy() {
        super.onDestroy();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem x;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        mOptionsMenu=menu;
        x=(MenuItem)menu.findItem(R.id.action_pre);
        x.setVisible(false);


        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {                                           //listener menu
        int id = item.getItemId();





        return super.onOptionsItemSelected(item);
    }

    public class Post extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ChatActivity.this);
            dialog.setTitle("Loging Out...");
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        protected ArrayList<String> doInBackground(ArrayList<String>... passing) {
            ArrayList<String> result = new ArrayList<String>();


            String response = "";


            //Toast.makeText(getActivity(),x.toString(), Toast.LENGTH_LONG).show();
            //Some calculations...
            try {

                response = MyGlobal.Post(MyGlobal.to, message);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            if ((response != null) && (response.equals("NetError"))) {
                result.add("Net Error");
                // async.setTitle(display);
            } else {

                result.add("Logout Success");

            }

            return result; //return result
        }

        protected void onPostExecute(ArrayList<String> result) {
            dialog.dismiss();
            Discussion x=new Discussion();
            x.Set(message,"Chat Message from you");
            Discussion_List.add(x);
            Log.e(LOG_TAG,message);
            FragmentManager fm = getSupportFragmentManager();
            ((ChatFragment)fm.findFragmentByTag("Chat")).Prepare();


            }
           // MakeText(result.get(0));

        }

}
