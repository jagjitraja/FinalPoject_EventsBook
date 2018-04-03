package eventsbook.t00533766.eventsbook.EventData.EventRoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;

@Database(entities = {Event.class, User.class},version = 1)
public abstract class EventRoomDatabase extends RoomDatabase {

    public abstract  EventDao getEventDao();

    private static EventRoomDatabase eventRoomDatabase;


    public static EventRoomDatabase getEventRoomDatabaseInstance(Context context){

        if(eventRoomDatabase==null){
            eventRoomDatabase = Room.databaseBuilder(context,EventRoomDatabase.class,
                    EventRoomDatabase.class.getSimpleName()).build();
        }

        return eventRoomDatabase;
    }



}
