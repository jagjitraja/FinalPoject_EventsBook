package eventsbook.t00533766.eventsbook.EventData.EventRoomDatabase;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

public class EventFireBase {

    private static FirebaseDatabase firebaseDatabase;

    public static FirebaseDatabase getFirebaseDatabase() {
        if(firebaseDatabase==null){
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }
        return firebaseDatabase;
    }
}
