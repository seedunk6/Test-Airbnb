package com.nextdots.airbnb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.HashMap;

/**
 * Created by Mariexi on 03/12/2016.
 */
public class UserSessionManager {
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_INICIO = "inicio";
    private static final String KEY_FOTO = "foto";
    private static final String KEY_ID = "id_user";
    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "SesionPref";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASS = "pass";
    private boolean remember;
    private String photoPath;

    public String getNombre() {
        return pref.getString(KEY_NAME,"");
    }

    public void setNombre(String nombre) {
        editor.putString(KEY_NAME, nombre);
        editor.commit();
    }

    public String getPassword() {
        return pref.getString(KEY_PASS,"");
    }

    public void setPassword(String password) {
        editor.putString(KEY_PASS, password);
        editor.commit();
    }

    boolean inicio;
    // Constructor
    public UserSessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        if(!isRemember())
        {
            logoutUser();
        }
        setInicio(true);
    }

    //Create login session
    public void createUserSession(Bundle bundle)
    {
        String nombre=bundle.getString("nombre");
        String password=bundle.getString("password");
        String id_user=bundle.getString("id_user");
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_NAME, nombre);
        // Storing email in pref
        editor.putString(KEY_PASS, password);
        //editor.putString(KEY_PUNTOS, puntos);
        editor.putString(KEY_ID, id_user);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin()
    {
        // Check login status
        if(!this.isUserLoggedIn())
        {
            return true;
        }
        return false;
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser()
    {
        editor.clear();
        editor.commit();
    }

    // Check for login
    public boolean isUserLoggedIn()
    {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public void setRemember(boolean remember)
    {
        editor.putBoolean(KEY_REMEMBER, remember);
        editor.commit();

    }

    public boolean isRemember()
    {
        return pref.getBoolean(KEY_REMEMBER, false);
    }

    public void setInicio(boolean inicio)
    {
        this.inicio=inicio;
    }

    public boolean isInicio()
    {
        return inicio;
    }

    public void setPhotoPath(String photoPath)
    {
        editor.putString(KEY_FOTO, photoPath);
        editor.commit();
    }
    public void setIdUser(String idUser)
    {
        editor.putString(KEY_ID, idUser);
    }

    public String getPhotoPath() {
        return pref.getString(KEY_FOTO, "");
    }
    public String getIdUser() {
        return pref.getString(KEY_ID, "");
    }
}