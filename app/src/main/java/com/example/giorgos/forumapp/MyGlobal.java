package com.example.giorgos.forumapp;

import android.app.Activity;
import android.app.Application;
import android.location.Location;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by makan on 11/2/2017.
 */

public class MyGlobal extends Application {
    private static String Username;
    private static String gpslatitude;
    private static String gpslongitude;
    public static String ip = "192.168.2.9";
    public static String to;

    public static String key;
    public static Activity main;
    public static int dialoguecheck = 0;
    public static Location location;
    public static long timerefresh1;
    public static long timelock1;
    public static long timegoogle1;

    //private static final String LOG_TAG = MainActivity.class.getSimpleName();


    public static void SetUsername(String x) {
        Username = x;
    }


    public static String GetUsername() {
        return Username;
    }


    public static String Logout() throws UnsupportedEncodingException {


        String i1 = URLEncoder.encode(Username, "UTF-8");

        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpClient client = new DefaultHttpClient(httpParams);

        HttpGet request = new HttpGet("http://" + ip + ":8080/ServerForum/Logout.do?user=" + Username);
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


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return ("Success");

            // responseStr = EntityUtils.toString(response.);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ("NetError");

        }


        return null;

    }

    public static String Post(String to,String msg) throws UnsupportedEncodingException {


        String i1 = URLEncoder.encode(to, "UTF-8");
        String i2 = URLEncoder.encode(msg, "UTF-8");
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpClient client = new DefaultHttpClient(httpParams);

        HttpGet request = new HttpGet("http://" + ip + ":8080/ServerForum/Post?to=" + i1+"&message="+i2);
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


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return ("Success");

            // responseStr = EntityUtils.toString(response.);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ("NetError");

        }


        return null;

    }

}


