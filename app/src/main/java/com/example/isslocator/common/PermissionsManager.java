package com.example.isslocator.common;


import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.isslocator.R;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionsManager {

    private static final int PERMISSIONS_REQUEST_CODE = 101;
    private static final String[] LIST_OF_REQUIRED_PERMISSIONS =
            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private final Activity activity;

    private final PublishSubject<Boolean> permissionGrant = PublishSubject.create();

    public PermissionsManager(Activity activity) {
        this.activity = activity;
    }

    public Observable<Boolean> getPermissionGrantObservable() {
        return permissionGrant;
    }

    @AfterPermissionGranted(PERMISSIONS_REQUEST_CODE)
    public void getRequiredPermissions() {
        if (EasyPermissions.hasPermissions(activity, LIST_OF_REQUIRED_PERMISSIONS)) {
            permissionGrant.onNext(true);
        } else {
            EasyPermissions.requestPermissions(activity,
                    activity.getString(R.string.permissions_rationale),
                    PERMISSIONS_REQUEST_CODE, LIST_OF_REQUIRED_PERMISSIONS);
        }
    }

    public void onPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
