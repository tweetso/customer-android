package com.experto.experto.Activities.AppPages;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.experto.experto.Activities.Authentication.LogIn;
import com.experto.experto.Adapters.ProblemPriceItemsAdapter;
import com.experto.experto.AppData.Request;
import com.experto.experto.AppData.Problem;
import com.experto.experto.ListItems.ProblemPriceItem;
import com.experto.experto.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import android.Manifest;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestActivity extends FragmentActivity implements OnMapReadyCallback {

    private ProblemPriceItemsAdapter adapter;
    private List<ProblemPriceItem> problemItems;
    private GridView problemItemsList;
    private ArrayList<Problem> problemList;
    private List<Integer> problemsIndexes = new ArrayList<>();
    private TextView totalPrice;
    private TextView sendRequest;
    private View.OnClickListener clickListener;
    private AlertDialog.Builder builder;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private GoogleMap mMap;
    private ImageView mGps;
    private int totalPriceValue;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final String TAG = "Map";
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location currentLocation;
    private ArrayList<String> sku = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        getLocationPermission();
        problemItemsList = (GridView) findViewById(R.id.requests_list);
        problemItems = new ArrayList<ProblemPriceItem>();
        adapter = new ProblemPriceItemsAdapter(this, R.layout.problem_price_item, problemItems);
        totalPrice = (TextView) findViewById(R.id.total_price);
        sendRequest = (TextView) findViewById(R.id.send_request);
        builder = new AlertDialog.Builder(this);
        // icon for the device location
        mGps = (ImageView) findViewById(R.id.ic_gps);
        initializeClickListener();
        sendRequest.setOnClickListener(clickListener);
        Bundle bundle = getIntent().getExtras();
        problemList = (ArrayList<Problem>) Home.getRequestInfo().get("problems");
        problemsIndexes = (ArrayList) Home.getRequestInfo().get("problemsIndexes");
        if (problemList != null) {
            for (int i = 0; i < problemsIndexes.size(); i++) {
                Problem problem = problemList.get(problemsIndexes.get(i));
                sku.add(problem.getSku());
                String name = problem.getName() + ":";
                int price = problem.getServiceFee();
                totalPriceValue += price;
                ProblemPriceItem problemPriceItem = new ProblemPriceItem(name, price);
                adapter.add(problemPriceItem);
            }
        }
        totalPrice.setText("Total: " + totalPriceValue + " SR.");
        problemItemsList.setAdapter(adapter);
    }


    public void initializeClickListener() {
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.send_request) {
                    user = mAuth.getCurrentUser();
                    if(user !=null){
                        if(currentLocation !=null){
                            builder.setMessage("Are you sure you want to send this request ?")
                                    .setTitle("Message").setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    addRequest();
                                    dialog.cancel();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                        else
                            Log.d("New","Location is null");
                    }
                    else {
                        Boolean requestMade = true;
                        Intent intent = new Intent(RequestActivity.this,LogIn.class);
                        intent.putExtra("request",requestMade);
                        startActivity(intent);
                    }
                }
            }

        };
    }

    public void addRequest(){
        Request request = new Request();
        request.setState(0);
        request.setCid(user.getUid());
        request.setCost(totalPriceValue);
        request.setCreated(new Timestamp(new Date()));
        request.setPicked(new Timestamp(new Date()));
        request.setSku(sku);
        request.setcLocation(new GeoPoint(currentLocation.getLatitude(),currentLocation.getLongitude()));
        request.setCanceledByTechnician(false);
        progressDialog.setMessage("Sending");
        progressDialog.show();
        int timeStampSeconds = (int)request.getCreated().getSeconds();
        int random1 = (int)(Math.random()*10);
        int random2 = (int)(Math.random()*10);
        int random3 = (int)(Math.random()*10);
        String requestID = timeStampSeconds+""+random1+""+random2+""+random3;
        request.setRid(requestID);
        db.collection("request").document(requestID)
                .set(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        // to refresh the requests list
                        ExpertoLoading.getRequestsFromDB(RequestActivity.this);
                        progressDialog.dismiss();
                        Toast.makeText(RequestActivity.this,"New request has been placed, you can track it in the requests page",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RequestActivity.this,Home.class);
                        // to remove all the activities and open a new Home activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RequestActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                moveCamera(new LatLng(latLng.latitude,latLng.longitude),
                        DEFAULT_ZOOM);
            }
        });
        if(currentLocation !=null){
            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),
                    DEFAULT_ZOOM);
        }
        else {
            if (mLocationPermissionsGranted) {
                getDeviceLocation();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        }
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(RequestActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(RequestActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        currentLocation.setLatitude(latLng.latitude);
        currentLocation.setLongitude(latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if(marker !=null){
            marker.remove();
        }
        MarkerOptions options= new MarkerOptions().position(latLng);
        marker= mMap.addMarker(options);
    }
}
