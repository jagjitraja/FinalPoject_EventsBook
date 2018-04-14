package eventsbook.t00533766.eventsbook.Activities_Fragments;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import eventsbook.t00533766.eventsbook.R;

import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_LOCATION_LATITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_LOCATION_LONGITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.USER_LOCATION_LATITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.USER_LOCATION_LONGITUDE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
        Intent intent = getIntent();

        Log.d(TAG, "onMapReady: ");
        LatLng eventLocation = null;
        LatLng userLocation = null;
        if (intent!=null){

            double userLatitude = intent.getDoubleExtra(USER_LOCATION_LATITUDE,0);
            double userLongitude = intent.getDoubleExtra(USER_LOCATION_LONGITUDE,0);
            double eventLatitude = intent.getDoubleExtra(EVENT_LOCATION_LATITUDE,0);
            double eventLongitude = intent.getDoubleExtra(EVENT_LOCATION_LONGITUDE,0);

            eventLocation = new LatLng(eventLatitude,eventLongitude);
            userLocation = new LatLng(userLatitude,userLongitude);


            Polyline polyline = mMap.addPolyline(new PolylineOptions().add(userLocation,eventLocation));

            mMap.addMarker(new MarkerOptions().position(eventLocation).title("Event"));
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Me"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(eventLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

            mMap.animateCamera( CameraUpdateFactory.zoomTo( 5 ) );

        }
    }
}
