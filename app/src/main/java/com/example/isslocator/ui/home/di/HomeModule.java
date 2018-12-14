package com.example.isslocator.ui.home.di;

import com.example.isslocator.ui.home.HomeActivity;
import com.example.isslocator.common.LocationManager;
import com.example.isslocator.common.PermissionsManager;
import com.example.isslocator.net.LocatorService;
import com.example.isslocator.ui.home.HomeContract;
import com.example.isslocator.ui.home.HomePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    private final HomeActivity homeActivity;

    public HomeModule(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Provides
    @HomeScope
    public LocationManager provideLocationManager() {
        return new LocationManager(homeActivity);
    }

    @Provides
    @HomeScope
    public PermissionsManager providePermissionsManager() {
        return new PermissionsManager(homeActivity);
    }


    @Provides
    @HomeScope
    public HomeContract.Presenter provideHomePresenter(PermissionsManager permissionsManager,
                                                       LocationManager locationManager,
                                                       LocatorService locatorService) {
        return new HomePresenter(permissionsManager, locationManager, locatorService, homeActivity);
    }
}
