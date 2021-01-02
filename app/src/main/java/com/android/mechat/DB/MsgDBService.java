package com.android.mechat.DB;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

//数据库的具体操作，有mysql语句，采用单例模式
public class MsgDBService {
    private static MsgDBService msgDBService = null;

    private MsgDBService() {
    }

    //单例模式
    public static MsgDBService getInstance() {
        if (msgDBService == null) {
            msgDBService = new MsgDBService();
        }
        return msgDBService;
    }

    /**
     * 1.查询所有全部信息
     **/
    public ArrayList<HashMap<String, String>> queryAllAddressMsg(Context context) {
        MusicDBManager musicDBManager = new MusicDBManager(context);
        DBHelper helper = new DBHelper(context);
        String sql = "SELECT * FROM addressmsg";
        ArrayList<HashMap<String, String>> queryResult = musicDBManager.querySQLite(sql, null);
        return queryResult;
    }


    /**
     * 2.添加一条信息
     **/
    public void addAddressMsg(Context context,
                              String send1,
                              String send2,
                              String msg) {
        String sql = "INSERT INTO addressmsg(msgid,send1,send2,msg,time) VALUES(?,?,?,?,?)";
        MusicDBManager musicDBManager = new MusicDBManager(context);
        DBHelper helper = new DBHelper(context);

        musicDBManager.updateSQLite(sql, new String[]{
                getLocalTime(),
                send1,
                send2,
                msg,
                getLocalTime() + "-"});
    }

    //获得本系统的时间
    public String getLocalTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date.getTime());
        return time;
    }

}
