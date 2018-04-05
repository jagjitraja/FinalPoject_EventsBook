package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import eventsbook.t00533766.eventsbook.Activites_Fragments.Fragments.AddEventFragment;
import eventsbook.t00533766.eventsbook.Activites_Fragments.Fragments.ViewEventFragment;
import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

import static eventsbook.t00533766.eventsbook.Utilities.Utils.ADD_FRAGMENT_CODE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_ADD_SUCCESS;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_DATA;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.FIRE_BASE_USER_KEY;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.INTENT_FRAGMENT_CODE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.VIEW_FRAGMENT_CODE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.VIEW_EVENT_INTENT_KEY;

public class EventDetailActivity extends FragmentActivity
        implements AddEventFragment.AddEventFragmentListener, ViewEventFragment.ViewEventFragmentListener {

    private final String TAG = EventDetailActivity.class.getSimpleName();
    private Event event;
    private User user;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Intent intent = getIntent();
        if (intent.getAction().equals(Utils.INTENT_ACTION)) {
            String code = intent.getStringExtra(INTENT_FRAGMENT_CODE);
            if (code.equals(ADD_FRAGMENT_CODE)) {
                showAddEventFragment();
                user = (User) intent.getSerializableExtra(FIRE_BASE_USER_KEY);
            } else if (code.equals(VIEW_FRAGMENT_CODE)) {
                showViewEventFragment();
                event = (Event) intent.getSerializableExtra(VIEW_EVENT_INTENT_KEY);
            }
        }else{
            Log.d(TAG, "onCreate: ACTION NOT MATCHING"+ intent.getAction());
        }



    }

    private void showViewEventFragment() {
        Log.d(TAG, "showViewEventFragment: ");
        ViewEventFragment viewEventFragment = new ViewEventFragment();
        viewEventFragment.setEvent(event);
        viewEventFragment.setEventFragmentListener(this);
        addFragment(viewEventFragment);
    }

    private void showAddEventFragment() {
        Log.d(TAG, "showAddEventFragment: ");

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
        AddEventFragment addEventFragment = new AddEventFragment();
        Log.d(TAG, "editEventClicked: e"+event);
        addEventFragment.setEvent(event);
        addEventFragment.setEventFragmentListener(this);
        replaceFragment(addEventFragment);
    }

    private void replaceFragment(Fragment fragment) {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }

    private void addFragment(Fragment fragment){
        fragmentTransaction.add(R.id.fragment_container,fragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    @Override
    public void PostEvent(Event event) {
        Log.d(TAG, "PostEvent: ");
        setResult(EVENT_ADD_SUCCESS, new Intent(this,MainActivity.class).
                putExtra(EVENT_DATA,event));
        finish();
    }
}
