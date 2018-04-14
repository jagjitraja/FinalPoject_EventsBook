package eventsbook.t00533766.eventsbook.Activities_Fragments.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.LoggedInUserSingleton;
import eventsbook.t00533766.eventsbook.Utilities.Utils;


public class AddEventFragment extends Fragment {


    private AddEventFragmentListener eventFragmentListener;

    private final String TAG = AddEventFragment.class.getSimpleName();
    private Event event;

    private User user;
    private EditText eventNameEditText;
    private TextView eventDateTextView;
    private EditText eventPriceEditText;
    private EditText eventAddressEditText;
    private EditText eventDescriptionEditText;
    private Button postEventButton;
    private ImageButton myLocationButton;
    private ImageView eventImage;

    private final int ADDING = 1;
    private final int EDITING = 2;

    private Date selectedDate = new Date(System.currentTimeMillis());

    private int EVENT_TASK = ADDING;

    public AddEventFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = LoggedInUserSingleton.getLoggedInUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventNameEditText = getActivity().findViewById(R.id.event_name_text_view);
        eventDateTextView = getActivity().findViewById(R.id.event_date_val);
        eventDateTextView.setOnClickListener(dateOnClickListener);
        eventPriceEditText = getActivity().findViewById(R.id.event_price__text_view);
        eventAddressEditText = getActivity().findViewById(R.id.address_edit_text);
        eventDescriptionEditText = getActivity().findViewById(R.id.event_description_text_view);
        postEventButton = getActivity().findViewById(R.id.post_event_button);
        myLocationButton = getActivity().findViewById(R.id.my_location_button);
        myLocationButton.setOnClickListener(locationOnClickListener);
        eventImage = getActivity().findViewById(R.id.event_imageView);
        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventFragmentListener.takePictureClicked();
            }
        });

        intializeLayout();
    }


    private void intializeLayout() {

        Log.d(TAG, "-----------------------------------------: "+(event==null));

        if (event != null) {
            eventNameEditText.setText(event.getEventName());
            eventDescriptionEditText.setText(event.getDescription());
            eventDateTextView.setText(event.getEventDate());
            eventPriceEditText.setText(event.getEventPrice() + "");
            eventAddressEditText.setText(event.getAddressLocation());
            Glide.with(getContext())
                    .load(event.getStorageURL())
                    .into(eventImage);
        }
        postEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEvent();
            }
        });
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setEventFragmentListener(AddEventFragmentListener eventFragmentListener) {
        this.eventFragmentListener = eventFragmentListener;
    }

    public void updateLocationEditText(Address address) {
        eventAddressEditText = getActivity().findViewById(R.id.address_edit_text);
        if (eventAddressEditText!=null){
            eventAddressEditText.setText(address.getAddressLine(0));
        }
    }

    public void setEventBitMap(Bitmap eventBitMap) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),eventBitMap);
        eventImage.setBackground(bitmapDrawable);
    }


    public interface AddEventFragmentListener {
        void PostEvent(Event event);
        void UpdateEvent(Event event);
        String getLocationClicked();
        void takePictureClicked();
    }

    public void postEvent() {

        if (event==null){
            event = new Event();
        }

        String eventName = eventNameEditText.getText().toString();
        String eventDate = eventDateTextView.getText().toString();
        String eventPrice = eventPriceEditText.getText().toString();
        String eventAddress = eventAddressEditText.getText().toString();
        String eventDescription = eventDescriptionEditText.getText().toString();

        if (TextUtils.isEmpty(eventName)||TextUtils.isEmpty(eventDate)||TextUtils.isEmpty(eventPrice)
                ||TextUtils.isEmpty(eventAddress)||TextUtils.isEmpty(eventDescription)){

            Utils.showToast(getContext(),"Please fill out all fields");
            return;
        }

        event.setEventName(eventName);
        event.setEventPrice(Double.parseDouble(eventPrice));
        event.setAddressLocation(eventAddress);
        event.setDescription(eventDescription);
        event.setEventDate(eventDate);
        
        Log.d(TAG, user + "\n\n\npostEvent: ******************************************" + event);
        if (Objects.equals(getActivity().getIntent().getAction(), Utils.ADD_INTENT_ACTION)) {
            eventFragmentListener.PostEvent(event);
        } else if (Objects.equals(getActivity().getIntent().getAction(), Utils.EDIT_INTENT_ACTION)) {
            eventFragmentListener.UpdateEvent(event);
        }

    }

    public void setDate(Date date) {
        this.selectedDate = date;
    }

    private View.OnClickListener dateOnClickListener = new View.OnClickListener() {

        private Date date = new Date(System.currentTimeMillis());

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.show();
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(i, i1, i2);
                    Date date = calendar.getTime();
                    eventDateTextView.setText(Utils.dateFormat.format(date));
                    setDate(date);
                }
            });
        }
    };
    private View.OnClickListener locationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String address = eventFragmentListener.getLocationClicked();
            if(address!=null){
                Log.d(TAG, "onClick: "+address);
                eventAddressEditText.setText(address);
            }else {
                Utils.showToast(getContext(),"Failed to obtain location :(");
            }
        }
    };

}


//TODO: ADDRESS SEARCH WHEN ENTERING ADDRESS
