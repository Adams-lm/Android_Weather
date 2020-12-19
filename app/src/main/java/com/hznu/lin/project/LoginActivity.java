package com.hznu.lin.project;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

    private final static String USERNAME = "LIN";
    private final static String PASSWORD = "123456";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.checkbox_remember, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.checkbox_remember:
                boolean checked = checkboxRemember.isChecked();
                if (checked) {
                    etUserName.setText(USERNAME);
                    etPassword.setText(PASSWORD);
                } else {
                    etUserName.setText("");
                    etPassword.setText("");
                }
                break;
            case R.id.btn_login:
                String username = etUserName.getText().toString();
                String pwd = etPassword.getText().toString();
                if (username.equals("") || pwd.equals("")) {
                    ToastUtil.showToast(getApplicationContext(), "请输入用户名或密码", Toast.LENGTH_SHORT);
                    break;
                }
                if (username.equals(USERNAME) && pwd.equals(PASSWORD)) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.showToast(getApplicationContext(), "密码错误，请重新输入", Toast.LENGTH_SHORT);
                }
                break;
        }
    }
}