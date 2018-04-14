package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.net.URL;
import java.util.ArrayList;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.LoggedInUserSingleton;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

/**
 * Created by T00533766 on 3/28/2018.
 */

public class EventListAdapter extends
        RecyclerView.Adapter<EventListAdapter.EventItemHolder> {

    private final String TAG = EventListAdapter.class.getSimpleName();
    private ArrayList<Event> eventArrayList;
    private Context context;
    private OnEventItemClick onEventItemClickListener;
    private User loggedInUser;
    private FirebaseStorage firebaseStorage;

    public EventListAdapter(ArrayList<Event> eventArrayList,
                            Context context, OnEventItemClick onEventItemClick) {
        this.eventArrayList = eventArrayList;
        this.context= context;
        this.onEventItemClickListener = onEventItemClick;
        this.loggedInUser = LoggedInUserSingleton.getLoggedInUser();
        firebaseStorage = FirebaseStorage.getInstance(Utils.EVENT_IMAGES_STORAGE);
    }

    @Override
    public EventItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.event_item_layout,parent,false);

        return new EventItemHolder(itemview);
    }

    @Override
    public void onBindViewHolder(EventItemHolder holder, final int position) {

        holder.bindDataToView(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEventItemClickListener.viewEventSelected(eventArrayList.get(position));
            }
        });
    }

    public void addEvent(Event event){
        this.eventArrayList.add(event);
        notifyDataSetChanged();
        Log.d(TAG, "addEvent: "+eventArrayList);
    }

    public void setEventArrayList(ArrayList<Event> eventArrayList){
        this.eventArrayList = eventArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    public class EventItemHolder extends RecyclerView.ViewHolder {

        TextView eventNameTextView;
        TextView eventDateTextView;
        TextView eventPostersNameTextView;
        TextView eventCityTextView;
        TextView eventDescriptionTextView;
        Button interestedButton;
        Button registerButton;
        ImageView eventImage;

        public EventItemHolder(final View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventname);
            eventDateTextView = itemView.findViewById(R.id.eventdate);
            eventDescriptionTextView = itemView.findViewById(R.id.eventdesctiption);
            interestedButton = itemView.findViewById(R.id.interested_button);
            registerButton = itemView.findViewById(R.id.register_button);
            eventPostersNameTextView = itemView.findViewById(R.id.posters_name);
            eventCityTextView = itemView.findViewById(R.id.eventCity);
            eventImage = itemView.findViewById(R.id.event_image);

        }

        public void bindDataToView(final int pos){
            final Event event = eventArrayList.get(pos);
            eventNameTextView.setText(event.getEventName());
            eventDescriptionTextView.setText(event.getDescription());
            eventDateTextView.setText(event.getEventDate());
            eventPostersNameTextView.setText(event.getPostedBy().getUserName());
            eventCityTextView.setText(event.getAddressLocation());

            boolean registering;
            if (event.getAttendingUsersCount()>0&&
                    event.getAttendingUsers().contains(loggedInUser.getUserID())){
                registerButton.setText(R.string.remove_event);
            }else {
                registerButton.setText(R.string.register_event);
            }
            if (event.getInterestedUsersCount()>0&&
                    event.getInterestedUsers().contains(loggedInUser.getUserID())){
                interestedButton.setText(R.string.unsave_event);
            }else {
                interestedButton.setText(R.string.interested_event);
            }

            interestedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEventItemClickListener.
                            eventInterestedClicked(event);
                }
            });
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEventItemClickListener.eventRegisterClicked(event);
                }
            });

            //LOAD IMAGES IN RECYCLER
            Glide.with(context)
                    .load(event.getStorageURL())
                    .into(eventImage);


        }
    }
}

//TODO BUG WHEN EDITING EVENT RIGHT AFTER ADDING  WITHOUT RELAUNCHING APP