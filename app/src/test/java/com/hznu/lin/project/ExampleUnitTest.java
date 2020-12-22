package com.hznu.lin.project;

import androidx.room.Room;

import com.baidu.mapapi.common.SysOSUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hznu.lin.project.db.TestDataBase;
import com.hznu.lin.project.entity.Weather;
import com.hznu.lin.project.entity.WeatherEntity;
import com.hznu.lin.project.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void db() {
        SimpleDateFormat formatter= new SimpleDateFormat("MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String today = formatter.format(date);
        System.out.println(today);

    }
}