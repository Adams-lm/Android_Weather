package com.hznu.lin.project.fragment.setting;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    public Preference login;
    public Preference username;
    public Preference password;
    public Preference city;

    public MyPreferenceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置页面绑定
        addPreferencesFromResource(R.xml.setting_preference);
        init();
    }

    public void init() {
        login = findPreference("login");
        username = findPreference("username");
        password = findPreference("password");
        city = findPreference("city");
        login.setOnPreferenceChangeListener(this);
        username.setOnPreferenceChangeListener(this);
        password.setOnPreferenceChangeListener(this);
        city.setOnPreferenceChangeListener(this);
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
                ToastUtil.showToast(getContext(), "默认城市修改成功", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
        return true;
    }

}
