package com.hznu.lin.project.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hznu.lin.project.dao.WeatherDataDao;
import com.hznu.lin.project.entity.WeatherData;

/**
 * @author LIN
 * @date 2020/12/22 9:35
 */
@Database(entities = {WeatherData.class}, version = 1)
public abstract class WeatherDataBase extends RoomDatabase {
    public abstract WeatherDataDao weatherDataDao();

}
