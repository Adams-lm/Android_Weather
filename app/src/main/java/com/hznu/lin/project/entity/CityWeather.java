package com.hznu.lin.project.entity;

/**
 * @author LIN
 * @date 2020/12/19 20:06
 */
public class CityWeather {
    private String city;
    private String weather;
    private String temperature;

    public CityWeather(String city, String weather, String temperature) {
        this.city = city;
        this.weather = weather;
        this.temperature = temperature;
    }

    public CityWeather() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "CityWeather{" +
                "city='" + city + '\'' +
                ", weather='" + weather + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }
}
