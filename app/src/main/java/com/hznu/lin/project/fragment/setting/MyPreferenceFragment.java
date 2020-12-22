package com.hznu.lin.project.fragment.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.hznu.lin.project.R;
import com.hznu.lin.project.service.impl.GetRequestServiceImpl;
import com.hznu.lin.project.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author LIN
 * @date 2020/12/20 19:13
 */
public class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private Preference login;
    private Preference username;
    private Preference password;
    private Preference city;
    private Preference confirm;
    private Preference datPast;
    private Preference dayFuture;
    private String passwordStr = "123";
    private String oldCity = "杭州";

    private SharedPreferences.Editor editor;
    private SharedPreferences sp;

    public MyPreferenceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spInit();
        // 设置页面绑定
        addPreferencesFromResource(R.xml.setting_preference);
        init();
    }

    /**
     * sp初始化
     */
    private void spInit() {
        sp = getActivity().getSharedPreferences("com.hznu.lin.project_preferences", Context.MODE_PRIVATE);
        passwordStr = sp.getString("password", passwordStr);
        oldCity = sp.getString("city", oldCity);
        editor = sp.edit();
        editor.putString("confirm", "");
        editor.commit();
    }

    /**
     * Preference初始化
     */
    public void init() {
        login = findPreference("login");
        username = findPreference("username");
        password = findPreference("password");
        city = findPreference("city");
        confirm = findPreference("confirm");
        datPast = findPreference("day_past");
        dayFuture = findPreference("day_future");
        login.setOnPreferenceChangeListener(this);
        username.setOnPreferenceChangeListener(this);
        password.setOnPreferenceChangeListener(this);
        city.setOnPreferenceChangeListener(this);
        confirm.setOnPreferenceChangeListener(this);
        datPast.setOnPreferenceChangeListener(this);
        dayFuture.setOnPreferenceChangeListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case "login":
                ToastUtil.showToast(getContext(), "自动登录设置修改成功", Toast.LENGTH_SHORT);
                break;
            case "username":
                ToastUtil.showToast(getContext(), "用户名修改成功", Toast.LENGTH_SHORT);
                break;
            case "password":
                ToastUtil.showToast(getContext(), "密码修改成功", Toast.LENGTH_SHORT);
                break;
            case "city":
                isValidCity(newValue.toString());
                break;
            case "confirm":
                String confirmStr = newValue.toString();
                if (confirmStr.equals(passwordStr)) {
                    username.setEnabled(true);
                    password.setEnabled(true);
                    confirm.setEnabled(false);
                    ToastUtil.showToast(getContext(), "密码验证成功", Toast.LENGTH_SHORT);
                } else {
                    ToastUtil.showToast(getContext(), "密码验证失败", Toast.LENGTH_SHORT);
                }
                break;
            case "day_past":
                ToastUtil.showToast(getContext(), "历史天数修改为" + newValue + "天", Toast.LENGTH_SHORT);
                break;
            case "day_future":
                ToastUtil.showToast(getContext(), "未来天数修改为" + newValue + "天", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 判断城市名是否合法
     * @param city
     */
    public void isValidCity(String city) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                GetRequestServiceImpl getRequestService = new GetRequestServiceImpl();
                Call<ResponseBody> call = getRequestService.getWeather(city);
                try {
                    Response<ResponseBody> response = call.execute();
                    String jsonStr = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jsonObject.getJSONObject("data");

                    Looper.prepare();
                    ToastUtil.showToast(getActivity(), "默认城市已修改为" + city, Toast.LENGTH_LONG);
                    Looper.loop();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    Looper.prepare();
                    ToastUtil.showToast(getActivity(), "城市名无效，请输入正确的城市名！", Toast.LENGTH_SHORT);
                    editor.putString("city", oldCity);
                    editor.commit();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
