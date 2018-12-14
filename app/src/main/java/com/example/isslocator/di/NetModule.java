package com.example.isslocator.di;

import android.app.Application;

import com.example.isslocator.net.LocatorService;
import com.example.isslocator.ui.home.di.HomeComponent;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.isslocator.common.Constants.BASE_URL;
import static com.example.isslocator.common.Constants.CONNECT_TIMEOUT;
import static com.example.isslocator.common.Constants.OK_HTTP_CACHE_LIMIT;
import static com.example.isslocator.common.Constants.READ_TIMEOUT;


@Module(subcomponents = HomeComponent.class)
class NetModule {

    @Provides
    @Singleton
    Cache provideOkHttpCache(@ApplicationContext Application application) {
        return new Cache(application.getCacheDir(), OK_HTTP_CACHE_LIMIT);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofitClient(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    LocatorService provideLocatorService(Retrofit retrofit) {
        return retrofit.create(LocatorService.class);
    }
}
