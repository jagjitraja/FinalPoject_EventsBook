package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.FireBaseUtils;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.LoggedInUserSingleton;
import eventsbook.t00533766.eventsbook.Utilities.Notifications.NotificationUtils;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

import static eventsbook.t00533766.eventsbook.Utilities.Utils.ADD_FRAGMENT_CODE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EDIT_INTENT_ACTION;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_DATA;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.ADD_INTENT_ACTION;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.FIRE_BASE_USER_KEY;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.INTENT_FRAGMENT_CODE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.VIEW_EVENT_INTENT_KEY;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.VIEW_FRAGMENT_CODE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnEventItemClick {

//TODO: NFC VIEW EVENT
    // TODO: SHArE BROADCAST
    //TODO: SEARCH
    //TODO: ADD GOOGLE CALENDER
    // TODO: EVENT REPLIES
    public final static int ADD_EVENT_REQUEST = 500;

    private User loggedInUser;

    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;

    private ArrayList<Event> eventArrayList;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private Toolbar toolbar;


    private ChildEventListener childEventListener = new ChildEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            Log.d(TAG, "onChildAdded: "+dataSnapshot.getKey()+"\n"+dataSnapshot.getValue());
            Event addedEvent = dataSnapshot.getValue(Event.class);
            if (addedEvent != null) {
                addedEvent.setEventID(dataSnapshot.getKey());
                eventListAdapter.addEvent(addedEvent);
            }
            Log.d(TAG, "\n\n\n\nonChildAdded: "+addedEvent);

            //NotificationUtils.createNotification(getApplicationContext(),addedEvent,loggedInUser);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Event addedEvent = dataSnapshot.getValue(Event.class);
            for (int i = 0;i<eventArrayList.size();i++) {
                if (addedEvent.getEventID().equals(eventArrayList.get(i).getEventID())){
                    Log.d(TAG, "-*-*-*-*-*-*-*-*-*-*-*-*--* "+addedEvent);
                    eventArrayList.set(i, addedEvent);
                    break;
                }
            }
            eventListAdapter.setEventArrayList(eventArrayList);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d(TAG, "onChildRemoved: "+dataSnapshot.getKey()+"\n"+dataSnapshot.getValue());

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "onChildMoved: ");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d(TAG, "onCancelled: ");
        }
    };
    public final String TAG = MainActivity.class.getSimpleName();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        eventArrayList = new ArrayList<>();

        initializeUIElements();
        if (firebaseAuth.getCurrentUser()==null){
            //Utils.showSnackBar(findViewById(R.id.main_activity),
            //        "User Session Timed Out",
            //        null,null);
            goToSplashActivity();
            finish();
        }
        initializeFireBase();

        Intent intent = getIntent();
        if (intent.getAction()!=null){
            if(intent.getAction().equalsIgnoreCase(Utils.EDIT_INTENT_ACTION))
                updateEvent((Event) intent.getSerializableExtra(Utils.EVENT_DATA));
        }

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        eventListAdapter = new EventListAdapter(eventArrayList,getApplicationContext(),this);
        recyclerView.setAdapter(eventListAdapter);
    }


    private void initializeUIElements() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddEventActivity();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                TextView userNameTextView = findViewById(R.id.user_name);
                TextView userEmailTextView = findViewById(R.id.user_email);
                if(firebaseAuth.getCurrentUser().getDisplayName()!=null)
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

    private void initializeFireBase() {
        if (firebaseAuth.getCurrentUser()!=null) {
            firebaseDatabase = FireBaseUtils.getFirebaseDatabase();
            databaseReference = firebaseDatabase.getReference().child(Utils.EVENT_NODE);
            setChildEventListener();
            loggedInUser = LoggedInUserSingleton.getLoggedInUser();
        }
    }

    private void setChildEventListener(){
        if (databaseReference!=null){
            databaseReference.addChildEventListener(childEventListener);
        }
    }
    private void removeChildEventListener(){
        if (databaseReference!=null)
        databaseReference.removeEventListener(childEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeChildEventListener();
    }

    private void goToAddEventActivity() {

        Intent intent =new Intent(getApplicationContext(),EventDetailActivity.class);
        intent.setAction(ADD_INTENT_ACTION);
        intent.putExtra(INTENT_FRAGMENT_CODE, ADD_FRAGMENT_CODE);
        startActivityForResult(intent,ADD_EVENT_REQUEST);
    }

    private void postEventToDatabase(Event event) {
        databaseReference.push().setValue(event);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case ADD_EVENT_REQUEST:
                if (resultCode==RESULT_OK){
                    Event event = (Event) data.getSerializableExtra(EVENT_DATA);
                    eventListAdapter.addEvent(event);
                    postEventToDatabase(event);
                }
                break;
            default:
                break;
        }
    }

    //TODO REFRESH ARRAY LIST AFTER UPDATING
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            eventListAdapter.setEventArrayList(eventArrayList);
        } else if (id == R.id.posted_events) {
            toolbar.setTitle(R.string.my_posted_events);
            ArrayList<Event> myPostedEvents = new ArrayList<>();
            for (Event event:eventArrayList) {
                if (event.getPostedBy().getUserID().equals(loggedInUser.getUserID())){
                    myPostedEvents.add(event);
                }
            }
            eventListAdapter.setEventArrayList(myPostedEvents);
        } else if (id == R.id.attending_events) {
            toolbar.setTitle(R.string.registered_events);
            ArrayList<Event> myAttendingEvents = new ArrayList<>();
            for (Event event:eventArrayList) {
                if (event.getAttendingUsers().contains(loggedInUser.getUserID())){
                    myAttendingEvents.add(event);
                }
            }
            eventListAdapter.setEventArrayList(myAttendingEvents);
        }else if (id == R.id.saved_events) {
            toolbar.setTitle(R.string.saved_events);
            ArrayList<Event> mySavedEvents = new ArrayList<>();
            for (Event event:eventArrayList) {
                if (event.getInterestedUsers().contains(loggedInUser.getUserID())){
                    mySavedEvents.add(event);
                }
            }
            eventListAdapter.setEventArrayList(mySavedEvents);
        } else if (id == R.id.settings) {
            toolbar.setTitle(R.string.settings);
            Log.d(TAG, "onNavigationItemSelected: ");
        } else if (id == R.id.sign_out) {
            firebaseAuth.signOut();
            goToSplashActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void eventInterestedClicked(Event event) {
        Log.d(TAG, "eventInterestedClicked: "+"\n"+event);

        if (!event.getInterestedUsers().contains(loggedInUser.getUserID())){
            event.addInterestedUser(loggedInUser.getUserID());
        }else{
            event.removeInterestedUser(loggedInUser.getUserID());
        }
        updateEvent(event);
    }

    @Override
    public void eventRegisterClicked(Event event) {
        Log.d(TAG, "eventRegisterClicked: \n"+event);

        if (!event.getAttendingUsers().contains(loggedInUser.getUserID())){
            event.addAttendingUsers(loggedInUser.getUserID());
        }else{
            event.removeAttendingUser(loggedInUser.getUserID());
        }
        updateEvent(event);
    }

    @Override
    public void viewEventSelected(Event event) {
        Intent intent = new Intent(getApplicationContext(),EventDetailActivity.class);
        intent.setAction(EDIT_INTENT_ACTION);
        intent.putExtra(INTENT_FRAGMENT_CODE, VIEW_FRAGMENT_CODE);
        intent.putExtra(VIEW_EVENT_INTENT_KEY,event);
        startActivity(intent);
    }

    public void updateEvent(Event event){
        Log.d(TAG, "updateEvent: 7777777777777777777"+event+"   \n "+event.getEventID());
        if (!Objects.equals(event.getEventID(), "")) {
            databaseReference.child(event.getEventID()).setValue(event);
            Utils.showToast(getApplicationContext(), "Updating Event, Refreshing Events List ");
            toolbar.setTitle(R.string.app_name);
        }
    }


}
