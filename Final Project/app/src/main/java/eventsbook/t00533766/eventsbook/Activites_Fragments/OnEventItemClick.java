package eventsbook.t00533766.eventsbook.Activites_Fragments;

import eventsbook.t00533766.eventsbook.EventData.Event;

public interface OnEventItemClick {

    public void eventItemClicked(String clickedButton, Event event);
    public void eventSelectedToView(Event event);
}