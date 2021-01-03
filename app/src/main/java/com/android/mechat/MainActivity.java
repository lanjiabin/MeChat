package com.android.mechat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mechat.DB.AddressDBService;
import com.android.mechat.DB.MsgDBService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView mChatRecyclerView;
    RecyclerAdapter mRecyclerAdapter;
    ChatRecyclerAdapter mChatRecyclerAdapter;
    ArrayList<HashMap<String, String>> mAddressList;
    ArrayList<HashMap<String, String>> mMsgAddressList; //查出所有聊天记录

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

    RelativeLayout mMeRelative; // 我的 页面
    Button mCancelLogin; //取消登陆
    TextView mNameTV; //名字
    TextView mPhoneV; //电话
    EditText mSignEt; //个性签名

    List<String> mMsgSend1;
    ArrayList<HashMap<String, String>> mList; //聊天列表数据

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
        mChatRecyclerView = findViewById(R.id.chatRecyclerView);
        //设置布局方向
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        mNameTV = findViewById(R.id.user_name_tv);
        mPhoneV = findViewById(R.id.phone_tv);
        mSignEt = findViewById(R.id.sign_et);

        change1(); //默认选中按钮1

        SharedPreferences share = getSharedPreferences("user_info", MODE_PRIVATE);
        String user_id = share.getString("user_id", "");
        ArrayList<HashMap<String, String>> info = AddressDBService.getInstance().queryAddressByID(this, user_id);
        String sign = info.get(0).get("sign");
        if (sign.equals("个性签名")) {
            mSignEt.setText("");
        } else {
            mSignEt.setText(sign);
        }
        mNameTV.setText(info.get(0).get("name"));
        mPhoneV.setText("电话: " + info.get(0).get("phone"));

        initData(); //更新聊天列表

    }

    //聊天列表
    public void initData() {
        //查询所有聊天记录
        mMsgAddressList = MsgDBService.getInstance().queryAllAddressMsg(this);
        mMsgSend1 = new ArrayList<>();
        if (mMsgAddressList.size() > 0) {
            String[] array = new String[mMsgAddressList.size()];
            for (int i = 0; i < mMsgAddressList.size(); i++) {
                array[i] = mMsgAddressList.get(i).get("send1");
            }

            //查出有多少人和登陆用户聊过天
            mMsgSend1 = contains(array);
        }

        //本地登陆的账户
        SharedPreferences share2 = getSharedPreferences("user_info", MODE_PRIVATE);
        String user_id2 = share2.getString("user_id", "");

        mList = new ArrayList<HashMap<String, String>>();
        for (int j = 0; j < mMsgSend1.size(); j++) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < mMsgAddressList.size(); i++) {
                if (user_id2.equals(mMsgAddressList.get(i).get("send2"))
                        && mMsgAddressList.get(i).get("send1").equals(mMsgSend1.get(j))) {
                    map.put("id", mMsgAddressList.get(i).get("msgid"));
                    map.put("msg", mMsgAddressList.get(i).get("msg"));
                    String name = AddressDBService.getInstance().queryAddressByID(getApplicationContext(), mMsgAddressList.get(i).get("send1")).get(0).get("name");
                    map.put("name", name);
                    map.put("send1", mMsgAddressList.get(i).get("send1"));
                    map.put("send2", mMsgAddressList.get(i).get("send2"));
                    map.put("time", mMsgAddressList.get(i).get("time"));
                }

            }
            mList.add(map);
        }

        mChatRecyclerAdapter = new ChatRecyclerAdapter(this, mList);
        mChatRecyclerAdapter.notifyDataSetChanged();
        mChatRecyclerView.setAdapter(mChatRecyclerAdapter);
    }

    //点击事件
    public void initListener() {
        // RecyclerView 点击事件，单击和长击。缺陷：长按的时候，只有把手放起来，才会触发长按事件。
        mRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener(this, mRecyclerView, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

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

        //聊天页面列表点击事件
        mChatRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener(this, mChatRecyclerView, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                SharedPreferences share = getSharedPreferences("user_info", MODE_PRIVATE);
                String user_id1 = mList.get(position).get("send1");
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


        mSignEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //文字输入完毕后，自动更新个性签名
                SharedPreferences share = getSharedPreferences("user_info", MODE_PRIVATE);
                String user_id = share.getString("user_id", "");
                AddressDBService.getInstance().updateAddressByID(getApplicationContext(), user_id, editable.toString());

            }
        });


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

    //去掉重复元素
    public List<String> contains(String[] array) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
            if (!list.contains(array[i])) {
                list.add(array[i]);
            }
        }
        return list;
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

            mChatRecyclerView.setVisibility(View.VISIBLE);

            initData();

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
            mChatRecyclerView.setVisibility(View.GONE);

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
            mChatRecyclerView.setVisibility(View.GONE);

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
            mChatRecyclerView.setVisibility(View.GONE);

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
            AddressDBService.getInstance().addAddress(this, contactId, name, "密码", "个性签名", phone1);

            //记得要把cursor给close掉
            phoneCursor.close();
        }
        cursor.close();
    }
}