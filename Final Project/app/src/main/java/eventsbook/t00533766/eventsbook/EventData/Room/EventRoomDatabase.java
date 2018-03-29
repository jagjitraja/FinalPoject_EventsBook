package eventsbook.t00533766.eventsbook.EventData.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;

/**
 * Created by T00533766 on 3/28/2018.
 */

@Database(entities = {Event.class, User.class},version = 1)
public abstract class EventRoomDatabase extends RoomDatabase {
}
