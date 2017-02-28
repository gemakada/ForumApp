package com.example.giorgos.forumapp;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;


/**
 * Created by Giorgos on 20/4/2016.
 */
public class PedestrianMapFragment extends android.support.v4.app.Fragment implements LocationListener {
    private static View view;
    private static View mOriginalContentView;
    private GoogleMap mMap;
    MapView mapview;
    String display;
    public Location location1;
    Double gpslatitude;
    Double gpslongitude;
    Double latnew;
    Double lonnew;
    private boolean flag;
    FloatingActionButton butpost;
    FloatingActionButton butrate;
    FloatingActionButton butrefresh;
    private static final String LOG_TAG = MapActivity.class.getSimpleName();
    private JSONArray array;
    private List<Marker> Markers = new ArrayList<>();
    private List<Marker> NewMarker;
    private Location current;
    private LatLng selection;
    private String Markername;
    //public DBHelper db;
    private JSONObject ret;
    private String postpos = "";
    private Location last=null;
    public Marker thread_marker;
    public Marker mrk;
    public double lat1;
    public double lon2;
    public boolean placeflag;
    public boolean locationsetted=false;
    public LocationManager locationManager;
    public GoogleApiClient mGoogleApiClient;
    public LocationListener listener;

    public boolean mTouched;
    public boolean touch=true;
    private String friends;
    public List<Friend> friends_list;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Xartis kai prosvasi se upiresies topothesias

       // db = new DBHelper(getActivity());
        // File dbFile = getDatabasePath("MyDBName.db");
        // Log.e(LOG_TAG, dbFile.toString());

        setflag(true);
        view = inflater.inflate(R.layout.map_main2,null);
        mapview = (MapView) view.findViewById(R.id.Location_map);



        mapview.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT>=23) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mapview.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMap = mapview.getMap();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            locationManager.requestLocationUpdates(bestProvider, 0, 0, this);

            //mMap.setMyLocationEnabled(true);
            //current = mMap.getMyLocation();
        }



        //locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
       // Criteria criteria = new Criteria();
        //String bestProvider = locationManager.getBestProvider(criteria, true);
        //location1 = locationManager.getLastKnownLocation(bestProvider);

       // if (location1 != null) {
       //     gpslatitude = location1.getLatitude();
       //    gpslongitude = location1.getLongitude();
      //  }
      //  if (location1 != null) {
            //onLocationChanged(location1);
        //}
        listener=this;





        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // FloatingActionButton butlock = (FloatingActionButton)view.findViewById(R.id.lock);
                // butlock.setVisibility(View.INVISIBLE);
            }
        });

        new calc_pos().execute();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                    mrk = marker;
                    mrk.showInfoWindow();
                    return true;



            }

        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String x=marker.getTitle();
                for (int i=0; i<friends_list.size(); i++) {
                    if (friends_list.get(i).getName().equals(x)) {
                        MyGlobal.to=friends_list.get(i).getKey();
                    }
                }
                Intent go=new Intent(getActivity().getBaseContext(), ChatActivity.class);
                startActivity(go);
            }
        });



        mOriginalContentView = super.onCreateView(inflater, container,
                 savedInstanceState);
        TouchableWrapper mTouchView = new TouchableWrapper(getActivity());
        mTouchView.addView(view);



        return mTouchView;
    }





    private void setUpMap() {
        //Katevasma Markers
        mMap.clear();
        Marker marker = mMap.addMarker(new MarkerOptions()

                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location))
                .position(new LatLng(gpslatitude, gpslongitude)));

        new Thread(new Runnable() {
            public void run() {
                // Toast.makeText(getActivity(),"check4", Toast.LENGTH_SHORT).show();


                    //Retrive();
                    AddMarkers();


            }
        }).start();
    }






    public void Retrive(int op) {
                                          //Pername san parametro stin createMarkersFromJson() to string pou pirame apo to server
                // Toast.makeText(MainActivity.this, "check15", Toast.LENGTH_SHORT).show();

                    //    Toast.makeText(this,"check5", Toast.LENGTH_SHORT).show();
                    //   Toast.makeText(MainActivity.this, "You selected :", Toast.LENGTH_SHORT).show();
                    AddMarkers();
                    //   Toast.makeText(MainActivity.this, "check15", Toast.LENGTH_SHORT).show();

            }

        //mMap.clear();








    public void AddMarkers() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {


               try {
                    //    Toast.makeText(this,"check5", Toast.LENGTH_SHORT).show();
                    //   Toast.makeText(MainActivity.this, "You selected :", Toast.LENGTH_SHORT).show();
                  //  Log.e(LOG_TAG,friends);
                 //   if (friends!=null) {
                   Log.e(LOG_TAG,friends);
                       createMarkersFromJson(friends);
               //}
                    //   Toast.makeText(MainActivity.this, "check15", Toast.LENGTH_SHORT).show();
               } catch (JSONException e) {
                  //  Log.e(LOG_TAG, "Error processing JSON", e);
               }





            }
        });
    }
    void createMarkersFromJson(String json) throws JSONException {                                  //epeksergasia JSOn listas,kai dimiourgia Marker Array
        //Toast.makeText(MainActivity.this,"katevikan", Toast.LENGTH_SHORT).show();
        friends_list = new ArrayList<Friend>();
        JSONObject json1 = new JSONObject(json);
        // System.out.print(json);
        array = json1.getJSONArray("topics");
        AddMarkers(array);

        // Toast.makeText(MainActivity.this, "You selected :", Toast.LENGTH_SHORT).show();

    }

    public void AddMarkers(JSONArray Places) throws JSONException {                                 //Topothetisi markers panw ston xarti
        //Toast.makeText(getActivity(), "Connection Success", Toast.LENGTH_SHORT).show();
        Location tmp = new Location("");

        float distance;

        for (int i = 0; i < Places.length(); i++) {


            JSONObject jsonObj = Places.getJSONObject(i);
            double x = Double.parseDouble(jsonObj.getString("latitude"));
            double y = Double.parseDouble(jsonObj.getString("longitude"));
            friends_list.add(new Friend(jsonObj.getString("username"),jsonObj.getString("key")));
            Log.e(LOG_TAG,jsonObj.getString("username"));


            tmp.setLatitude(x);
            tmp.setLongitude(y);
         //   if (current!=null) {
                distance = tmp.distanceTo(current);

              //  if (distance < 20) {

                    if (!jsonObj.get("username").equals(MyGlobal.GetUsername())) {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .title(jsonObj.getString("username"))

                                .position(new LatLng(x, y)));

                    }
                    //((MainActivity)getActivity()).Markers_Main=Markers;
                //}
           // }

        }

    }



   // public class




    public class calc_pos extends AsyncTask<ArrayList<Marker>, Void, ArrayList<String>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Calculating Position and Venues...");
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
        }

        protected ArrayList<String> doInBackground(ArrayList<Marker>... passing) {
            ArrayList<String> result = new ArrayList<String>();


            String ret="";


            //Toast.makeText(getActivity(),x.toString(), Toast.LENGTH_LONG).show();
            //Some calculations...
           while (!locationsetted) {

           }

                result.add("Location Set");


            return result; //return result
        }

        protected void onPostExecute(ArrayList<String> result) {

            Toast.makeText(getActivity(),result.get(0), Toast.LENGTH_LONG).show();
            //Retrive(0);
            dialog.dismiss();

            Log.e(LOG_TAG, "Location setted " + result.get(0));

        }
    }


    public class calc_new_venues extends AsyncTask<ArrayList<Marker>, Void, ArrayList<String>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Retrieving Friends...");
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
        }

        protected ArrayList<String> doInBackground(ArrayList<Marker>... passing) {
            ArrayList<String> result = new ArrayList<String>();

            String query;
            friends=getNearestFriends(gpslatitude,gpslongitude);

            result.add("Friends Retrieved");
            Log.e(LOG_TAG,friends);

            return result; //return result
        }

        protected void onPostExecute(ArrayList<String> result) {

            Toast.makeText(getActivity(),result.get(0), Toast.LENGTH_LONG).show();
            Retrive(1);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(gpslatitude, gpslongitude))
                    .zoom(18)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            dialog.dismiss();
            Log.e(LOG_TAG, "Result "+ friends);
            Log.e(LOG_TAG, "Result " + result.get(0));

        }
    }

    public void RestoreState() {
        mrk.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_venue_pin_selected));
        mMap.getUiSettings().setAllGesturesEnabled(true);
        touch=true;

    }


    public void UpdateInfo() {





    }




    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        String bestProvider = locationManager.getBestProvider(criteria, true);
                        locationManager.requestLocationUpdates(bestProvider, 0, 0, this);

                        //mMap.setMyLocationEnabled(true);
                        //current = mMap.getMyLocation();
                    }



                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



















    public void onLocationChanged(Location location) {                                              //listener topothesias
        gpslatitude = location.getLatitude();
        gpslongitude = location.getLongitude();
        int i;


        // LatLng latLng = new LatLng(gpslatitude, gpslongitude);
        //mMap.addMarker(new MarkerOptions().position(latLng));
         //CameraUpdate camera=CameraUpdateFactory.newLatLngZoom(,10);

        if(getActivity()!=null){
            if (flag) {

                new calc_dist().execute();
                setflag(false);
            }

            //Locale EL = new Locale("us");
           // Geocoder x=new Geocoder(getActivity(),Locale.getDefault());
           // display="";










        current=location;

        locationsetted=true;


       // location1=location;
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
               // locationManager.removeUpdates(listener);
            }
          //  Log.e(LOG_TAG, "Mpike");


    }

    }

    public void Prepare() {
        this.getFragmentManager().beginTransaction().detach(this).attach(this).commit();


    }
    public void FocusMarker(Marker mark) {
        //Emfanisi shmeiou pou ginetai focus


        mark.showInfoWindow();
       // CameraPosition cameraPosition = new CameraPosition.Builder()
        //        .target(mark.getPosition())
         //       .zoom(15)
         //       .build();
      //  mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));




    };


    public class TouchableWrapper extends FrameLayout {
        public TouchableWrapper(Context context) {
            super(context);
        }
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouched = true;
                    break;
                case MotionEvent.ACTION_UP:
                      mTouched = false;
                    break;
            }

            return super.dispatchTouchEvent(ev);

        }

    }


    public void res() {
        new calc_new_venues().execute();

    }


    public class calc_dist extends AsyncTask<ArrayList<Marker>, Void, ArrayList<String>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Calculating Address...");
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        protected ArrayList<String> doInBackground(ArrayList<Marker>... passing) {
            ArrayList<String> result = new ArrayList<String>();


            String ret="";


            //Toast.makeText(getActivity(),x.toString(), Toast.LENGTH_LONG).show();
            //Some calculations...
            ret=getLocationInfo(gpslatitude, gpslongitude);
            if (!ret.equals("")) {
                result.add("Position Acquired "+display);
                // async.setTitle(display);
            }
            else {
                result.add("Network Error");
            }

            return result; //return result
        }

        protected void onPostExecute(ArrayList<String> result) {
            Marker marker = mMap.addMarker(new MarkerOptions()

                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location))
                    .position(new LatLng(gpslatitude, gpslongitude)));
            dialog.dismiss();
            ((MapActivity) getActivity()).setText(display);
            res();
            //Log.e(LOG_TAG, "Response of Google in millis " + Long.toString(curgoogletime));

        }
    }

    public void refresh() {
        mMap.clear();
        new calc_dist().execute();



    }

    public  String getLocationInfo(final double lat,final double lng)
    {

        //((Pedestrian) getActivity()).PD.show();

        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpGet request = new HttpGet("http://maps.google.com/maps/api/geocode/json?KEY=AIzaSyAacswioSDsSHkSI7T0PYc87GO6A4lLTGg&latlng=" + lat + "," + lng + "&sensor=true");
        HttpResponse response;
        String responseStr = null;
        StringBuilder sb = new StringBuilder();
        try {
            response = client.execute(request);

            try {
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                //Log.e(LOG_TAG, "key is" +MyGlobal.key );
                Log.e(LOG_TAG, "Responseeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee of PostRequest request" + sb.toString());
                //  return sb.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // responseStr = EntityUtils.toString(response.);
            Log.e(LOG_TAG, "Response of GET request" + response.toString());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            //((Pedestrian) getActivity()).PD.dismiss();
            // ((Pedestrian) getActivity()).alertDialog.show();
            Log.e(LOG_TAG, "Exception net");
            return"";


            // TODO Auto-generated catch block

        }


        JSONObject jsonObject = new JSONObject();
        Log.e(LOG_TAG, sb.toString());
        try {
            jsonObject = new JSONObject(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ret = jsonObject;
        Log.e(LOG_TAG, "address striiiiiiiiing");

        JSONObject geo;
        JSONObject geo1;
        JSONObject geo2;
        JSONObject geo3;
        String aprox;

        if (ret != null) {

            try {
                geo = ret.getJSONArray("results").getJSONObject(0);
                geo1 = geo.getJSONArray("address_components").getJSONObject(1);
                geo2 = geo.getJSONArray("address_components").getJSONObject(0);
                aprox = ret.getJSONArray("results").getJSONObject(2).getJSONObject("geometry").getString("location_type");

                String mCuurentAddress = geo1.getString("long_name") + " " + geo2.getString("long_name");
                //
                display = mCuurentAddress;
                Log.e(LOG_TAG,"Ta pireeee" +mCuurentAddress);

                return "Good";
                //arg.setTitle(display);


            } catch (JSONException e1) {
                Log.e(LOG_TAG, "Exception string");
                e1.printStackTrace();

            }
        }

        return "";



    }


    public  String getNearestFriends(final double lat,final double lng) {
        String x = Double.toString(lat);
        String y = Double.toString(lng);
        String i1 = null;
        try {
            i1 = URLEncoder.encode(x, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String i2 = null;
        try {
            i2 = URLEncoder.encode(y, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String i3 = null;
        try {
            i3 = URLEncoder.encode(MyGlobal.GetUsername(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String i4=null;
        try {
            i4 = URLEncoder.encode(MyGlobal.key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        String i3=URLEncoder.encode(MyGlobal.key, "UTF-8");


        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpGet request = new HttpGet("http://"+MyGlobal.ip+":8080/ServerForum/UserPos.do?user="+i3+"&lat=" + i1 + "&lon=" + i2 + "&key="+i4 );
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
                Log.e(LOG_TAG, "Json" + sb.toString());





                //((Pedestrian) getActivity()).MakeText("Refresh Complete");
              //  Toast.makeText(getActivity(),"Refresh Complete",Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG,sb.toString());
                //friends=sb.toString();
                return sb.toString();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // responseStr = EntityUtils.toString(response.);
            Log.e(LOG_TAG, "Response of GET request" + response.toString());

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

            return"";


            // TODO Auto-generated catch block

        }





        return"";
    }



    public void setflag(boolean x) {
        if (x) {
            flag=true;
        }
        else {
            flag=false;
        }


    }
























    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

}
