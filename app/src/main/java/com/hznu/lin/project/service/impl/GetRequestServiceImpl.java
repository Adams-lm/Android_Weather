package com.hznu.lin.project.service.impl;

import com.hznu.lin.project.entity.LoginBean;
import com.hznu.lin.project.service.GetRequestService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author LIN
 * @date 2020/12/13 19:46
 */
public class GetRequestServiceImpl implements GetRequestService {

    @Override
    public Call<LoginBean> login(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://travelstar.top:8888")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<LoginBean> call = retrofit.create(GetRequestService.class)
                .login(username, password);

        return call;
    }

    @Override
    public Call<ResponseBody> getWeather(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://wthrcdn.etouch.cn")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<ResponseBody> call = retrofit.create(GetRequestService.class)
                .getWeather(city);

        return call;
    }
}
