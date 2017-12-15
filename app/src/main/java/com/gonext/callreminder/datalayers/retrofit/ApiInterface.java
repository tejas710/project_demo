package com.gonext.callreminder.datalayers.retrofit;


import com.gonext.callreminder.datalayers.model.AdDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/app/getAds")
    Call<AdDataResponse> getServerAds(@Query("appId") String Options);
}


