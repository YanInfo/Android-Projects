
package com.yan.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author zhangyan
 * @data 2019/1/10
 */
public class LoginActivity extends Activity {

    private EditText user;
    private EditText password;
    private Button login;
    private Button register;
    private SharedPreferences pref;
    private CheckBox rembemberPass;
    public static final String TAG = "LoginActivity";
    private static final String URLLOGIN = "xxx/login/json/data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        //绑定控件
        init();

        //记住密码
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String user1 = pref.getString("user", "");
            String password1 = pref.getString("password", "");
            user.setText(user1);
            password.setText(password1);
            rembemberPass.setChecked(true);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override

            //登录按键的响应
            public void onClick(View v) {

                String[] data = null;
                String inputUser = user.getText().toString();
                String inputPassword = password.getText().toString();

                if (TextUtils.isEmpty(inputUser)) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(inputPassword)) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    data = new String[]{inputUser, inputPassword};
                    @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case 0:
                                    Toast.makeText(LoginActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, SuccessActivity.class));
                                    LoginActivity.this.finish();
                                    break;
                                case 2:
                                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Log.e("input error", "url为空");
                                    break;
                                case 4:
                                    Toast.makeText(LoginActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                            }
                        }
                    };
                    OperateData operateData = new OperateData();
                    String jsonString = operateData.stringTojson(data);
                    URL url = null;
                    try {
                        url = new URL(URLLOGIN);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    operateData.sendData(jsonString, handler, url);

                }

            }
        });


        /**
         * 跳转到注册页面
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        /**
         * 初始化
         */
        private void init () {
            login = findViewById(R.id.login);
            register = findViewById(R.id.register);
            user = findViewById(R.id.user);
            password = findViewById(R.id.password);
            rembemberPass = findViewById(R.id.remember);
        }

    }
}
