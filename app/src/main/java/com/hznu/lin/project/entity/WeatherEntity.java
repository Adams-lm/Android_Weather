package com.hznu.lin.project.entity;

/**
 * @author LIN
 * @date 2020/12/21 23:51
 */
public class WeatherEntity {
    private String days;
    private String temp_low;
    private String temp_high;

    public WeatherEntity() {
    }

    public WeatherEntity(String days, String temp_low, String temp_high) {
        this.days = days;
        this.temp_low = temp_low;
        this.temp_high = temp_high;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTemp_low() {
        return temp_low;
    }

    public void setTemp_low(String temp_low) {
        this.temp_low = temp_low;
    }

    public String getTemp_high() {
        return temp_high;
    }

    public void setTemp_high(String temp_high) {
        this.temp_high = temp_high;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "days='" + days + '\'' +
                ", temp_low='" + temp_low + '\'' +
                ", temp_high='" + temp_high + '\'' +
                '}';
    }
}
