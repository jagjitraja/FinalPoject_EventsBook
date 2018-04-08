package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

import eventsbook.t00533766.eventsbook.Activites_Fragments.Fragments.AddEventFragment;
import eventsbook.t00533766.eventsbook.Activites_Fragments.Fragments.ViewEventFragment;
import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

import static eventsbook.t00533766.eventsbook.Utilities.Utils.ADD_FRAGMENT_CODE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EDIT_INTENT_ACTION;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_DATA;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_LOCATION_LATITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_LOCATION_LONGITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.FIRE_BASE_USER_KEY;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.INTENT_FRAGMENT_CODE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.USER_LOCATION_LATITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.USER_LOCATION_LONGITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.VIEW_EVENT_INTENT_KEY;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.VIEW_FRAGMENT_CODE;

public class EventDetailActivity extends FragmentActivity
        implements AddEventFragment.AddEventFragmentListener,
        ViewEventFragment.ViewEventFragmentListener {

    private final String TAG = EventDetailActivity.class.getSimpleName();
    private Event event;
    private User user;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LocationCallback locationCallback;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location location;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        fragmentManager = getSupportFragmentManager();
        geocoder = new Geocoder(getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationRequest = new LocationRequest().
                setInterval(500).setMaxWaitTime(1000).setFastestInterval(1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.d(TAG, "onLocationResult: ");
                if (locationResult != null) {
                    location = locationResult.getLastLocation();
                    Log.d(TAG, "onLocationResult: "+location.getLatitude()+"   "+location.getLongitude());
                } else {
                    Utils.showToast(getApplicationContext(), "Failed to get locaiton");
                }
            }
        };

        if (locationManager != null && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            getLocation();
        } else {
            Utils.showToast(getApplicationContext(), "Location Services are disabled " +
                    (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
        }

        Intent intent = getIntent();

        user = (User) intent.getSerializableExtra(FIRE_BASE_USER_KEY);
        if (intent.getAction().equals(Utils.ADD_INTENT_ACTION)) {
            String code = intent.getStringExtra(INTENT_FRAGMENT_CODE);
            if (code.equals(ADD_FRAGMENT_CODE)) {
                showAddEventFragment();
            } else if (code.equals(VIEW_FRAGMENT_CODE)) {
                showViewEventFragment();
                event = (Event) intent.getSerializableExtra(VIEW_EVENT_INTENT_KEY);
            }
        }
    }

    private void replaceFragment(Fragment fragment) {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void addFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    private void showViewEventFragment() {
        ViewEventFragment viewEventFragment = new ViewEventFragment();
        viewEventFragment.setEventAndLoggedInUser(event, user);
        viewEventFragment.setEventFragmentListener(this);
        addFragment(viewEventFragment);
    }

    private void showAddEventFragment() {
        AddEventFragment addEventFragment = new AddEventFragment();
        addEventFragment.setEventFragmentListener(this);
        addEventFragment.setUser(user);
        addFragment(addEventFragment);
    }



    @Override
    public void ViewFragmentEvent(Uri uri) {

    }

    @Override
    public void editEventClicked(Event event) {
        getIntent().setAction(Utils.EDIT_INTENT_ACTION);
        AddEventFragment addEventFragment = new AddEventFragment();
        Log.d(TAG, "editEventClicked: e" + event);
        addEventFragment.setEvent(event);
        addEventFragment.setUser(user);
        addEventFragment.setEventFragmentListener(this);
        replaceFragment(addEventFragment);
    }

    @Override
    public void showInMapClicked(Event event) {
        Log.d(TAG, "showInMapClicked: ---------------------------");

        Address address = null;
        try {
            address = (Address) geocoder.getFromLocationName(event.getAddressLocation(),1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (location!=null && address!=null){
            Intent intent = new Intent(this,MapsActivity.class);
            intent.putExtra(USER_LOCATION_LATITUDE,location.getLatitude());
            intent.putExtra(USER_LOCATION_LONGITUDE,location.getLongitude());
            intent.putExtra(EVENT_LOCATION_LATITUDE,address.getLatitude());
            intent.putExtra(EVENT_LOCATION_LONGITUDE,address.getLongitude());
            startActivity(intent);
        }else {
            getLocation();
        }
    }

    @Override
    public void PostEvent(Event event) {
        event.setPostedBy(user);
        setResult(Activity.RESULT_OK, new Intent(this, MainActivity.class).
                putExtra(EVENT_DATA, event));
        finish();
    }

    @Override
    public void UpdateEvent(Event event) {
        event.setPostedBy(user);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(EVENT_DATA, event);
        intent.setAction(EDIT_INTENT_ACTION);
        startActivity(intent);
    }

    @Override
    public String getLocationClicked() {
        if (location == null){
            getLocation();
        }else {
            Address address = null;
            try {
                address = (Address) geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (address != null) {
                Log.d(TAG, "getLocationClicked: " + address);
                return address.getAddressLine(0) + ", " + address.getLocality() + ", " + address.getPostalCode();
            }
        }
        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Utils.LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.showToast(this, "LOCATION GRANTED");
                getLocation();
            } else {
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                Utils.LOCATION_REQUEST_CODE);

    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }


}
