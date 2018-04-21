package eventsbook.t00533766.eventsbook.Activities_Fragments.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import eventsbook.t00533766.eventsbook.Activities_Fragments.Fragments.AddEventFragment;
import eventsbook.t00533766.eventsbook.Activities_Fragments.Fragments.ViewEventFragment;
import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.LoggedInUserSingleton;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

import static eventsbook.t00533766.eventsbook.Utilities.Utils.DELETE_INTENT_ACTION;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EDIT_INTENT_ACTION;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_DATA;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_LOCATION_LATITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_LOCATION_LONGITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.USER_LOCATION_LATITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.USER_LOCATION_LONGITUDE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.hasInternet;


/*
* This activity uses two fragments
* @EVENTADDFRAGMENT
* @EVENTVIEWFRAGMENT
*
* it switches them based on how the user launches the activity or selects to edit the event from view event fragment
*
* ALSO PROVIDES NFC functionality when user is on this screen
* */
public class EventDetailActivity extends FragmentActivity
        implements AddEventFragment.AddEventFragmentListener,
        ViewEventFragment.ViewEventFragmentListener,
        NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback {

    private static final String EVENT_SAVE_INSTANCE_KEY = "EVENT_SAVE_INSTANCE_KEY";
    private static final int NDEF_SENT = 0;

    private final String TAG = EventDetailActivity.class.getSimpleName();
    private Event event;
    private User user;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private Gson gson = new Gson();

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
    private Geocoder geocoder;
    private NfcAdapter nfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        fragmentManager = getSupportFragmentManager();
        geocoder = new Geocoder(getApplicationContext(), Locale.CANADA);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        user = LoggedInUserSingleton.getLoggedInUser();
        firebaseStorage = FirebaseStorage.getInstance(Utils.EVENT_IMAGES_STORAGE);
        storageReference = firebaseStorage.getReference();

        locationRequest = new LocationRequest().
                setInterval(1500000).setMaxWaitTime(100000).setFastestInterval(10000);

        if (locationManager != null &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            getLocation();
        } else {
            Utils.showToast(getApplicationContext(), "Location Services are disabled " +
                    (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
        }


        if (Objects.equals(getIntent().getAction(), NfcAdapter.ACTION_NDEF_DISCOVERED)){
            processIntent();
            return;
        }

        if (savedInstanceState != null) {
            this.event = (Event) savedInstanceState.getSerializable(EVENT_SAVE_INSTANCE_KEY);
            AddEventFragment addEventFragment = (AddEventFragment) fragmentManager.findFragmentByTag(Utils.ADD_FRAGMENT);
            if (addEventFragment != null) {
                addEventFragment.setEvent(event);
            }
        }


        Intent intent = getIntent();
        if (intent.getAction().equals(Utils.ADD_INTENT_ACTION)) {
                showAddEventFragment();
        }else {
            event = (Event) intent.getSerializableExtra(Utils.EVENT_DATA);
            showViewEventFragment();
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter==null){
            Utils.showToast(this,"NFC not supported");
        }else {
            nfcAdapter.setNdefPushMessageCallback(this,this);
            nfcAdapter.setOnNdefPushCompleteCallback(this,this);
        }

    }

    private void replaceFragment(Fragment fragment, String tag) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentManager.findFragmentByTag(Utils.VIEW_FRAGMENT));
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.commit();
        Log.d(TAG, "editEventClicked: "+fragmentManager.getFragments().toString());
    }

    private void addFragment(Fragment fragment, String tag) {
        if (!fragmentManager.getFragments().contains(fragment)) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, fragment, tag);
            fragmentTransaction.commit();
        }
    }

    private void showViewEventFragment() {
        ViewEventFragment viewEventFragment = (ViewEventFragment) fragmentManager.findFragmentByTag(Utils.VIEW_FRAGMENT);
        if (viewEventFragment == null){
            viewEventFragment = new ViewEventFragment();
            viewEventFragment.setEventFragmentListener(this);
        }
        viewEventFragment.setEvent(event);
        addFragment(viewEventFragment, Utils.VIEW_FRAGMENT);
    }

    private void showAddEventFragment() {
        AddEventFragment addEventFragment = (AddEventFragment) fragmentManager.findFragmentByTag(Utils.ADD_FRAGMENT);
        if (addEventFragment == null){
            addEventFragment = new AddEventFragment();
            addEventFragment.setEventFragmentListener(this);
        }
        addFragment(addEventFragment, Utils.ADD_FRAGMENT);
    }



    //CALL BACKS FROM THE FRAGMENTS FROM VIEW EVENT OR ADD EVENT
    @Override
    public void editEventClicked(Event event) {
        getIntent().setAction(Utils.EDIT_INTENT_ACTION);
        AddEventFragment addEventFragment = (AddEventFragment) fragmentManager.findFragmentByTag(Utils.ADD_FRAGMENT);
        if (addEventFragment == null){
            addEventFragment = new AddEventFragment();
            addEventFragment.setEventFragmentListener(this);
        }
        addEventFragment.setEvent(event);
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
        if (location != null) {
            intent.putExtra(USER_LOCATION_LATITUDE, location.getLatitude());
            intent.putExtra(USER_LOCATION_LONGITUDE, location.getLongitude());
        }else {
            Utils.showToast(getApplicationContext(),"Couldnt get user location");
        }
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            intent.putExtra(EVENT_LOCATION_LATITUDE, address.getLatitude());
            intent.putExtra(EVENT_LOCATION_LONGITUDE, address.getLongitude());
        }else{
            Utils.showToast(getApplicationContext(), "Couldnt get Event Location");
        }
        startActivity(intent);

    }

    @Override
    public void shareEventClicked(Event event) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TITLE,event.getEventName());
        intent.putExtra(Intent.EXTRA_TEXT,event.toString());
        intent.setType("text/plain");
        startActivity(intent);
    }

    @Override
    public void deleteEventClicked(Event event) {
        Log.d(TAG, "deleteEventClicked: "+event);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(EVENT_DATA, event);
        intent.setAction(DELETE_INTENT_ACTION);
        startActivity(intent);
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


    //GET LOCATION AND CAMERA PERMISSIONS
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
        String ndefEvent =  gson.toJson(event);

        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT,
                new byte[0],
                ndefEvent.getBytes());

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }

    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {

       handler.obtainMessage(NDEF_SENT).sendToTarget();
    }

    private final Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NDEF_SENT:
                    Utils.showToast(getApplicationContext(), "Event sent!");
                    break;
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Objects.equals(getIntent().getAction(), NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            processIntent();
        }
    }

    private void processIntent() {
        Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        if (rawMsgs!=null) {
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            String receievedEventJSON = new String(msg.getRecords()[0].getPayload());
            event = gson.fromJson(receievedEventJSON,Event.class);
            showViewEventFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EVENT_SAVE_INSTANCE_KEY, event);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.event = (Event) savedInstanceState.getSerializable(EVENT_SAVE_INSTANCE_KEY);
            AddEventFragment addEventFragment = (AddEventFragment)
                    fragmentManager.findFragmentByTag(Utils.ADD_FRAGMENT);
            if (addEventFragment != null) {
                addEventFragment.setEvent(event);
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    //SAVE IMAGE TO FIREBASE STORAGE WHENA ACTIVITY COMES BACK FROM THE CAMERA AND UPDATE UI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        if (requestCode == Utils.IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                if (hasInternet(getApplicationContext())) {

                    final Bitmap imageBitmap = (Bitmap) extras.get("data");
                    final StorageReference eventImages = storageReference.child(Utils.EVENTS_IMAGES);
                    if (event==null){
                        Log.d(TAG, "onActivityResult: ADDING NEW EVENT IMAGE");
                        event = new Event();
                    }
                    if (event.getImageID().isEmpty()){
                        event.setImageID(System.currentTimeMillis()+"");
                    }
                    StorageReference thisEventImage = eventImages.child(event.getImageID());
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

                            AddEventFragment addEventFragment = (AddEventFragment)
                                    fragmentManager.findFragmentByTag(Utils.ADD_FRAGMENT);
                            if (addEventFragment != null) {
                                addEventFragment.setEventBitMap(imageBitmap);
                                if (downloadUrl != null) {
                                    Log.d(TAG, "onSuccess: " + downloadUrl);
                                    event.setStorageURL(downloadUrl.toString());
                                }
                                addEventFragment.setEvent(event);
                            }
                        }
                    });
                    uploadTask.addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Utils.showToast(getApplicationContext(), "Image upload failed");
                        }
                    });
                }else {
                    Utils.showToast(getApplicationContext(),"Sorry, we cant save images when device is offline :(");
                }
            }
        } else {
            Utils.showToast(getApplicationContext(), "Failed to save Image");
        }
    }



}
