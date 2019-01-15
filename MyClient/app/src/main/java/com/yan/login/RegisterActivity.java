package com.yan.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyan
 * @date 2019/1/10
 */

public class RegisterActivity extends Activity implements View.OnClickListener {

    private Button loginRegister;
    private EditText loginUser;
    private EditText loginPassword;
    private EditText loginPassword1;
    private EditText loginPhone;
    private String sex;
    private String city;
    private String[] str = null;

    private TextView textView;
    private Spinner spinner;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;
    
    private static final String URLREGISTER = "xxx/register/json/data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

       //控件绑定
        init();

        //注册按钮
        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = loginUser.getText().toString();
                String password = loginPassword.getText().toString();
                String password1 = loginPassword1.getText().toString();
                String phone = loginPhone.getText().toString();

                        if (TextUtils.isEmpty(user)) {
                            //用户名为空
                            Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(password)) {
                            Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(password1)) {
                            Toast.makeText(RegisterActivity.this, "请确认密码",Toast.LENGTH_SHORT).show();
                        } else if (!password.equals(password1)) {
                            Toast.makeText(RegisterActivity.this, "两次密码不一样，请验证", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(phone)) {
                            Toast.makeText(RegisterActivity.this, "请输入电话", Toast.LENGTH_SHORT).show();
                        } else {
                            str= new String[]{user, password1};

                            Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    switch (msg.what) {
                                        case 0:
                                            Toast.makeText(RegisterActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 1: Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                            //注册成功跳转到登录页面
                                            startActivity( new Intent(RegisterActivity.this, LoginActivity.class));
                                            RegisterActivity.this.finish();
                                            break;
                                        case 2:
                                            Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 3:
                                            Log.e("input error", "url为空");
                                            break;
                                        case 4:Toast.makeText(RegisterActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                    }
                                }
                            };
                            OperateData operateData = new OperateData();
                            String jsonString = operateData.stringTojson(str);
                            URL url = null;
                            try {
                                url = new URL(URLREGISTER);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            operateData.sendData(jsonString, handler, url);
                        }


                //保存数据到SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences("register", MODE_PRIVATE).edit();
                editor.putString("User", user);
                editor.putString("password", password);
                editor.putString("password1", password1);
                editor.putString("phone", phone);
                editor.putString("Sex", sex);
                editor.putString("City", city);
                editor.commit();
            }
        });

        //RadioGroup 性别选择
        RadioGroup radgroup = (RadioGroup) findViewById(R.id.radioGroup);
        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                Toast.makeText(getApplicationContext(), "你选了" + radbtn.getText().toString(), Toast.LENGTH_LONG).show();
                sex = radbtn.getText().toString();
            }
        });


        //Spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        textView = (TextView) findViewById(R.id.un);

        dataList = new ArrayList<String>();
        dataList.add("北京");
        dataList.add("上海");
        dataList.add("广州");
        dataList.add("深圳");
        dataList.add("武汉");
        dataList.add("杭州");
        dataList.add("南京");
        dataList.add("成都");
        dataList.add("黄冈");
        dataList.add("呼和浩特");


        /*为spinner定义适配器，也就是将数据源存入adapter，这里需要三个参数
        1. 第一个是Context（当前上下文），这里就是this
        2. 第二个是spinner的布局样式，这里用android系统提供的一个样式
        3. 第三个就是spinner的数据源，这里就是dataList*/
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataList);

        //style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        spinner.setAdapter(adapter);

        //城市选择
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView.setText("City：" + adapter.getItem(position));
                city = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText("请选择您的城市");

            }

        });
    }


    /**
     *
     * 字符数组转json
     */
    private String stringArraytoJson(String[] strings) {

        if (strings==null){return "";}
        String js = "[{"+"username:"+strings[0]+"password:"+strings[1];

        return js;
    }
    
    /**
     * 绑定控件
     */
    private void init() {
        loginRegister = findViewById(R.id.L_register);
        loginUser = findViewById(R.id.L_user);
        loginPassword = findViewById(R.id.L_password);
        loginPassword1 = findViewById(R.id.L_password1);
        loginPhone = findViewById(R.id.L_phone);
    }


    @Override
    public void onClick(View v) {

    }

    }





