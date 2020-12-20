package com.hznu.lin.project.fragment.weather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hznu.lin.project.R;
import com.hznu.lin.project.adapter.CityWeatherAdapter;
import com.hznu.lin.project.entity.CityWeather;
import com.hznu.lin.project.entity.Weather;
import com.hznu.lin.project.service.impl.GetRequestServiceImpl;
import com.hznu.lin.project.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class TodayFragment extends Fragment {
    @BindView(R.id.iv_weatherImg)
    ImageView ivWeatherImg;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_windPower)
    TextView tvWindPower;
    @BindView(R.id.tv_windDt)
    TextView tvWindDt;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<CityWeather> weatherList = new ArrayList<>();
    private String[] citys = {"杭州", "北京", "天津", "福州", "厦门", "宁波", "温州"};
    public static String defaultCity = "杭州";

    public TodayFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, view);

        // recyclerView初始化
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTodayWeather(defaultCity);
        initWeatherList();
    }

    public void initWeatherList() {
        for (String city : citys) {
            getCityWeather(city);
        }
        // 直到城市天气都获取到后再初始化recyclerView
        while (weatherList.size() != citys.length) ;
        CityWeatherAdapter adapter = new CityWeatherAdapter(weatherList);
        recyclerView.setAdapter(adapter);
    }


    // 获取当天天气
    public void getTodayWeather(String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetRequestServiceImpl getRequestService = new GetRequestServiceImpl();
                Call<ResponseBody> call = getRequestService.getWeather(city);
                try {
                    Response<ResponseBody> response = call.execute();
                    String jsonStr = response.body().string();
                    // 将json传到handle中进行操作
                    Message msg = Message.obtain();
                    Bundle b = new Bundle();
                    b.putString("jsonStr", jsonStr);
                    msg.setData(b);
                    handlerWeather.sendMessage(msg);
                } catch (IOException e) {
                    ToastUtil.showToast(getContext(), "系统异常，请稍后再试", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 获取当天天气handle
    @SuppressLint("HandlerLeak")
    private Handler handlerWeather = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle dataMsg = msg.getData();
            String jsonStr = dataMsg.getString("jsonStr");
            // Json处理
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONObject data = jsonObject.getJSONObject("data");

                String city = data.getString("city");
                JSONArray forecastArray = data.getJSONArray("forecast");
                // 只获取当日天气信息
                JSONObject weatherObject = forecastArray.getJSONObject(0);
                Weather weather = new Gson().fromJson(weatherObject.toString(), Weather.class);
//                Log.i("WEATHER", weather.toString());
                String temperature = weather.getLow().substring(3) + " ~ " + weather.getHigh().substring(3);
                String windPower = "风力：" + weather.getFengli().charAt(9) + "级";
                String windDt = "风向：" + weather.getFengxiang();
                int resource;
                switch (weather.getType()) {
                    case "多云":
                        resource = R.drawable.cloudy;
                        break;
                    case "小雨":
                        resource = R.drawable.rainy;
                        break;
                    case "晴":
                        resource = R.drawable.sunny;
                        break;
                    case "阴":
                    default:
                        resource = R.drawable.default_weather;
                }
                ivWeatherImg.setImageResource(resource);
                // UI处理

                tvTemperature.setText(temperature);
                tvType.setText(weather.getType());
                tvWindPower.setText(windPower);
                tvWindDt.setText(windDt);
                tvCity.setText(city);
            } catch (JSONException e) {
                ToastUtil.showToast(getContext(), "系统异常，请稍后再试", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };

    private void getCityWeather(String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetRequestServiceImpl getRequestService = new GetRequestServiceImpl();
                Call<ResponseBody> call = getRequestService.getWeather(city);
                try {
                    Response<ResponseBody> response = call.execute();
                    String jsonStr = response.body().string();

                    // json处理
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject data = jsonObject.getJSONObject("data");
                    // 只获取当日天气信息
                    JSONArray forecastArray = data.getJSONArray("forecast");
                    JSONObject weatherObject = forecastArray.getJSONObject(0);
                    Weather weather = new Gson().fromJson(weatherObject.toString(), Weather.class);
                    String temperature = weather.getLow().substring(3) + " ~ " + weather.getHigh().substring(3);
                    CityWeather cityWeather = new CityWeather(city, weather.getType(), temperature);
//                    Log.i("WEATHER", cityWeather.toString() + "=============");
                    weatherList.add(cityWeather);
                } catch (IOException e) {
                    ToastUtil.showToast(getContext(), "IOException", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                } catch (JSONException e) {
                    ToastUtil.showToast(getContext(), "JSONException", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        }).start();
    }

}