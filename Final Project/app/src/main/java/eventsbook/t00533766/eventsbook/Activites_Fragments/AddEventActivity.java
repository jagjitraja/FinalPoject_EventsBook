package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import eventsbook.t00533766.eventsbook.R;

public class AddEventActivity extends AppCompatActivity {

    private EditText eventNameEditText;
    private TextView eventDateTextView;
    private EditText eventPriceEditText;
    private EditText eventAddressEditText;
    private EditText eventDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        eventNameEditText = findViewById(R.id.event_name_edit_text);
        eventDateTextView = findViewById(R.id.event_date_val);
        eventPriceEditText = findViewById(R.id.event_price_input);
        eventAddressEditText = findViewById(R.id.address_val);
        eventDescriptionEditText = findViewById(R.id.event_description_value);

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




    }
}
