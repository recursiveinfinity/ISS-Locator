package com.example.isslocator.di;

import android.app.Application;

import com.example.isslocator.ui.home.di.HomeComponent;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = NetModule.class)
public interface AppComponent {

    HomeComponent.Builder homeComponentBuilder();

    @Component.Builder
    interface Builder {
        AppComponent build();
        @BindsInstance Builder application(@ApplicationContext Application application);
    }
}
