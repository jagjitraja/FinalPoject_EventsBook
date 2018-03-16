package eventsbook.t00533766.eventsbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_intro);


        Handler handler = new Handler();
        handler.postDelayed(goToMainActivityRunnable,3000);
    }

    private Runnable goToMainActivityRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    };
    //TODO - Connect and get location, and updates from database
    //TODO - Show icon and spinner

}
