package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Event> eventArrayList;

    public final static String FIRE_BASE_USER = "FIREBASE_USER";
    public static final String EVENT_DATA = "EVENT ADDED";

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "onChildAdded: ");
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "onChildChanged: ");
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d(TAG, "onChildRemoved: ");
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


    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;


    private final static int ADD_EVENT_REQUEST = 500;
    public final static int EVENT_ADD_SUCCESS = 20;
    public final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser()==null){
            //Utils.showSnackBar(findViewById(R.id.main_activity),
            //        "User Session Timed Out",
            //        null,null);
            goToSplashActivity();
            finish();
        }
        initializeFireBase();
        eventArrayList = new ArrayList<>();
        initializeUIElements();

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
        
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        eventListAdapter = new EventListAdapter(eventArrayList,getApplicationContext());
        recyclerView.setAdapter(eventListAdapter);


    }
    private void initializeFireBase() {
        if (firebaseAuth.getCurrentUser()!=null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid());
            setChildEventListener();
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

        Intent intent =new Intent(getApplicationContext(),AddEventActivity.class);

        String name = firebaseAuth.getCurrentUser().getDisplayName();
        if (name==null){
            name = "";
        }
        User user = new User(firebaseAuth.getUid(), name
                ,firebaseAuth.getCurrentUser().getEmail());
        intent.putExtra(FIRE_BASE_USER,user);

        startActivityForResult(intent,ADD_EVENT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: "+requestCode+"        "+resultCode);
        switch (requestCode){
            case ADD_EVENT_REQUEST:
                if (resultCode==EVENT_ADD_SUCCESS){
                    Log.d(TAG, "onActivityResult: ");
                    Event event = (Event) data.getSerializableExtra(EVENT_DATA);
                    eventArrayList.add(event);
                    eventListAdapter.notifyDataSetChanged();

                    postEventToDatabase(event);
                }
                break;
            default:
                break;
        }
    }

    private void postEventToDatabase(Event event) {
        databaseReference.push().setValue(event);
    }

    private void goToSplashActivity() {
        Utils.goToActivity(new Intent(getApplicationContext(),SplashIntroActivity.class),
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d(TAG, "onNavigationItemSelected: ");
        int id = item.getItemId();

        if (id == R.id.events_line) {
            toolbar.setTitle(R.string.app_name);
        } else if (id == R.id.posted_events) {
            toolbar.setTitle(R.string.my_events);
        } else if (id == R.id.attending_events) {
            toolbar.setTitle(R.string.saved_events);
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

    public void interestedClicked(View view) {
        Log.d(TAG, "interestedClicked: ");
    }
}
