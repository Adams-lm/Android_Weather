package com.hznu.lin.project.fragment.setting;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hznu.lin.project.R;
import com.hznu.lin.project.fragment.weather.TodayFragment;
import com.hznu.lin.project.service.impl.GetRequestServiceImpl;
import com.hznu.lin.project.util.HttpUtil;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.btn_city, R.id.btn_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_city:
                String city = etCity.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GetRequestServiceImpl getRequestService = new GetRequestServiceImpl();
                        Call<ResponseBody> call = getRequestService.getWeather(city);
                        try {
                            Response<ResponseBody> response = call.execute();
                            String jsonStr = response.body().string();
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            JSONObject data = jsonObject.getJSONObject("data");
                            // 将json传到handle中进行操作
                        } catch (IOException e) {
                            ToastUtil.showToast(getContext(), "系统异常，请稍后再试", Toast.LENGTH_LONG);
                            e.printStackTrace();
                        } catch (JSONException e) {
                            ToastUtil.showToast(getContext(), "城市名不合法，请输入正确的城市名！" + city, Toast.LENGTH_LONG);
                            e.printStackTrace();
                        }
                    }
                }).start();
                TodayFragment.defaultCity = city;
                ToastUtil.showToast(getContext(), "默认城市已修改为" + city, Toast.LENGTH_LONG);
                break;
            case R.id.btn_reset:
                TodayFragment.defaultCity = "杭州";
                ToastUtil.showToast(getContext(), "默认城市已重置为" + "杭州", Toast.LENGTH_LONG);
                break;
        }
    }
}