package com.android.mechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mechat.DB.MsgDBService;

import java.util.ArrayList;
import java.util.HashMap;

public class MsgActivity extends AppCompatActivity {

    EditText mMsgEt;
    Button mSendBtn;

    MsgRecyclerAdapter mMsgRecyclerAdapter;
    ArrayList<HashMap<String, String>> mMsgList;
    RecyclerView mMsgRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        initView();
        initListener();

    }

    public void initView() {
        mMsgEt = findViewById(R.id.msg_edit);
        mSendBtn = findViewById(R.id.send_btn);
        mMsgRecycler = findViewById(R.id.msg_recycler);
        mMsgList = MsgDBService.getInstance().queryAllAddressMsg(this);

        if (mMsgList == null || mMsgList.size() <= 0) {
            return;
        }
        updateData();
    }

    public void updateData() {
        mMsgList = MsgDBService.getInstance().queryAllAddressMsg(this);
        //设置布局方向
        mMsgRecycler.setLayoutManager(new LinearLayoutManager(this));
        //设置adapter关联的list
        Intent intent = getIntent();
        String send1 = intent.getStringExtra("send1"); //收件人
        String send2 = intent.getStringExtra("send2"); //发件人
        mMsgRecyclerAdapter = new MsgRecyclerAdapter(this, mMsgList, send1, send2);
        //数据更新
        mMsgRecyclerAdapter.notifyDataSetChanged();
        //关联adapter
        mMsgRecycler.setAdapter(mMsgRecyclerAdapter);
        for (int i = 0; i < mMsgList.size(); i++) {
        }
        mMsgRecycler.smoothScrollToPosition(mMsgList.size() - 1);
    }

    public void initListener() {
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mMsgEt.getText().toString().trim();
                if (msg.equals("")) {
                    Toast.makeText(MsgActivity.this, "不能发送空白信息！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = getIntent();
                    String send1 = intent.getStringExtra("send1"); //收件人
                    String send2 = intent.getStringExtra("send2"); //发件人
                    MsgDBService.getInstance().addAddressMsg(MsgActivity.this, send1, send2, msg);
                    mMsgEt.setText("");
                    //数据更新
                    updateData();
                }

            }
        });
    }
}