package com.hznu.lin.project;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.baidu.mapapi.SDKInitializer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hznu.lin.project.dao.WeatherDataDao;
import com.hznu.lin.project.db.WeatherDataBase;
import com.hznu.lin.project.entity.Weather;
import com.hznu.lin.project.entity.WeatherData;
import com.hznu.lin.project.entity.WeatherEntity;
import com.hznu.lin.project.fragment.history.HistoryFragment;
import com.hznu.lin.project.fragment.weather.TodayFragment;
import com.hznu.lin.project.service.impl.GetRequestServiceImpl;
import com.hznu.lin.project.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 城市天气列表初始化
        TodayFragment.weatherList.clear();
        for (String city : TodayFragment.citys) {
            TodayFragment.getCityWeather(city);
        }

        // HelloChart数据初始化
        WeatherPastInit();
        WeatherForecastInit();

        // BaiDu地图SDK初始化
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        // BottomNavigationView初始化
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_weather, R.id.navigation_history, R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    /**
     * 未来天气数据初始化
     */
    private void WeatherForecastInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonStr = HttpUtil.get("http://api.k780.com/?app=weather.future&weaid=%E6%9D%AD%E5%B7%9E&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json", null);
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    List<WeatherEntity> weatherList = new Gson().fromJson(jsonArray.toString(),
                            new TypeToken<List<WeatherEntity>>() {
                            }.getType());
                    // 查询未来7天天气
                    for (int i = 0; i < weatherList.size(); i++) {
                        HistoryFragment.dateFuture[i] = weatherList.get(i).getDays().substring(5);
                        HistoryFragment.dateFuture[i] = HistoryFragment.dateFuture[i].replace('-', '/');
                        HistoryFragment.tempLowFuture[i] = Integer.parseInt(weatherList.get(i).getTemp_low());
                        HistoryFragment.tempHighFuture[i] = Integer.parseInt(weatherList.get(i).getTemp_high());
                        Log.i("WEATHER", HistoryFragment.dateFuture[i]);
                    }
                    // 因为这个api只提供未来6天数据，所以第7天数据这里选择复制第五天的数据
                    String month = HistoryFragment.dateFuture[5].substring(0, 2);
                    String dayStr = HistoryFragment.dateFuture[5].substring(3, 5);
                    int day = Integer.parseInt(dayStr) + 1;
                    HistoryFragment.dateFuture[6] = month + "/" + day;
                    HistoryFragment.tempLowFuture[6] = HistoryFragment.tempLowFuture[3];
                    HistoryFragment.tempHighFuture[6] = HistoryFragment.tempHighFuture[3];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 历史天气数据初始化
     */
    private void WeatherPastInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String city = "杭州";
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
                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
                    Date date = new Date(System.currentTimeMillis());
                    String today = formatter.format(date);
                    String low = weather.getLow();
                    String high = weather.getHigh();
                    // 截取字符串
                    low = low.substring(3, low.length() - 1);
                    high = high.substring(3, high.length() - 1);
                    WeatherData weatherData = new WeatherData();
                    weatherData.setDate(today);
                    weatherData.setLow(low);
                    weatherData.setHigh(high);
                    // sqlite
                    WeatherDataBase weatherDataBase = Room.databaseBuilder(getApplicationContext(), WeatherDataBase.class, "WeatherDataBase.db").allowMainThreadQueries().build();
                    WeatherDataDao weatherDataDao = weatherDataBase.weatherDataDao();
                    WeatherData sqlData = weatherDataDao.findByDate(today);
                    // 如果今天数据还没记录，则写入数据库
                    if (sqlData == null) {
                        weatherDataDao.insertAll(weatherData);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}