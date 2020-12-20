package com.hznu.lin.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hznu.lin.project.R;
import com.hznu.lin.project.entity.CityWeather;

import java.util.List;

/**
 * @author LIN
 * @date 2020/12/19 20:18
 */
public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.ViewHolder> {

    private List<CityWeather> cityWeatherList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivWeather;
        TextView tvCity;
        TextView tvTemperature;

        public ViewHolder(View view) {
            super(view);
            ivWeather = view.findViewById(R.id.iv_weather);
            tvCity = view.findViewById(R.id.tv_city);
            tvTemperature = view.findViewById(R.id.tv_temperature);
        }
    }

    public CityWeatherAdapter(List<CityWeather> fruitList) {
        cityWeatherList = fruitList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_weather_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityWeather cityWeather = cityWeatherList.get(position);
        int resource;
        switch(cityWeather.getWeather()){
            case "多云":
                resource=R.drawable.cloudy_grey;
                break;
            case "小雨":
                resource=R.drawable.rainy_grey;
                break;
            case "晴":
                resource=R.drawable.sunny_grey;
                break;
            case "阴":
            default:
                resource=R.drawable.default_grey;
        }
        holder.ivWeather.setImageResource(resource);
        holder.tvCity.setText(cityWeather.getCity());
        holder.tvTemperature.setText(cityWeather.getTemperature());
    }


    @Override
    public int getItemCount() {
        return cityWeatherList.size();
    }
}
