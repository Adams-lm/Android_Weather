package com.hznu.lin.project.service;


import com.hznu.lin.project.entity.LoginBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetRequestService {
    @GET("/api/user/json")
    Call<LoginBean> login(@Query("username") String username, @Query("password") String password);

    @GET("weather_mini")
    Call<ResponseBody> getWeather(@Query("city") String city);

}
