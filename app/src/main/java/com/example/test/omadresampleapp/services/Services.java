package com.example.test.omadresampleapp.services;

import com.example.test.omadresampleapp.model.Nurses;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

public interface Services {

    @POST("/nurses/login/")
    public void loginNurse(@Header("Authorization") String authHeader, Callback<Response> callback);

    @GET("/nurses/")
    public void getNurse(Callback<Nurses> callback);

    @POST("/nurses/")
    public void createNurse(@Body Nurses nurses, Callback<Response> callback);

}
