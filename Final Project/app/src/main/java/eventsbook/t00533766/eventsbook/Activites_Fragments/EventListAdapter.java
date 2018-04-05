package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.R;

import static eventsbook.t00533766.eventsbook.Utilities.Utils.*;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.INTENT_ACTION;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.INTENT_FRAGMENT_CODE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.VIEW_FRAGMENT_CODE;

/**
 * Created by T00533766 on 3/28/2018.
 */

public class EventListAdapter extends
        RecyclerView.Adapter<EventListAdapter.EventItemHolder> {

    private final String TAG = EventListAdapter.class.getSimpleName();
    private ArrayList<Event> eventArrayList;
    private Context context;
    private OnEventItemClick onEventItemClickListener;

    public EventListAdapter(ArrayList<Event> eventArrayList,
                            Context context,OnEventItemClick onEventItemClick) {
        this.eventArrayList = eventArrayList;
        this.context= context;
        this.onEventItemClickListener = onEventItemClick;
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
                Intent intent = new Intent(context,EventDetailActivity.class);
                intent.setAction(INTENT_ACTION);
                intent.putExtra(INTENT_FRAGMENT_CODE, VIEW_FRAGMENT_CODE);
                intent.putExtra(VIEW_EVENT_INTENT_KEY,eventArrayList.get(position));
                context.startActivity(intent);
            }
        });
    }

    public void setEventArrayList(ArrayList<Event> eventArrayList){
        this.eventArrayList = eventArrayList;notifyDataSetChanged();
    }

    public void addEvent(Event event){
        this.eventArrayList.add(event);
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

        public EventItemHolder(final View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventname);
            eventDateTextView = itemView.findViewById(R.id.eventdate);
            eventDescriptionTextView = itemView.findViewById(R.id.eventdesctiption);
            interestedButton = itemView.findViewById(R.id.interested_button);
            registerButton = itemView.findViewById(R.id.register_button);
            eventPostersNameTextView = itemView.findViewById(R.id.posters_name);
            eventCityTextView = itemView.findViewById(R.id.eventCity);

            //TODO:HIDE INTERESTED AND REGISTER BUTTONS FOR SAME USER

        }

        public void bindDataToView(final int pos){
            Event event = eventArrayList.get(pos);
            eventNameTextView.setText(event.getEventName());
            eventDescriptionTextView.setText(event.getDescription());
            eventDateTextView.setText(event.getStringDate());
            eventPostersNameTextView.setText(event.getPostedBy().getUserName());
            eventCityTextView.setText(event.getAddressLocation());

            interestedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEventItemClickListener.eventItemClicked("INTERESTED",eventArrayList.get(pos));
                }
            });
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEventItemClickListener.eventItemClicked("REGISTER",eventArrayList.get(pos));
                }
            });
        }
    }
}
