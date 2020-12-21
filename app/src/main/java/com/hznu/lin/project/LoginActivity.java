package com.hznu.lin.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.hznu.lin.project.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_userName)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.checkbox_remember)
    CheckBox checkboxRemember;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forget)
    TextView tvForget;

    private static String username = "user";
    private static String password = "123";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();

    }

    public void init() {
        SharedPreferences sp = getApplication().getSharedPreferences("com.hznu.lin.project_preferences", Context.MODE_PRIVATE);
        boolean login = sp.getBoolean("login", false);
        username = sp.getString("username", username);
        password = sp.getString("password", password);
        // 自动登录
        if (login) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnClick({R.id.checkbox_remember, R.id.btn_login, R.id.tv_forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.checkbox_remember:
                boolean checked = checkboxRemember.isChecked();
                if (checked) {
                    etUserName.setText(username);
                    etPassword.setText(password);
                } else {
                    etUserName.setText("");
                    etPassword.setText("");
                }
                break;
            case R.id.tv_forget:
                etUserName.setText(username);
                etPassword.setText(password);
                ToastUtil.showToast(getApplicationContext(), "用户名：" + username + "\n密码：" + password, Toast.LENGTH_SHORT);
                break;
            case R.id.btn_login:
                String user = etUserName.getText().toString();
                String pwd = etPassword.getText().toString();
                if (user.equals("") || pwd.equals("")) {
                    ToastUtil.showToast(getApplicationContext(), "请输入用户名或密码", Toast.LENGTH_SHORT);
                    break;
                }
                if (user.equals(username) && pwd.equals(password)) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    ToastUtil.showToast(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT);
                } else {
                    ToastUtil.showToast(getApplicationContext(), "密码错误，请重新输入", Toast.LENGTH_SHORT);
                }
                break;
        }
    }

}