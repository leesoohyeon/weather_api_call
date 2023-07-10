package com.example.weather_api_call;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Retrofit_Service {

    @GET("v2/local/geo/coord2regioncode.json")
    Call<KakaoData> serchAddressList(@Query("x") String x, @Query("y") String y, @Header("Authorization") String apikey);
}
