package com.example.isslocator.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Pair;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class LocationManager {

    private final Activity activity;
    private final PublishSubject<Pair<Double, Double>> locationObservable = PublishSubject.create();
    private final FusedLocationProviderClient locationProviderClient;

    public LocationManager(Activity activity) {
        this.activity = activity;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public Observable<Pair<Double, Double>> getLocationObservable() {
        return locationObservable;
    }

    @SuppressLint("MissingPermission")
    public void getLastKnownLocation() {
        locationProviderClient.getLastLocation().addOnSuccessListener(activity, location -> {
            if (location != null) {
                locationObservable.onNext(new Pair<>(location.getLatitude(), location.getLongitude()));
            }
        });
    }

}
