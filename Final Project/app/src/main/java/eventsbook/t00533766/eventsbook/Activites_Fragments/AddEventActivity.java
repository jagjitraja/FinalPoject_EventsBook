package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

public class AddEventActivity extends AppCompatActivity {

    private String TAG = AddEventActivity.class.getSimpleName();

    private Event event;

    private User user;
    private EditText eventNameEditText;
    private TextView eventDateTextView;
    private EditText eventPriceEditText;
    private EditText eventAddressEditText;
    private EditText eventDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra(Utils.EDIT_EVENT_INTENT_KEY);
        user = (User) intent.getSerializableExtra(Utils.FIRE_BASE_USER_KEY);

        Log.d(TAG, "onCreate: " + user);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        eventNameEditText = findViewById(R.id.event_name_text_view);
        eventDateTextView = findViewById(R.id.event_date_val);
        eventPriceEditText = findViewById(R.id.event_price__text_view);
        eventAddressEditText = findViewById(R.id.address__text_view);
        eventDescriptionEditText = findViewById(R.id.event_description_text_view);

        if (event!=null){
            eventNameEditText.setText(event.getEventName());
            eventDescriptionEditText.setText(event.getDescription());
            eventDateTextView.setText(event.getStringDate());
            eventPriceEditText.setText(event.getEventPrice()+"");
            eventAddressEditText.setText(event.getAddressLocation());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedId = item.getItemId();
        switch (selectedId){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void postEvent(View view) {

        String eventName = eventNameEditText.getText().toString();
        String eventDate = eventDateTextView.getText().toString();
        String eventPrice = eventPriceEditText.getText().toString();
        String eventAddress = eventAddressEditText.getText().toString();
        String eventDescription = eventDescriptionEditText.getText().toString();
        Event event = new Event(0,eventName,eventDescription,new Date()
                ,user,0.00,eventAddress);

        Log.d(TAG, "postEvent: "+event.toString());
        setResult(MainActivity.EVENT_ADD_SUCCESS,
                new Intent(this,MainActivity.class).
                putExtra(MainActivity.EVENT_DATA,event));
        finish();
    }
}
