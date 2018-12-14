package com.example.isslocator.dto;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocatorResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("request")
    @Expose
    private Request request;
    @SerializedName("response")
    @Expose
    @Nullable
    private List<Response> response = null;

    public String getMessage() {
        return message;
    }

    public Request getRequest() {
        return request;
    }

    @Nullable
    public List<Response> getResponse() {
        return response;
    }
}
