package com.example.isslocator.ui.home.di;

import com.example.isslocator.ui.home.HomeActivity;

import dagger.Subcomponent;

@HomeScope
@Subcomponent(modules = HomeModule.class)
public interface HomeComponent {
    void inject(HomeActivity homeActivity);

    @Subcomponent.Builder
    interface Builder {
        Builder homeModule(HomeModule homeModule);
        HomeComponent build();
    }
}
