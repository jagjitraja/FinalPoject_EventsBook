package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.R;

/**
 * Created by T00533766 on 3/28/2018.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventItemHolder> {

    private ArrayList<Event> eventArrayList;
    private Context context;

    public EventListAdapter(ArrayList<Event> eventArrayList, Context context) {
        this.eventArrayList = eventArrayList;
        this.context= context;
    }


    @Override
    public EventItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(context).inflate(R.layout.event_item_layout,parent,false);
        return new EventItemHolder(itemview);
    }

    @Override
    public void onBindViewHolder(EventItemHolder holder, int position) {
        holder.bindDataToView(position);
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    public class EventItemHolder extends RecyclerView.ViewHolder {

        TextView eventNameTextView;
        TextView eventDateTextView;
        TextView eventDescriptionTextView;
        Button interestedButton;
        Button registerButton;

        public EventItemHolder(View itemView) {
            super(itemView);

            eventNameTextView = itemView.findViewById(R.id.eventname);
            eventDateTextView = itemView.findViewById(R.id.eventdate);
            eventDescriptionTextView = itemView.findViewById(R.id.eventdesctiption);
           // interestedButton = itemView.findViewById(R.id.interested_button);
            //registerButton = itemView.findViewById(R.id.register_button);
        }

        public void bindDataToView(int pos){
            Event event = eventArrayList.get(pos);
            eventNameTextView.setText(event.getEventName());
            eventDescriptionTextView.setText(event.getDescription());
            eventDateTextView.setText(event.getStringDate());
        }
    }
}
