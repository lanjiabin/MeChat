package com.android.mechat.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//创建数据库
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mechat.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //通讯录表
        db.execSQL("CREATE TABLE IF NOT EXISTS address" +
                "(id VARCHAR PRIMARY KEY," +  //联系人id
                "name VARCHAR," +             //联系人名字
                "pass VARCHAR," +             //联系人密码
                "sign VARCHAR," +             //个性签名
                "phone VARCHAR)");            //联系人电话号码


        //聊天记录
        db.execSQL("CREATE TABLE IF NOT EXISTS addressmsg" +
                "(msgid VARCHAR PRIMARY KEY," +  //信息id
                "send1 VARCHAR," +               //收件人
                "send2 VARCHAR," +               //发件人
                "msg VARCHAR," +                 //内容名字
                "time VARCHAR)");                //时间


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
