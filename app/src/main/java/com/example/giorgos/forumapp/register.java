package com.example.giorgos.forumapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
 * Created by makan on 13/2/2017.
 */

public class register extends AppCompatActivity {
    private View inputEmail,inputPassword;
    private Button btnLogin,btnLinkToRegister;
    private String response;
    private static final String TAG = login.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()

                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }

        setContentView(R.layout.register_res);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inputEmail = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // DBHelper db=new DBHelper(this);
        // File dbFile = getDatabasePath("MyDBName.db");
        // Log.e(TAG, dbFile.toString());
        //  if (db.insertContact("elephant1","good1","info","info","info"))Log.e(TAG, "Ta vale");

        //  Cursor tmp;
        //  tmp=db.getData("elephant1");
        // tmp.moveToFirst();
        // while (tmp.isAfterLast()==false) {
        //   if (tmp != null) {
        // tmp.moveToFirst();
        //      Log.e(TAG, tmp.getString(1));
        //      tmp.moveToNext();

        //   }
        // }


        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = ((EditText) inputEmail).getText().toString().trim();
                String password = ((EditText) inputPassword).getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    //  checkLogin(email, password);
                    Intent go = new Intent(getApplicationContext(), login.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    try {
                        response = Login(email, password);


                    } catch (UnsupportedEncodingException e1) {

                    }
                    if (response!=null) {
                        if (!(response.equals("Registration Success"))) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration Error", Toast.LENGTH_LONG)
                                    .show();


                        } else {

                            Toast.makeText(getApplicationContext(),
                                    "Registration success", Toast.LENGTH_LONG)
                                    .show();
                            go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(go);
                            finish();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Communication failed", Toast.LENGTH_LONG)
                                .show();
                    }


                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }

            }

        });




    }


    public static String Login(String Username,String Password) throws UnsupportedEncodingException {

        String i1 = URLEncoder.encode(Username, "UTF-8");
        String i2 = URLEncoder.encode(Password, "UTF-8");
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpGet request = new HttpGet("http://"+MyGlobal.ip+":8080/ServerForum/Register.do?user=" + i1 + "&pass=" + i2);
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
                Log.e(TAG, "Responseeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee of GET request" + sb.toString());
                MyGlobal.SetUsername(sb.toString());
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


        } catch (IOException e) {

            Log.e(TAG, "Error Communication");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        return null;



    }






}
