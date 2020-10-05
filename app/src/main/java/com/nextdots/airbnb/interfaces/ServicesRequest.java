package com.nextdots.airbnb.interfaces;

import com.nextdots.airbnb.models.ResDetail;
import com.nextdots.airbnb.models.ResHotel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mariexi on 04/12/2016.
 */
public interface ServicesRequest {

    @GET("/v2/search_results?client_id=3092nxybyb0otqw18e8nh5nty")
    Call<ResHotel> getListSearch(@Query("_limit") int limit, @Query("location") String city);

    @GET("/v2/listings/{id}?client_id=3092nxybyb0otqw18e8nh5nty&_format=v1_legacy_for_p3")
    Call<ResDetail> getListDetails(@Path("id") int id
    );


}
