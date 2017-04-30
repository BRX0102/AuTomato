package hashmaps.automatoandroid;

import android.*;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

//hashmaps-allday
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , LocationListener
        , View.OnClickListener
        , GoogleMap.OnMarkerClickListener{


    //SocketIO
    ///////////////////////////////////////////////////////
    private Socket mSocket;
    ///////////////////////////////////////////////////////
    int spinnerPosition;
    String selectedResNumString = "";
    int selectedResNum = 0;
    String defaultSpinnerHeader = "Select severity";

    final String serverURL = "https://shielded-brushlands-57140.herokuapp.com";

    private Button markButton, deleteButton;

    private Spinner roadSpinner;

    private static Location mostRecentLocation = null;

    private final String TAG = "MapsActivity";

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private GoogleMap mMap;

    //////////////////////////////////////////////////////////
    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    Location recentMark = null;

    @Override
    protected void onResume() {
        super.onResume();
           initialValues();
    }

    ////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //CHECK FOR PERMISSIONS
        ///////////////////////////////////
        //check for internet permission
        /////////////////////////////////////////////
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET);
        Log.d(TAG, "INTERNET permission " + permissionCheck);

        int permissionCheck3 = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        Log.d(TAG, "FINE location permission " + permissionCheck3);

        // Assume thisActivity is the current activity
        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.d(TAG, "COARSE location permission " + permissionCheck2);

        ///////////////////////////////////////////

        //START MAP
        ///////////////////////////////////
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //////////////////////////////////

        //NEW CODE FOR LOCATION
        ////////////////////////////////////
        buildGoogleApiClient();
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//set to high accuracy for most accurate latLon
        // .setInterval(10 * 1000);        // 10 seconds, in milliseconds
        //.setFastestInterval(1 * 1000); // 1 second, in milliseconds
        ////////////////////////////////////




        //checks if the user has enabled the right permission
        ////////////////////////////////////////////////////
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkPermissions();
        } else {//if permissions are enabled
            //GoogleApiClient();
            mGoogleApiClient.connect();
        }

        markButton = (Button) findViewById(R.id.markButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        markButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        roadSpinner = (Spinner) findViewById(R.id.roadSpinner);

        //START SPINNER CODE
        ///////////////////////////////////
        //START LISTVIEW CODE
        ///////////////////////////////////////////
        ArrayList<String> mobileArray = new ArrayList<String>();
        //list of data to display
        final List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(defaultSpinnerHeader);
        spinnerArray.add("Everyone");
        spinnerArray.add("4x4 only");
        spinnerArray.add("Tractor Only");
        spinnerArray.add("No One");

        //START SPINNER CODE
        ///////////////////////////////////
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

// Spinner spinYear = (Spinner)findViewById(R.id.spin);
        roadSpinner.setAdapter(spinnerArrayAdapter);
        roadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                //int spinnerPosition = bookSpinner.getSelectedItemPosition();
                spinnerPosition = position;
                //ToastIt(""+position);

                //gets the chosen item
                //skip 0 because that is not a book
                for(int i=1; i < spinnerArray.size(); i++){
                    if(spinnerPosition == i){
                        selectedResNumString = spinnerArray.get(i);
                        selectedResNum = i;
                        //selectedResNum = Integer.parseInt(selectedResNumString);
                        Log.d(TAG, "selected item: "+selectedResNumString);
                        toastIt(selectedResNumString);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        //END SPINNER CODE
        ///////////////////////////////////

        //SOCKETON
        // Receiving an object
//        try {
//            mSocket = IO.socket(serverURL);
//            mSocket.on("coordinates", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    JSONObject obj = (JSONObject)args[0];
//                        Log.d(TAG,"a response");
//                }
//            });
//            mSocket.disconnect();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            Log.d(TAG,"fail");
//        }
    }

    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void checkPermissions() {
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        Log.d(TAG, "FINE location permission " + permissionCheck);

        // Assume thisActivity is the current activity
        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.d(TAG, "COARSE location permission " + permissionCheck2);

        //NEW
        //ASKS FOR THE PERMISSIONS
        //////////////////////////////////
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        ////////////////////////////////////////////
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Log.d(TAG, "permission granted");
                    //goes to the search portion
                    //onSearch();
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mGoogleApiClient.connect();
                    mMap.setMyLocationEnabled(true);
                    //mapLocation();

                }else {
                    //on permission denied
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        /*ADD A MARKER
        LatLng csumb = new LatLng(36.652527, -121.797277);

        CameraUpdate center = CameraUpdateFactory.newLatLng(csumb);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);

        //marker
        //mMap.moveCamera(center);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.addMarker(new MarkerOptions().position(csumb).title(":)"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(csumb, 5));

        */
        //when the map is ready, it checks if the lcoation permissions are set
        //then turns on location
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            centerMapOnMyLocation();
            //Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
            //       mGoogleApiClient);
        }
    }

    private void ToastIt(String aString) {
        Toast.makeText(getApplicationContext(),
                aString, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        android.location.Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (location == null) {
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "location is null");

        } else {
            //If everything went fine lets get latitude and longitude
            //enables the fusedlocation
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            //ToastIt(""+currentLatitude);
        }

    }

    private void centerMapOnMyLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        LatLng myLocation = new LatLng(currentLatitude,
                currentLongitude);

        CameraUpdate center = CameraUpdateFactory.newLatLng(myLocation);
        //CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);

        //marker
        //mMap.moveCamera(center);
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14));
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
                Log.d(TAG, "Location services canceled original pendingintern");
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.d(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        centerMapOnMyLocation();
        mostRecentLocation = location;

        //ToastIt("Location Changed "+currentLatitude + " WORKS " + currentLongitude + "");
        Log.d(TAG, "onLocationChanged "+currentLatitude + " , " + currentLongitude + "");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.markButton:
                //AutomatoAPI api = new AutomatoAPI();
                if(selectedResNum > 0) {
                    //api.sendBlockade(mostRecentLocation, selectedResNum-1);
                    //api.initialValues();
                    //initialValues();
                    sendBlockade(mostRecentLocation, selectedResNum-1);
                }
                break;

            case R.id.deleteButton:
                if(recentMark !=null){
                    //toastIt(recentMark.toString());
                    deleteMarker(recentMark);
                }
                else{
                    toastIt("please select a marker");
                }
                break;
        }
    }

    public void toastIt(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Initial values for the map
     */
    public void initialValues(){
        try {
            //clears the map
            if(mMap != null)
                mMap.clear();
            mSocket = IO.socket(serverURL);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    mSocket.emit("readData");
                }

            }).on("coordinates", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    MapsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Your code to run in GUI thread here

                    Log.d(TAG, "got a response");
                    JSONObject data = (JSONObject) args[0];
                    Iterator<String> iter = data.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        Log.d(TAG, key);
                        try {
                            JSONArray value = (JSONArray) data.get(key);
                            for (int i = 0; i < value.length(); i++) {
                                JSONObject objects = value.getJSONObject(i);
                                Iterator key_2 = objects.keys();

                                final Location location = new Location("automato");
                                int severity = 3;

                                while (key_2.hasNext()) {
                                    String k = key_2.next().toString();
                                    Log.d(TAG,"Key : " + k + ", value : "
                                            + objects.getString(k));
                                    //latitude
                                    if(k.equals("latitude")) {
                                        String temp = objects.getString(k);
                                        Double tempD = Double.parseDouble(temp);
                                        location.setLatitude(tempD);
                                    }
                                    if(k.equals("longitude")) {
                                        String temp = objects.getString(k);
                                        Double tempD = Double.parseDouble(temp);
                                        location.setLongitude(tempD);
                                    }
                                    if(k.equals("status")) {
                                        severity = objects.getInt(k);
                                    }
                                }
                                // System.out.println(objects.toString());
                                Log.d(TAG, "-----------");

                                setNewMarker(location, severity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //mSocket.disconnect();
                        }//public void run() {
                    });
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d(TAG, "disconnected");
                }

            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends latitude, longitude, and what type of blocked is it
     */
    public void sendBlockade(final Location aLocation, final int num){
        try {
            mSocket = IO.socket(serverURL);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    // Sending an object
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("latitude", aLocation.getLatitude());
                        obj.put("longitude", aLocation.getLongitude());
                        obj.put("blockType", num);
                        mSocket.emit("markEndPoint", obj);
                        //getInitData();
                        //mSocket.disconnect();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //SET NEW POINT ON MAP WITH THIS
                }

            }).on("newPoint", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {

                    MapsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Your code to run in GUI thread here
                    Log.d(TAG, "got a response");
                    JSONObject data = (JSONObject) args[0];
                    Double newLatitude;
                    Double newLongitude;
                    int newBlockType;
                    try {
                        newLatitude= data.getDouble("latitude");
                        newLongitude= data.getDouble("longitude");
                        newBlockType = data.getInt("blockType");


                        Log.d(TAG,""+newLatitude);
                        Log.d(TAG,""+newLongitude);
                        Log.d(TAG,""+newBlockType);

                        final Location location = new Location("automato");
                        int severity = 3;

                        location.setLatitude(newLatitude);
                        location.setLongitude(newLongitude);

                        setNewMarker(location, severity);

                    } catch (JSONException e) {
                        Log.d(TAG,"ERROR");
                    }
                    mSocket.disconnect();
                        }//public void run() {
                    });
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d(TAG, "disconnected");
                }

            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends latitude, longitude, and what type of blocked is it
     */
    public void deleteMarker(final Location aLocation){
        try {
            mSocket = IO.socket(serverURL);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    // Sending an object
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("latitude", aLocation.getLatitude());
                        obj.put("longitude", aLocation.getLongitude());
                        mSocket.emit("deleteLocation", obj);
                        //getInitData();
                        //mSocket.disconnect();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //SET NEW POINT ON MAP WITH THIS
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d(TAG, "disconnected");
                }

            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void setNewMarker(Location location, int severity){
        mMap.setOnMarkerClickListener(this);

        BitmapDescriptor bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
        String title = "";
        String snippet = "";
        if(severity == 0){
            title = "Everyone can pass by";
            snippet = "The road is good!";
            bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        }
        else if(severity == 1){
            title = "4x4 only";
            bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
        }
        else if(severity == 2){
            title = "Tractor Only";
            bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        }
        else if(severity == 3){
            title = "No one can cross";
            snippet = "Take another route";
            bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("severity: "+title)
                .snippet(snippet)
                .icon(bitmap)
        );
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        recentMark = new Location("automato");
        recentMark.setLatitude(marker.getPosition().latitude);
        recentMark.setLongitude(marker.getPosition().longitude);
        return false;
    }
}
