package com.hznu.lin.project.fragment.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hznu.lin.project.R;
import com.hznu.lin.project.service.impl.GetRequestServiceImpl;
import com.hznu.lin.project.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SettingFragment extends Fragment {


    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.btn_city)
    Button btnCity;
    @BindView(R.id.btn_reset)
    Button btnReset;

    private SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    // 初始化
    @SuppressLint("CommitPrefEdits")
    public void init() {
        // SharedPreferences
        SharedPreferences sp = getActivity().getSharedPreferences("SP", Context.MODE_PRIVATE);
        editor = sp.edit();
        etCity.setHint(sp.getString("defaultCity", "杭州"));
    }

    // btn响应
    @OnClick({R.id.btn_city, R.id.btn_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_city:
                String city = etCity.getText().toString();
                if (city.equals("")) {
                    city = etCity.getHint().toString();
                }
                String finalCity = city;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GetRequestServiceImpl getRequestService = new GetRequestServiceImpl();
                        Call<ResponseBody> call = getRequestService.getWeather(finalCity);
                        try {
                            Response<ResponseBody> response = call.execute();
                            String jsonStr = response.body().string();

                            Message msg = Message.obtain();
                            Bundle b = new Bundle();
                            b.putString("jsonStr", jsonStr);
                            b.putString("city", finalCity);
                            msg.setData(b);
                            // 将json传到handle中进行操作
                            handlerWeather.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.btn_reset:
                etCity.setHint("杭州");
                ToastUtil.showToast(getContext(), "默认城市已重置为" + "杭州", Toast.LENGTH_LONG);
                break;
        }
    }

    // 判断城市名是否合法
    @SuppressLint("HandlerLeak")
    private Handler handlerWeather = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle dataMsg = msg.getData();
            String jsonStr = dataMsg.getString("jsonStr");
            String city = dataMsg.getString("city");
            // Json处理
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                jsonObject.getJSONObject("data");
                etCity.setText("");
                etCity.setHint(city);
                ToastUtil.showToast(getContext(), "默认城市已修改为" + city, Toast.LENGTH_LONG);
            } catch (JSONException e) {
                // 发生异常说明城市名不合法
                ToastUtil.showToast(getContext(), "城市名无效，请输入正确的城市名！", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };

    // 结束时存储默认城市
    @Override
    public void onStop() {
        super.onStop();
        String city = etCity.getHint().toString();
        editor.putString("defaultCity", city);
        editor.commit();
    }
}