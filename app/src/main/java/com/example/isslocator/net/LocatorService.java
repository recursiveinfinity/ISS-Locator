package com.example.isslocator.net;

import com.example.isslocator.common.Constants;
import com.example.isslocator.dto.LocatorResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocatorService {
    @GET(Constants.ISS_LOCATOR_ENDPOINT)
    Single<LocatorResponse> getISSPasses(@Query("lat") Double latitude, @Query("lon") Double longitude);
}
