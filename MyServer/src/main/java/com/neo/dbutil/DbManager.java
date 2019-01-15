package com.neo.dbutil;

import java.sql.*;

/**
 * <p>Description: </p>
 *
 * @author zhangyan
 * @date 2019/1/6 19:50
 */
public class DbManager {

    /**
     * 数据库连接
     */
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String USER = "root";
    public static final String PASS = "root";
    public static final String URL = "jdbc:mysql://localhost:3306/user";

    /**
     * 静态成员，支持单态模式
     */
    private static DbManager per = null;
    private Connection conn = null;
    private Statement stmt = null;

    /**
     * 单例模式-懒汉模式
     */
    private DbManager() {
    }

    public static DbManager createInstance() {
        if (per == null) {
            per = new DbManager();
            per.initDB();
        }
        return per;
    }

    /**
     * 加载驱动
     */
    public void initDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接数据库，获取句柄+对象
     */
    public void connectDB() {
        System.out.println("Connecting to database...");
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("SqlManager:Connect to database successful.");
    }

    /**
     * 关闭数据库 关闭对象，释放句柄
     */
    public void closeDB() {
        System.out.println("Close connection to database..");
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Close connection successful");
    }

    /**
     * 查询
     *
     * @param sql
     * @return
     */
    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 更新
     *
     * @param sql
     * @return
     */
    public int executeUpdate(String sql) {
        int ret = 0;
        try {
            ret = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}

