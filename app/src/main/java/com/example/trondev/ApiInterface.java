package com.example.trondev;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("/user/{name}.json")
    Call<JsonObject> getUserData(@Path("name") String name);
}
