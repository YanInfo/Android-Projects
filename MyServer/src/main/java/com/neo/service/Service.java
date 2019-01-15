package com.neo.service;

import com.neo.dbutil.DbManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>Description: </p>
 *
 * @author zhangyan
 * @date 2019/1/6 19:53
 */
public class Service {

    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     */
    public boolean register(String username, String password) {

        if ((!("").equals(username)) && (!("").equals(password))) {
            // 获取Sql查询语句
            String regSql = "insert into user(username, password) values('" + username + "','" + password + "') ";
            // 获取DB对象
            DbManager sql = DbManager.createInstance();
            sql.connectDB();

            int ret = sql.executeUpdate(regSql);
            if (ret != 0) {
                sql.closeDB();
                return true;
            }
            sql.closeDB();
            return false;
        } else {
            return false;
        }

    }

    /**
     * 查询
     *
     * @param username
     * @return
     */
    public boolean execute(String username) {

        if ((!("").equals(username))) {

            String exeSql = "select * from user where username ='" + username + "'";
            boolean flag = true;
            DbManager sql = DbManager.createInstance();
            sql.connectDB();

            try {
                ResultSet exe = sql.executeQuery(exeSql);
                //username存在，返回true
                if (exe.next()) {
                    sql.closeDB();
                    flag = true;
                } else {
                    flag = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return flag;
        } else {
            return false;
        }

    }


    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    public boolean login(String username, String password) {

        if ((!("").equals(username)) && (!("").equals(password))) {
            // 获取Sql查询语句
            String logSql = "select * from user where username ='" + username
                    + "' and password ='" + password + "'";

            // 获取DB对象
            DbManager sql = DbManager.createInstance();
            sql.connectDB();

            // 操作DB对象
            try {
                ResultSet rs = sql.executeQuery(logSql);
                if (rs.next()) {
                    sql.closeDB();
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sql.closeDB();
            return false;
        } else {
            return false;
        }
    }

    /**
     * 封装json
     *
     * @param type
     * @return
     */
    public String toJson(int type) {
        JSONObject jsonObject = new JSONObject();
        String jsonString = "";
        try {
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString = jsonObject.toString();
        return jsonString;
    }

}
