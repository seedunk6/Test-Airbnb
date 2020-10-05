package com.nextdots.airbnb.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nextdots.airbnb.*;
import com.nextdots.airbnb.utils.PermissionsDispatcher;
import com.nextdots.airbnb.utils.UserSessionManager;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mariexi on 04/12/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    UserSessionManager sesion;
    @BindView(R.id.login_button)
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        PermissionsDispatcher.showDialogPermissions(this);
        ButterKnife.bind(this);
        sesion = new UserSessionManager(getApplicationContext());

        keyHashGenerate();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));

        LoginManager.getInstance().logOut();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.e("-->", "Entro Facebook: "+loginResult.getAccessToken().getUserId());

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                        String id = jsonObject.optString("id");
                        String email = jsonObject.optString("email");
                        String firstName = jsonObject.optString("first_name");
                        String lastName = jsonObject.optString("last_name");
                        String foto = "https://graph.facebook.com/" + id + "/picture?width=200&height=150";

                        sesion.setNombre(firstName + " " +lastName);
                        sesion.setPhotoPath(foto);

                        Log.e("--> Email ", email);
                        Log.e("--> User ", firstName +" "+lastName);
                        Log.e("--> Foto ", foto);
                        Log.v("LoginActivity", graphResponse.toString());

                        sesion.setRemember(true);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void keyHashGenerate(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.nextdots.airbnb", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

}
