package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            Utils.showSnackBar(findViewById(R.id.main_activity),
                    "User Session Timed Out",
                    null,null);
            goToSplashActivity();
            finish();
        }

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

    private void goToAddEventActivity() {
        Utils.goToActivity(new Intent(getApplicationContext(),AddEventActivity.class),0,getApplicationContext());
    }
    private void goToSplashActivity() {
        Utils.goToActivity(
                new Intent(getApplicationContext(),SplashIntroActivity.class),
                0,
                getApplicationContext()
        );
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.events_line) {
            toolbar.setTitle(R.string.app_name);
        } else if (id == R.id.posted_events) {
            toolbar.setTitle("My Events");
        } else if (id == R.id.attending_events) {
            toolbar.setTitle(R.string.saved_events);
        } else if (id == R.id.settings) {
            toolbar.setTitle(R.string.settings);
        } else if (id == R.id.sign_out) {
            firebaseAuth.signOut();
            goToSplashActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
