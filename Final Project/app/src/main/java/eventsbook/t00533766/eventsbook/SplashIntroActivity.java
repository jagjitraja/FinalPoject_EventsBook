package eventsbook.t00533766.eventsbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Timer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashIntroActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private final static int SIGN_IN_CODE = 152;
    private final String TAG = SplashIntroActivity.class.getName();
    private Button login;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_intro);

        login = findViewById(R.id.splash_button_login);
        progressBar = findViewById(R.id.progress_bar_login);
        progressBar.setMax(100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progressBar.setMin(0);
        }
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            Log.d(TAG, "onCreate: PREVIOUS LOGGED IN USER");
            login.setText("Continue");
            progressBar.setProgress(25);
        } else {
            login.setText("Login");
            progressBar.setProgress(25);
        }
    }

    private void goToMainActivity() {

        Handler handler = new Handler();
        handler.postDelayed(goToMainActivityRunnable, 3000);

    }

    private Runnable goToMainActivityRunnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setProgress(100);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    };
    //TODO - Connect and get location, and updates from database
    //TODO - Show icon and spinner

    private void showSignInScreen() {

        AuthUI authUI = AuthUI.getInstance();

        startActivityForResult(authUI.createSignInIntentBuilder()
                .setIsSmartLockEnabled(true, true)
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                        // new AuthUI.IdpConfig.FacebookBuilder().build()
                )).build(), SIGN_IN_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: *******************************");
        if (requestCode == SIGN_IN_CODE) {
            Log.d(TAG, "onActivityResult: RESULT =====================");
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: RESULT OK");
                progressBar.setProgress(90);
                goToMainActivity();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "onActivityResult: RESULT CANCELLED");
                showSnackBar(getString(R.string.cancelled_sign_in));
            } else {
                Log.d(TAG, "onActivityResult: RESULT FAILED");
                showSnackBar(getString(R.string.failed_sign_in));
            }
        }

    }

    private void showSnackBar(String s) {
        Log.d(TAG, "showSnackBar: ");
        Snackbar.make(findViewById(R.id.splash_view), s, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSignInScreen();
                    }
                }).show();
    }


    public void login(View view) {
        if (firebaseAuth.getCurrentUser() != null) {
            goToMainActivity();
            progressBar.setProgress(90);
        } else {
            showSignInScreen();
            progressBar.setProgress(65);
        }
    }

}
