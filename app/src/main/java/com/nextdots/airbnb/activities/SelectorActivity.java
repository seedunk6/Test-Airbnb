package com.nextdots.airbnb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nextdots.airbnb.R;
import com.nextdots.airbnb.utils.UserSessionManager;

/**
 * Created by Mariexi on 03/12/2016.
 */
public class SelectorActivity extends AppCompatActivity {
    private static final String TAG = SelectorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        UserSessionManager sesion = new UserSessionManager(this);

        if(sesion.isRemember()) {
            //Main
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }else{
            //Login
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

}
