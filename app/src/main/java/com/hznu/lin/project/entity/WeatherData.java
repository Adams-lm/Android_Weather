package com.hznu.lin.project.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * @author LIN
 * @date 2020/12/22 9:36
 */
@Entity(tableName = "weatherData")
public class WeatherData {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "low")
    private String low;
    @ColumnInfo(name = "high")
    private String high;

    @Ignore
    public WeatherData(int id, String date, String low, String high) {
        this.id = id;
        this.date = date;
        this.low = low;
        this.high = high;
    }

    public WeatherData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", low='" + low + '\'' +
                ", high='" + high + '\'' +
                '}';
    }
}
