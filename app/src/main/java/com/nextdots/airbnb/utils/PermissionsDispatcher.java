package com.nextdots.airbnb.utils;

import android.support.v4.app.ActivityCompat;

import com.nextdots.airbnb.activities.LoginActivity;

import permissions.dispatcher.PermissionUtils;

/**
 * Created by Mariexi Hostienda on 04/10/16.
 */
public class PermissionsDispatcher {
    private static final int REQUEST = 1;
    private static final String[] PERMISSIONS = new String[] {"android.permission.ACCESS_FINE_LOCATION"};

    private PermissionsDispatcher() {
    }

    public static void showDialogPermissions(LoginActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSIONS)) {
            //target.initLogin();
        } else {
            ActivityCompat.requestPermissions(target, PERMISSIONS, REQUEST);
        }
    }

    public static void onRequestPermissionsResult(LoginActivity target, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST:
                if (!PermissionUtils.hasSelfPermissions(target, PERMISSIONS)) {
                    target.finish();
                }else
                {
                    //target.initLogin();
                }
                break;
            default:
                break;
        }
    }
}
