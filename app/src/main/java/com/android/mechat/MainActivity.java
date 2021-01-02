package com.android.mechat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mechat.DB.AddressDBService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerAdapter mRecyclerAdapter;
    ArrayList<HashMap<String, String>> mAddressList;

    //底下的四个按钮
    Button mBtn1;
    Button mBtn2;
    Button mBtn3;
    Button mBtn4;

    //底下的四个文字
    TextView mTv1;
    TextView mTv2;
    TextView mTv3;
    TextView mTv4;

    RelativeLayout mMeRelative;
    Button mCancelLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAddressInfo();
        initView();
        initListener();
    }

    //控件初始化
    public void initView() {
        //查询所有联系人
        mAddressList = AddressDBService.getInstance().queryAllAddress(this);

        //布局初始化
        mRecyclerView = findViewById(R.id.recyclerListView);
        //设置布局方向
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置adapter关联的list
        mRecyclerAdapter = new RecyclerAdapter(this, mAddressList);
        //数据更新
        mRecyclerAdapter.notifyDataSetChanged();
        //关联adapter
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mBtn1 = findViewById(R.id.btn1);
        mBtn2 = findViewById(R.id.btn2);
        mBtn3 = findViewById(R.id.btn3);
        mBtn4 = findViewById(R.id.btn4);

        mTv1 = findViewById(R.id.tv1);
        mTv2 = findViewById(R.id.tv2);
        mTv3 = findViewById(R.id.tv3);
        mTv4 = findViewById(R.id.tv4);

        mMeRelative = findViewById(R.id.me_relative);
        mCancelLogin = findViewById(R.id.cancel_login);

        change1(); //默认选中按钮1

    }

    //点击事件
    public void initListener() {
        // RecyclerView 点击事件，单击和长击。缺陷：长按的时候，只有把手放起来，才会触发长按事件。
        mRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener(this, mRecyclerView, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "点击了：" + position, Toast.LENGTH_SHORT).show();

                SharedPreferences share = getSharedPreferences("user_info", MODE_PRIVATE);
                String user_id1 = mAddressList.get(position).get("id");
                String user_id2 = share.getString("user_id", "");

                Intent intent = new Intent(MainActivity.this, MsgActivity.class);
                intent.putExtra("send1", user_id1); //收件人
                intent.putExtra("send2", user_id2); //发件人

                if (user_id1 == null || user_id2 == null || user_id1.equals("") || user_id2.equals("")) {
                    return;
                }

                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));


        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change1();
            }
        });

        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change2();
            }
        });


        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change3();
            }
        });

        mBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change4();
            }
        });


        mTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change1();
            }
        });

        mTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change2();
            }
        });


        mTv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change3();
            }
        });

        mTv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change4();
            }
        });

        mCancelLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("isAutoLogin", false);
                startActivity(intent);
                finish();
            }
        });

    }

    public void change1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBtn1.setBackground(getResources().getDrawable(R.drawable.groupchat2));
            mBtn2.setBackground(getResources().getDrawable(R.drawable.address1));
            mBtn3.setBackground(getResources().getDrawable(R.drawable.looklook1));
            mBtn4.setBackground(getResources().getDrawable(R.drawable.me1));

            mTv1.setTextColor(Color.GREEN);
            mTv2.setTextColor(Color.GRAY);
            mTv3.setTextColor(Color.GRAY);
            mTv4.setTextColor(Color.GRAY);

            mRecyclerView.setVisibility(View.GONE);
            mMeRelative.setVisibility(View.GONE);

        }
    }

    public void change2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBtn1.setBackground(getResources().getDrawable(R.drawable.groupchat1));
            mBtn2.setBackground(getResources().getDrawable(R.drawable.address2));
            mBtn3.setBackground(getResources().getDrawable(R.drawable.looklook1));
            mBtn4.setBackground(getResources().getDrawable(R.drawable.me1));

            mTv1.setTextColor(Color.GRAY);
            mTv2.setTextColor(Color.GREEN);
            mTv3.setTextColor(Color.GRAY);
            mTv4.setTextColor(Color.GRAY);

            mRecyclerView.setVisibility(View.VISIBLE);
            mMeRelative.setVisibility(View.GONE);

        }
    }

    public void change3() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBtn1.setBackground(getResources().getDrawable(R.drawable.groupchat1));
            mBtn2.setBackground(getResources().getDrawable(R.drawable.address1));
            mBtn3.setBackground(getResources().getDrawable(R.drawable.looklook2));
            mBtn4.setBackground(getResources().getDrawable(R.drawable.me1));

            mTv1.setTextColor(Color.GRAY);
            mTv2.setTextColor(Color.GRAY);
            mTv3.setTextColor(Color.GREEN);
            mTv4.setTextColor(Color.GRAY);

            mRecyclerView.setVisibility(View.GONE);
            mMeRelative.setVisibility(View.GONE);

        }
    }

    public void change4() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBtn1.setBackground(getResources().getDrawable(R.drawable.groupchat1));
            mBtn2.setBackground(getResources().getDrawable(R.drawable.address1));
            mBtn3.setBackground(getResources().getDrawable(R.drawable.looklook1));
            mBtn4.setBackground(getResources().getDrawable(R.drawable.me2));

            mTv1.setTextColor(Color.GRAY);
            mTv2.setTextColor(Color.GRAY);
            mTv3.setTextColor(Color.GRAY);
            mTv4.setTextColor(Color.GREEN);

            mRecyclerView.setVisibility(View.GONE);
            mMeRelative.setVisibility(View.VISIBLE);

        }
    }

    //获取联系人信息
    public void getAddressInfo() {
        Cursor cursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phone1 = "";
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            while (phoneCursor.moveToNext()) {
                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone = phone.replace("-", "");
                phone = phone.replace(" ", "");
                if (phone1.equals("")) {
                    phone1 = phone;
                }

            }

            //添加到数据库
            AddressDBService.getInstance().addAddress(this, contactId, name, "null", phone1);

            //记得要把cursor给close掉
            phoneCursor.close();
        }
        cursor.close();
    }
}