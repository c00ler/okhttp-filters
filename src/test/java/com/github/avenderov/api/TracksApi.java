package com.github.avenderov.api;

import com.github.avenderov.model.Track;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TracksApi {

    @GET("/tracks/{id}")
    Call<Track> getById(@Path("id") String id);

}
