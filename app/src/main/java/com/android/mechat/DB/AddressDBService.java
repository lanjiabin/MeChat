package com.android.mechat.DB;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

//数据库的具体操作，有mysql语句，采用单例模式
public class AddressDBService {
    private static AddressDBService addressDBService = null;

    private AddressDBService() {
    }

    //单例模式
    public static AddressDBService getInstance() {
        if (addressDBService == null) {
            addressDBService = new AddressDBService();
        }
        return addressDBService;
    }

    /**
     * 1.查询所有查询联系人信息
     **/
    public ArrayList<HashMap<String, String>> queryAllAddress(Context context) {
        MusicDBManager musicDBManager = new MusicDBManager(context);
        DBHelper helper = new DBHelper(context);
        String sql = "SELECT * FROM address";
        ArrayList<HashMap<String, String>> queryResult = musicDBManager.querySQLite(sql, null);
        return queryResult;
    }

    /**
     * 2.根据id查询联系人
     **/
    public ArrayList<HashMap<String, String>> queryAddressByID(Context context, String addressId) {
        MusicDBManager musicDBManager = new MusicDBManager(context);
        DBHelper helper = new DBHelper(context);
        String sql = "SELECT * FROM address WHERE id=?";
        ArrayList<HashMap<String, String>> queryResult = musicDBManager.querySQLite(sql, new String[]{addressId});
        return queryResult;
    }


    /**
     * 3.更新一个联系人信息
     **/
    public void updateAddressByID(Context context,
                                  String id,
                                  String name,
                                  String phone) {
        MusicDBManager musicDBManager = new MusicDBManager(context);
        DBHelper helper = new DBHelper(context);
        String sql = "UPDATE address SET name=?,phone=? WHERE id=?";
        musicDBManager.updateSQLite(sql, new String[]{
                name,
                phone,
                id});
    }


    /**
     * 4.添加一个联系人
     **/
    public void addAddress(Context context,
                           String id,
                           String name,
                           String pass,
                           String phone) {
        String sql = "INSERT INTO address(id,name,pass,phone) VALUES(?,?,?,?)";
        MusicDBManager musicDBManager = new MusicDBManager(context);
        DBHelper helper = new DBHelper(context);

        musicDBManager.updateSQLite(sql, new String[]{
                id,
                name,
                pass,
                phone});
    }

    /**
     * 5.删除一个联系人
     **/
    public void deleteAddressByID(Context context, String id) {
        MusicDBManager musicDBManager = new MusicDBManager(context);
        DBHelper helper = new DBHelper(context);
        String sql = "delete from address where id=?";
        musicDBManager.updateSQLite(sql, new String[]{id});
    }

}
