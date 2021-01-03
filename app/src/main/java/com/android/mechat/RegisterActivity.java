package com.android.mechat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mechat.DB.AddressDBService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * 注册
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText mRegisterName, mRegisterPass, mRegisterPassAga, mRegisterPhone;
    private Button mRegister;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    //初始化布局
    public void initView() {
        mContext = getApplicationContext();//得到上下文
        mRegisterName = findViewById(R.id.register_name);//注册用户名
        mRegisterPass = findViewById(R.id.register_pass);//注册密码
        mRegisterPassAga = findViewById(R.id.register_pass_aga);//确认密码
        mRegisterPhone = findViewById(R.id.register_phone);//确认密码
        mRegister = findViewById(R.id.register);//注册按钮
        //注册按钮点击事件
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTeacherInfo();
            }
        });
    }

    //注册信息的逻辑
    public void setTeacherInfo() {
        String registerName = mRegisterName.getText().toString();//获得用户输入的用户名
        String registerPass = mRegisterPass.getText().toString();//获得用户输入的密码
        String registerPassAga = mRegisterPassAga.getText().toString();//获得用户输入的姓名
        String registerPhone = mRegisterPhone.getText().toString();//电话号码
        //输入框不能为空，做判断
        if (!registerName.equals("") && !registerPass.equals("") && !registerPassAga.equals("") && !registerPhone.equals("")) {

            if (!registerPass.equals(registerPassAga)) {
                Toast.makeText(mContext, "两次密码不相等，请重新输入！", Toast.LENGTH_LONG).show();
                return;
            }
//            //通过 SharedPreferences 保存用户注册的信息 MODE_PRIVATE 是指其他应用不能获得该文件
//            // user_info 是文件名
            SharedPreferences teacherInfo = getSharedPreferences("user_info", MODE_PRIVATE);
            SharedPreferences.Editor editor = teacherInfo.edit();
            editor.putString("user_name", registerName);
            editor.putString("user_pass", registerPass);
            editor.putString("user_phone", registerPhone);
            editor.putString("user_register_time", getLocalTime()); //注册时间
            editor.putString("user_login_time", "1888-08-08 08:08:08"); //登陆时间
            int id = AddressDBService.getInstance().queryAllAddress(this).size() + 99;
            editor.putString("user_id", String.valueOf(id)); //注册id
            editor.apply();

            AddressDBService.getInstance().addAddress(this, String.valueOf(id), registerName, registerPass, "个性签名", registerPhone);
            Toast.makeText(mContext, registerName + " 注册成功", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(mContext, "输入框内容不能有空", Toast.LENGTH_SHORT).show();
        }

    }

    //获得本系统的时间
    public String getLocalTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date.getTime());
        return time;
    }
}