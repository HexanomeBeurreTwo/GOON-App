package com.h4115.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreen extends Activity {

    protected final int SPLASH_DISPLAY_LENGTH = 1500;
    protected final DBHandler dbHandler = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                User user = dbHandler.getUser();
                if(user != null) {
                    GetUser getUser = new GetUser(getApplicationContext(), dbHandler);
                    getUser.execute("https://goonapp-dev.herokuapp.com/connection" + "?username=" + user.getUsername() + "&password=" + user.getPassword());
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    SplashScreen.this.startActivity(intent);
                    SplashScreen.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
