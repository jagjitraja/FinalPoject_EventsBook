package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
import static eventsbook.t00533766.eventsbook.Utilities.Utils.dateFormat;

public class EventDetailActivity extends FragmentActivity
        implements AddEventFragment.AddEventFragmentListener,
        ViewEventFragment.ViewEventFragmentListener,
        NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback {


    private static final String EVENT_SAVE_INSTANCE_KEY = "EVENT_SAVE_INSTANCE_KEY";
    private static final String USER_SAVE_INSTANCE_KEY = "USER_SAVE_INSTANCE_KEY";

    private final String TAG = EventDetailActivity.class.getSimpleName();
    private Event event;
    private User user;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    private OnCompleteListener<Location> locationCompleteListener = new OnCompleteListener<Location>() {
        @Override
        public void onComplete(@NonNull Task<Location> task) {
            if (task.isSuccessful()) {
                location = task.getResult();
                if (location != null)
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        AddEventFragment addEventFragment = (AddEventFragment) fragmentManager.findFragmentByTag(Utils.ADD_FRAGMENT);
                        if (addEventFragment != null) {
                            addEventFragment.updateLocationEditText(addressList.get(0));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


            } else {
                Utils.showToast(getApplicationContext(), "Couldnt get location");
            }
        }
    };

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null) {
                location = locationResult.getLastLocation();
                Log.d(TAG, "onLocationResult: " + location.getLatitude() + "   " + location.getLongitude());
            } else {
                Utils.showToast(getApplicationContext(), "Failed to get locaiton, Enter an addresss");
            }
        }
    };
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
        geocoder = new Geocoder(getApplicationContext(), Locale.CANADA);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        locationRequest = new LocationRequest().
                setInterval(150000).setMaxWaitTime(1000).setFastestInterval(1000);

        if (locationManager != null &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
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
                event = (Event) intent.getSerializableExtra(VIEW_EVENT_INTENT_KEY);
                showViewEventFragment();
            }
        }

        firebaseStorage = FirebaseStorage.getInstance("gs://eventify-jsb.appspot.com");
        storageReference = firebaseStorage.getReference();

    }

    private void replaceFragment(Fragment fragment, String tag) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.commit();
    }

    private void addFragment(Fragment fragment, String tag) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    private void showViewEventFragment() {
        ViewEventFragment viewEventFragment = new ViewEventFragment();
        viewEventFragment.setEventAndLoggedInUser(event, user);
        viewEventFragment.setEventFragmentListener(this);
        addFragment(viewEventFragment, Utils.VIEW_FRAGMENT);
    }

    private void showAddEventFragment() {
        AddEventFragment addEventFragment = new AddEventFragment();
        addEventFragment.setEventFragmentListener(this);
        addEventFragment.setUser(user);
        addFragment(addEventFragment, Utils.ADD_FRAGMENT);
    }

    @Override
    public void ViewFragmentEvent(Uri uri) {

    }

    @Override
    public void editEventClicked(Event event) {
        getIntent().setAction(Utils.EDIT_INTENT_ACTION);
        AddEventFragment addEventFragment = new AddEventFragment();
        addEventFragment.setEvent(event);
        addEventFragment.setUser(user);
        addEventFragment.setEventFragmentListener(this);
        replaceFragment(addEventFragment, Utils.ADD_FRAGMENT);
    }

    @Override
    public void showInMapClicked(Event event) {
        List<Address> addresses = null;
        try {

            addresses = geocoder.getFromLocationName(event.getAddressLocation(), 1);
            Log.d(TAG, addresses.toString());
            Log.d(TAG, "showInMapClicked: " + event.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MapsActivity.class);
        if (location != null && addresses != null) {
            Address address = addresses.get(0);

            intent.putExtra(USER_LOCATION_LATITUDE, location.getLatitude());
            intent.putExtra(USER_LOCATION_LONGITUDE, location.getLongitude());
            intent.putExtra(EVENT_LOCATION_LATITUDE, address.getLatitude());
            intent.putExtra(EVENT_LOCATION_LONGITUDE, address.getLongitude());
            startActivity(intent);
        } else if (location == null) {
            Utils.showToast(getApplicationContext(), "Couldnt get Event Location");
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
        if (location == null) {
            getLocation();
        } else {
            List<Address> address = null;
            try {
                address = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (address != null) {
                Log.d(TAG, "getLocationClicked: " + address);
                return address.get(0).getAddressLine(0) + ", " +
                        address.get(0).getLocality() + ", " + address.get(0).getPostalCode();
            }
        }
        return null;
    }

    @Override
    public void takePictureClicked() {

        requestCameraPermission();

    }


    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, Utils.IMAGE_CAPTURE_REQUEST_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Utils.LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } else if (requestCode == Utils.IMAGE_CAPTURE_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "takePictureClicked: ");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, Utils.IMAGE_CAPTURE_REQUEST_CODE);
                }
            } else {
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
            }
        }
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
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(locationCompleteListener);
            fusedLocationProviderClient.requestLocationUpdates
                    (locationRequest, locationCallback, null);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        return null;
    }

    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EVENT_SAVE_INSTANCE_KEY, event);
        outState.putSerializable(USER_SAVE_INSTANCE_KEY, user);
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: --------------------------");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "4444444444444444444444444444");
        if (savedInstanceState != null) {
            this.event = (Event) savedInstanceState.getSerializable(EVENT_SAVE_INSTANCE_KEY);
            this.user = (User) savedInstanceState.getSerializable(USER_SAVE_INSTANCE_KEY);

            Log.d(TAG, "onRestoreInstanceState: --------------------------");
            AddEventFragment addEventFragment = (AddEventFragment)
                    fragmentManager.findFragmentByTag(Utils.ADD_FRAGMENT);
            if (addEventFragment != null) {
                addEventFragment.setEvent(event);
                addEventFragment.setUser(user);
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        if (requestCode == Utils.IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                AddEventFragment addEventFragment = (AddEventFragment)
                        fragmentManager.findFragmentByTag(Utils.ADD_FRAGMENT);
                if (addEventFragment != null) {
                    addEventFragment.setEventBitMap(imageBitmap);
                }
                StorageReference eventImages = storageReference.child(Utils.EVENTS_IMAGES);
                StorageReference thisEventImage = eventImages.child(event.getEventID());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                byte[] bitmapBytes = byteArrayOutputStream.toByteArray();
                UploadTask uploadTask = thisEventImage.putBytes(bitmapBytes);
                Log.d(TAG, "onActivityResult: ***********************************");
                uploadTask.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Utils.showToast(getApplicationContext(), "Image upload complete");
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });

                uploadTask.addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utils.showToast(getApplicationContext(), "Image upload failed");

                    }
                });
            }
        } else {
            Utils.showToast(getApplicationContext(), "Failed to save Image");
        }
    }


}
