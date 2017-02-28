/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.giorgos.forumapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.Marker;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;
import static com.example.giorgos.forumapp.R.styleable.Toolbar;

//import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    private Bundle instance;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private static final String TAG =MainActivity.class.getSimpleName();
    public boolean flag;
    public int thesi;
    private FloatingActionButton button;
    private View separator;
    private View view;
    private EditText edit;
    public ProgressDialog PD;
    public List<Topic> Topic_List=new ArrayList<>();
    public List<Discussion> Discussion_List=new ArrayList<>();
    private String topic;
    private Intent servise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        instance = savedInstanceState;
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()

                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }
        flag=false;
        PD=new ProgressDialog(MainActivity.this);
        PD.setTitle("Please Wait..");
        PD.setMessage("Loading...");
        PD.setCancelable(false);
        setContentView(R.layout.activity_main);                                 //Emfanisi Main Activity
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                     //Mpainei toolbar
        setSupportActionBar(toolbar);
        separator=(View)findViewById(R.id.separate);
        separator.setVisibility(View.INVISIBLE);
        button=(FloatingActionButton)findViewById(R.id.fab);
        button.setVisibility(View.INVISIBLE);
        edit=(EditText)findViewById(R.id.message);
        edit.setVisibility(View.INVISIBLE);
        SetUpList();
        servise=new Intent(getBaseContext(), GcmIntentService.class);
        startService(servise);

        button.setOnClickListener(new View.OnClickListener() {
            String response;

            @Override
            public void onClick(View v) {
                if (edit.getText()!=null) {
                    PostMessage(edit.getText().toString());
                }


            }
        });


        // Setting ViewPager for each Tabs
       // FragmentManager fm = getSupportFragmentManager();
        //Fragment fragment = fm.findFragmentById(R.id.topics);

       // fm.beginTransaction().add(R.id.fragment_container, new TopicsFragment()).commit();



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {

        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.

    }

    public void onPause() {
        super.onPause();
        //context.unregisterReceiver(mRegistrationBroadcastReceiver);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }


    protected void onResume() {
        super.onResume();


    }

    public void onRestart() {
        super.onRestart();


    }





    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }



    public void Test(int position) {

        thesi=1;
        ActivityCompat.invalidateOptionsMenu(MainActivity.this);
        separator=(View)findViewById(R.id.separate);
        separator.setVisibility(View.VISIBLE);
        button=(FloatingActionButton)findViewById(R.id.fab);
        button.setVisibility(View.VISIBLE);
        edit=(EditText)findViewById(R.id.message);
        edit.setVisibility(View.VISIBLE);
            Topic who=Topic_List.get(position);
            String cat=who.Get_name();
            Log.e(TAG,"lalalalalala");
        topic=cat;
        SetUpDiscussion(cat);


    }

    @Override
    public void onBackPressed() {
        ActivityCompat.invalidateOptionsMenu(MainActivity.this);
        separator=(View)findViewById(R.id.separate);
        separator.setVisibility(View.INVISIBLE);
        button=(FloatingActionButton)findViewById(R.id.fab);
        button.setVisibility(View.INVISIBLE);
        edit=(EditText)findViewById(R.id.message);
        edit.setVisibility(View.INVISIBLE);
        Discussion_List=new ArrayList<>();
        thesi=0;
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount() != 0) {
            fm.popBackStack();


            Log.e(TAG,"MPikeeee");
        } else {

            super.onBackPressed();
        }
    }


    private void SetUpList() {                                                                       //Katevasma Markers
        PD.show();
        new Thread(new Runnable() {
            public void run() {
                // Toast.makeText(getActivity(),"check4", Toast.LENGTH_SHORT).show();
                try {

                    retrieveTopics();

                } catch (IOException e) {
                    //Log.e(LOG_TAG, "Cannot retrive cities", e);
                    //((MainActivity)getActivity()).PD.dismiss();

                    return;
                }
            }
        }).start();
    }

    private void SetUpDiscussion(final String topic) {                                                                       //Katevasma Markers
        PD.show();
        Log.e(TAG,"SetUp");
        new Thread(new Runnable() {
            public void run() {
                // Toast.makeText(getActivity(),"check4", Toast.LENGTH_SHORT).show();
                try {

                    Discussions(topic);

                } catch (IOException e) {
                    Log.e(TAG, "Cannot retrive cities", e);
                    //((MainActivity)getActivity()).PD.dismiss();

                    return;
                }
            }
        }).start();
    }

    private void PostTopic(final String topic) {                                                                       //Katevasma Markers
        PD.show();
        new Thread(new Runnable() {
            public void run() {
                // Toast.makeText(getActivity(),"check4", Toast.LENGTH_SHORT).show();
                try {

                    String result=PostNew(topic,"noop",1);

                } catch (IOException e) {
                    //Log.e(LOG_TAG, "Cannot retrive cities", e);
                    //((MainActivity)getActivity()).PD.dismiss();

                    return;
                }
            }
        }).start();
    }

    private void PostMessage(final String message) {                                                                       //Katevasma Markers
        PD.show();
        new Thread(new Runnable() {
            public void run() {
                // Toast.makeText(getActivity(),"check4", Toast.LENGTH_SHORT).show();
                try {

                    String result=PostNew(topic,message,2);

                } catch (IOException e) {
                    //Log.e(LOG_TAG, "Cannot retrive cities", e);
                    //((MainActivity)getActivity()).PD.dismiss();

                    return;
                }
            }
        }).start();
    }




    protected void retrieveTopics() throws IOException {                                      //Fortwsi apo server kai epeksergasia Json listas
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        final StringEntity userDataStringEntity;
        try {

            //  Toast.makeText(MainActivity.this,"check15555", Toast.LENGTH_SHORT).show();
            // Connect to the web service

            URL url = new URL("http://"+MyGlobal.ip+":8080/ServerForum/Servlet.do");
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream(),"UTF-8");

            // Read the JSON data into the StringBuilder

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
            userDataStringEntity = new StringEntity(json.toString(), "UTF-8");
            //String x=new String()




        } catch (IOException e) {
           // Log.e(LOG_TAG, "Error connecting to service", e);
            throw new IOException("Error connecting to serviceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }



        try {
            //    Toast.makeText(this,"check5", Toast.LENGTH_SHORT).show();
            //   Toast.makeText(MainActivity.this, "You selected :", Toast.LENGTH_SHORT).show();


           // Log.e(LOG_TAG,json.toString());

            JSONObject json1 = new JSONObject(json.toString());
            // System.out.print(json);
            JSONArray array = json1.getJSONArray("topics");
            createTopicList(array);
            //   Toast.makeText(MainActivity.this, "check15", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
         //   Log.e(LOG_TAG, "Error processing JSON", e);
        }
    }


    protected void Discussions(String topic) throws IOException {
        String i1 = URLEncoder.encode(topic, "UTF-8");
        String who=URLEncoder.encode(MyGlobal.GetUsername(),"UTF-8");//Fortwsi apo server kai epeksergasia Json listas
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        final StringEntity userDataStringEntity;
        Log.e(TAG,"Mpainei kai edw342423");
        try {
            Log.e(TAG,"Mpainei kai edw");
            //  Toast.makeText(MainActivity.this,"check15555", Toast.LENGTH_SHORT).show();
            // Connect to the web service

            URL url = new URL("http://"+MyGlobal.ip+":8080/ServerForum/Servlet.do?username="+who+"&intopic="+i1);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream(),"UTF-8");

            // Read the JSON data into the StringBuilder

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
            userDataStringEntity = new StringEntity(json.toString(), "UTF-8");
            //String x=new String()




        } catch (IOException e) {
            // Log.e(LOG_TAG, "Error connecting to service", e);
            throw new IOException("Error connecting to serviceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }



        try {
            //    Toast.makeText(this,"check5", Toast.LENGTH_SHORT).show();
            //   Toast.makeText(MainActivity.this, "You selected :", Toast.LENGTH_SHORT).show();


            // Log.e(LOG_TAG,json.toString());

            JSONObject json1 = new JSONObject(json.toString());
            // System.out.print(json);
            JSONArray array = json1.getJSONArray("messages");
            createDiscussionList(array);
            //   Toast.makeText(MainActivity.this, "check15", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            //   Log.e(LOG_TAG, "Error processing JSON", e);
        }
    }









    public void createTopicList(JSONArray Topics) throws JSONException {



        for (int i=0; i<Topics.length(); i++) {

            JSONObject jsonObj = Topics.getJSONObject(i);
            String x1=jsonObj.getString("name");
            String x2=jsonObj.getString("topic");
            Topic obj=new Topic();
            obj.Set(x2,x1);
            Topic_List.add(i,obj);



        }
        PD.dismiss();
        if (!flag) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.fragment_container, new TopicsFragment(),"Topics").commit();//feugei to Loading....
        }
        else {
            FragmentManager fm = getSupportFragmentManager();
            ((TopicsFragment)fm.findFragmentByTag("Topics")).Prepare();
            flag=false;
        }
    }


    public void createDiscussionList(JSONArray Topics) throws JSONException {



        for (int i=0; i<Topics.length(); i++) {

            JSONObject jsonObj = Topics.getJSONObject(i);
            String x1=jsonObj.getString("message");
            String x2=jsonObj.getString("user");
            Discussion obj=new Discussion();
            obj.Set(x1,x2);
            Discussion_List.add(i,obj);



        }
        PD.dismiss();
        if (!flag) {
            Log.e(TAG,"Paei gia refresh34324234");
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment_container, new DiscussionFragment(),"Discussion").addToBackStack(null).commit();//feugei to Loading....
        }
        else {
            Log.e(TAG,"Paei gia refresh");
            FragmentManager fm = getSupportFragmentManager();
            ((DiscussionFragment)fm.findFragmentByTag("Discussion")).Prepare();
            flag=false;
        }
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                                           //listener menu
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            if (thesi==0) {
                Topic_List = new ArrayList<>();
                flag = true;
                SetUpList();
            }
            else {
                refreshDialogue();
            }



            return true;
        }
        if (id==R.id.add) {
            Dialogue();
            return true;
        }
        if (id==R.id.Map) {
            Intent go=new Intent(getApplicationContext(), MapActivity.class);
            startActivity(go);
        }
        if (id==R.id.LogOut) {
            new postlogout().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshDialogue() {
        Discussion_List = new ArrayList<>();
        flag = true;
            Log.e(TAG,"RefreshDialogue");
            SetUpDiscussion(topic);

    }

    public void Dialogue () {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                            PostTopic(userInputDialogEditText.getText().toString());

                        // ToDo get user input here
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

}


    public  String PostNew(String topic,String message,int mode) throws UnsupportedEncodingException {


        String i1 = URLEncoder.encode(topic, "UTF-8");
        String i2 = URLEncoder.encode(message, "UTF-8");
        String who=URLEncoder.encode(MyGlobal.GetUsername(),"UTF-8");
        //String i3 = URLEncoder.encode(topic, "UTF-8");
        String URL;
        HttpClient client = new DefaultHttpClient();
        if (mode==1) {
            URL="http://"+MyGlobal.ip+":8080/ServerForum/Servlet2.do?username="+who+"&topic=" + i1;
            //HttpGet request = new HttpGet("http://192.168.56.1:8080/ServerForum/Servlet2.do?username=gemakada&topic=" + i1);
        }
        else {
            URL="http://"+MyGlobal.ip+":8080/ServerForum/Servlet2.do?username="+who+"&topic=" + i1+"&message="+i2;
           // HttpGet request = new HttpGet("http://192.168.56.1:8080/ServerForum/Servlet2.do?username=gemakada&topic=" + i1+"&message="+i2);
        }
        HttpGet request = new HttpGet(URL);
        HttpResponse response;
        String responseStr = null;
        try {
            response = client.execute(request);
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                //Log.e(LOG_TAG, "key is" +MyGlobal.key );
                Log.e(TAG, "Responseeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee of PostRequest request" + sb.toString());
                if (mode!=1) {
                    Log.e(TAG, "Mpike");
                    Discussion mes=new Discussion();
                    mes.Set(message,who);
                    Discussion_List.add(mes);
                    //Discussion_List=new ArrayList<>();
                   // SetUpDiscussion(topic);
                    FragmentManager fm = getSupportFragmentManager();
                    ((DiscussionFragment) fm.findFragmentByTag("Discussion")).Prepare();
                }
                PD.hide();
                Toast.makeText(MainActivity.this,"Topic Posted", Toast.LENGTH_SHORT).show();
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // responseStr = EntityUtils.toString(response.);
            Log.e(TAG, "Response of GET request" + response.toString());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"Network error while posting Topic", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {


            Toast.makeText(MainActivity.this,"Network error while posting Topic", Toast.LENGTH_SHORT).show();
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem x;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (thesi==1) {
            x=(MenuItem)menu.findItem(R.id.add);
            x.setVisible(false);

        }
        else {
            x=(MenuItem)menu.findItem(R.id.add);
            x.setVisible(true);

        }

        return true;
    }


    public class postlogout extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
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

                response= MyGlobal.Logout();
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
            if ((result.get(0) != null)&&(!result.get(0).equals("Net Error"))) {


                    Intent go = new Intent(getApplicationContext(), login.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(go);
                    finish();



            }
            MakeText(result.get(0));

        }



    }
    public void MakeText(String txt) {
        Toast.makeText(getBaseContext(), txt, Toast.LENGTH_SHORT).show();
    }








}

