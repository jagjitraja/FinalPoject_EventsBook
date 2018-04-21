package eventsbook.t00533766.eventsbook.Activities_Fragments.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import eventsbook.t00533766.eventsbook.Activities_Fragments.EventListAdapter;
import eventsbook.t00533766.eventsbook.Activities_Fragments.OnEventItemClick;
import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.FireBaseUtils;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.LoggedInUserSingleton;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_DATA;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.ADD_INTENT_ACTION;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.VIEW_INTENT_ACTION;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.hasInternet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnEventItemClick {

    // TODO: EVENT REPLIES
    public final static int ADD_EVENT_REQUEST = 500;

    private User loggedInUser;

    private EventListAdapter eventListAdapter;

    private ArrayList<Event> eventArrayList;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private DrawerLayout drawer;
    private Toolbar toolbar;


    private ChildEventListener childEventListener = new ChildEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "onChildAdded: ");
            //CHECK NEW EVENT ADDED AND AD IT TO THE LIST ADAPTER
            Event addedEvent = dataSnapshot.getValue(Event.class);
            if (addedEvent != null) {
                addedEvent.setEventID(dataSnapshot.getKey());
                eventListAdapter.addEvent(addedEvent);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            //REMOVE EVENT IF DELETED
            if (eventListAdapter!=null)
                eventListAdapter.notifyDataSetChanged();
            Utils.showToast(getApplicationContext(),"Event Deleted");

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    public final String TAG = MainActivity.class.getSimpleName();

    private AdView adView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int addCount = 0;
        //COUNT TO CHECK NUMBER OF TIMES APP WAS LAUNCHED
        SharedPreferences sharedPreferences = getSharedPreferences("AD_COUNTER", MODE_PRIVATE);
        if (sharedPreferences.contains("ADD_COUNT")) {
            addCount = sharedPreferences.getInt("ADD_COUNT", 0);
            addCount++;
            sharedPreferences.edit().putInt("ADD_COUNT", addCount).apply();
        } else {
            sharedPreferences.edit().putInt("ADD_COUNT", 0).apply();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        eventArrayList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        eventListAdapter = new EventListAdapter(eventArrayList, getApplicationContext(), this);
        recyclerView.setAdapter(eventListAdapter);

        initializeUIElements();
        if (firebaseAuth.getCurrentUser() == null) {
            //Utils.showSnackBar(findViewById(R.id.main_activity),
            //        "User Session Timed Out",
            //        null,null);
            goToSplashActivity();
            finish();
        }
        initializeFireBase();


        //CHECK THE INTENT ACTION TO SEE IF IT WAS FROM EDITING OR DELETEING AN INTENT
        Intent intent = getIntent();
        if (intent.getAction() != null) {
            if (intent.getAction().equalsIgnoreCase(Utils.EDIT_INTENT_ACTION)) {
                updateEvent((Event) intent.getSerializableExtra(Utils.EVENT_DATA));
            }else if (intent.getAction().equals(Utils.DELETE_INTENT_ACTION)){
                deleteEvent((Event) intent.getSerializableExtra(Utils.EVENT_DATA));
            }
        }

        if (!hasInternet(getApplicationContext())) {
            Utils.showToast(getApplicationContext(), "Device is offline, some features may not be available :(");
        }

        requestCalenderPermission();

        //DISPLAY AD IF USER LAUNCHES THE APP THE FIFTH TIME
        adView = findViewById(R.id.adView);
        if (addCount % 5 == 0) {
            AdRequest adRequest = new AdRequest.Builder()
                    //TEST DEVICE
                    .addTestDevice("E3EE1D13EDAEB3017D14B72B162321EC")
                    .build();
            adView.loadAd(adRequest);
            //TODO
            adView.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }
            });
            //AUTO HIDE
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adView.setVisibility(View.INVISIBLE);
                }
            }, 150000);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(R.string.app_name);
    }


    //REQUEST CALENDER WRITE ACCESS
    private void requestCalenderPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    Utils.CALENDER_REQUEST_CODE);
        }

    }

    private void initializeUIElements() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddEventActivity();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                TextView userNameTextView = findViewById(R.id.user_name);
                TextView userEmailTextView = findViewById(R.id.user_email);
                if (firebaseAuth.getCurrentUser().getDisplayName() != null)
                    userNameTextView.setText(firebaseAuth.getCurrentUser().getDisplayName());
                userEmailTextView.setText(firebaseAuth.getCurrentUser().getEmail());

                super.onDrawerOpened(drawerView);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    //INITIALIZE FIREBASE AND GET A REFERENCE TO THE EVENTS NODE
    private void initializeFireBase() {
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseDatabase firebaseDatabase = FireBaseUtils.getFirebaseDatabase();
            databaseReference = firebaseDatabase.getReference().child(Utils.EVENT_NODE);
            setChildEventListener();
            loggedInUser = LoggedInUserSingleton.getLoggedInUser();
        }
    }

    //SET CHILD EVENT LISTENER AND REFRESH DATA WHEN LAUNCHED
    private void setChildEventListener() {
        if (databaseReference != null) {
            Log.d(TAG, "setChildEventListener: ");
            Query eventsAfterDate = null;
            eventsAfterDate = databaseReference.orderByChild("eventDate");
            eventsAfterDate.addChildEventListener(childEventListener);
            eventArrayList.clear();
            eventListAdapter.setEventArrayList(eventArrayList);
        }
    }

    private void removeChildEventListener() {
        if (databaseReference != null)
            databaseReference.removeEventListener(childEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeChildEventListener();
    }

    private void goToAddEventActivity() {
        Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
        intent.setAction(ADD_INTENT_ACTION);
        startActivityForResult(intent, ADD_EVENT_REQUEST);
    }

    private void postEventToDatabase(Event event) {
        databaseReference.push().setValue(event);
        insertEventInCalender(event);
    }

    //CREATE INTENT TO SAVE IN DEVICE CALENDER
    private void insertEventInCalender(Event event) {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    Utils.CALENDER_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getEventDateinMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getEventDateinMillis())
                    .putExtra(CalendarContract.Events.TITLE, event.getEventName())
                    .putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription())
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, event.getAddressLocation());
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Utils.CALENDER_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Utils.showToast(getApplicationContext(), "Calender Sync disabled :(");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_EVENT_REQUEST:
                //IF ACTIVITY RETURNS FROM ADDING AN EVENT, IT WILL ADD TO ADAPTER AND REFRESH
                if (resultCode == RESULT_OK) {
                    Event event = (Event) data.getSerializableExtra(EVENT_DATA);
                    eventListAdapter.addEvent(event);
                    eventListAdapter.notifyDataSetChanged();
                    postEventToDatabase(event);
                }
                break;
            default:
                break;
        }
    }

    private void goToSplashActivity() {
        Utils.goToActivity(new Intent(getApplicationContext(),
                        SplashIntroActivity.class),
                0, getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.filter_events) {
        //FILTER EVENTS BASED ON THE SELECTED DATE
            DatePickerDialog datePickerDialog = new DatePickerDialog(this);
            datePickerDialog.setTitle(getString(R.string.filter_date_prompt));
            datePickerDialog.show();
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(i, i1, i2);
                    Date date = calendar.getTime();
                    Query eventsAfterDate = null;

                    eventsAfterDate = databaseReference.orderByChild("eventDate")
                            .equalTo(String.valueOf(Utils.dateFormat.format(date)));
                    eventsAfterDate.addChildEventListener(childEventListener);
                    eventArrayList.clear();
                    eventListAdapter.setEventArrayList(eventArrayList);
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.d(TAG, "onNavigationItemSelected: ");
        int id = item.getItemId();

        if (id == R.id.events_line) {
            toolbar.setTitle(R.string.app_name);
            Query eventsAfterDate = null;
            eventsAfterDate = databaseReference.orderByChild("eventDate");
            eventsAfterDate.addChildEventListener(childEventListener);
            eventArrayList.clear();
            eventListAdapter.setEventArrayList(eventArrayList);
        } else if (id == R.id.posted_events) {
            toolbar.setTitle(R.string.my_posted_events);
            ArrayList<Event> myPostedEvents = new ArrayList<>();
            for (Event event : eventArrayList) {
                if (event.getPostedBy().getUserID().equals(loggedInUser.getUserID())) {
                    myPostedEvents.add(event);
                }
            }
            eventListAdapter.setEventArrayList(myPostedEvents);
        } else if (id == R.id.attending_events) {
            toolbar.setTitle(R.string.registered_events);
            ArrayList<Event> myAttendingEvents = new ArrayList<>();
            for (Event event : eventArrayList) {
                if (event.getAttendingUsers().contains(loggedInUser.getUserID())) {
                    myAttendingEvents.add(event);
                }
            }
            eventListAdapter.setEventArrayList(myAttendingEvents);
        } else if (id == R.id.saved_events) {
            toolbar.setTitle(R.string.saved_events);
            ArrayList<Event> mySavedEvents = new ArrayList<>();
            for (Event event : eventArrayList) {
                if (event.getInterestedUsers().contains(loggedInUser.getUserID())) {
                    mySavedEvents.add(event);
                }
            }
            eventListAdapter.setEventArrayList(mySavedEvents);
        } else if (id == R.id.settings) {
            toolbar.setTitle(R.string.settings);
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        } else if (id == R.id.sign_out) {
            firebaseAuth.signOut();
            goToSplashActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //CALL BACKS FROM THE LIST ADAPTER TO UPDATE THE DATABSE AND EVENTS
    @Override
    public void eventInterestedClicked(Event event) {

        if (!event.getInterestedUsers().contains(loggedInUser.getUserID())) {
            event.addInterestedUser(loggedInUser.getUserID());
            insertEventInCalender(event);
        } else {
            event.removeInterestedUser(loggedInUser.getUserID());
        }
        updateEvent(event);
    }

    @Override
    public void eventRegisterClicked(Event event) {

        if (!event.getAttendingUsers().contains(loggedInUser.getUserID())) {
            event.addAttendingUsers(loggedInUser.getUserID());
            insertEventInCalender(event);
        } else {
            event.removeAttendingUser(loggedInUser.getUserID());
        }
        updateEvent(event);
    }

    @Override
    public void viewEventSelected(Event event) {
        Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
        intent.setAction(VIEW_INTENT_ACTION);
        intent.putExtra(EVENT_DATA, event);
        startActivity(intent);
    }


    public void updateEvent(Event event) {
        if (!Objects.equals(event.getEventID(), "")) {
            databaseReference.child(event.getEventID()).setValue(event);
            Utils.showToast(getApplicationContext(), "Updating Event, Refreshing Events List ");
            toolbar.setTitle(R.string.app_name);

            //UPDATE UI FOR SAVES AND REGISTERS
            databaseReference.child(event.getEventID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Event addedEvent = dataSnapshot.getValue(Event.class);
                    for (int i = 0; i < eventArrayList.size(); i++) {
                        if (addedEvent != null && addedEvent.getEventID().equals(eventArrayList.get(i).getEventID())) {
                            eventArrayList.set(i, addedEvent);
                            break;
                        }
                    }
                    eventListAdapter.setEventArrayList(eventArrayList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void deleteEvent(Event event) {
        eventListAdapter.removeEvent(event);
        if (event.getEventID().length()>0)
            databaseReference.child(event.getEventID()).removeValue();
        Log.d(TAG, "deleteEvent: "+event.getEventID()+"                "+databaseReference.child(event.getEventID()));

    }
}
