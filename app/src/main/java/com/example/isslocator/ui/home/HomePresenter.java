package com.example.isslocator.ui.home;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.example.isslocator.common.LocationManager;
import com.example.isslocator.common.PermissionsManager;
import com.example.isslocator.dto.Response;
import com.example.isslocator.model.Result;
import com.example.isslocator.net.LocatorService;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;


import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private final PermissionsManager permissionsManager;
    private final LocationManager locationManager;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final LocatorService locatorService;
    private final HomeContract.View view;

    public HomePresenter(PermissionsManager permissionsManager, LocationManager locationManager,
                         LocatorService locatorService, HomeContract.View view) {
        this.permissionsManager = permissionsManager;
        this.locationManager = locationManager;
        this.locatorService = locatorService;
        this.view = view;
        setupSubscriptions();
    }

    @Override
    public void stop() {
        compositeDisposable.clear();
    }

    @Override
    public void onPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void getLocation() {
        requestPermissions();
    }

    private void setupSubscriptions() {
        compositeDisposable.add(permissionsManager.getPermissionGrantObservable()
                .subscribe(hasPermissions -> {
                    if (hasPermissions) {
                        getLastKnownLocation();
                    } else {
                        onPermissionsDenied();
                    }
                }));

        compositeDisposable.add(locationManager.getLocationObservable()
                .subscribe(this::onLocationAcquired));
    }

    private void requestPermissions() {
       permissionsManager.getRequiredPermissions();
    }

    private void getLastKnownLocation() {
        locationManager.getLastKnownLocation();
    }

    private void onLocationAcquired(Pair<Double, Double> locationPair) {
        compositeDisposable.add(locatorService.getISSPasses(locationPair.first, locationPair.second)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .filter(locatorResponse -> locatorResponse.getResponse() != null)
                .flatMap(locatorResponse -> Observable.fromIterable(locatorResponse.getResponse()))
                .map(response -> getResultFromResponse(response, locationPair))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> view.showProgress())
                .doOnEvent((success, failure) -> view.hideProgress())
                .subscribe(view::showResults, this::handleError));
    }

    private Result getResultFromResponse(Response response, Pair<Double, Double> locationPair) {
        return new Result(String.valueOf(response.getDuration()),
                LocalDateTime.ofInstant(Instant.ofEpochSecond(response.getRisetime()), ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.getDefault())),
                String.valueOf(locationPair.first) + " | " + String.valueOf(locationPair.second));
    }


    private void onPermissionsDenied() {
        view.showError("Your location cannot be detected without your permission");
    }

    private void handleError(@NonNull Throwable throwable) {
        if (throwable.getMessage() != null) {
            view.showError(throwable.getMessage());
        }
    }


}
