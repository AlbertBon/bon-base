package com.bon.common.util;

import java.sql.*;
import java.util.List;

/**
 * @program: 后台-基础项目
 * @description: 数据库连接
 * @author: Bon
 * @create: 2018-08-17 15:13
 **/
public class GenerateDBUtils {
    private static final Connection conn;
    private static final String driverClass = GeneratePropertiesUtils.getValueByKey("generator.jdbc.driver");
    private static final String connectionUrl = GeneratePropertiesUtils.getValueByKey("generator.jdbc.url");
    private static final String username = GeneratePropertiesUtils.getValueByKey("generator.jdbc.username");
    private static final String password = GeneratePropertiesUtils.getValueByKey("generator.jdbc.password");

    private static GenerateDBUtils instance = null;
    /**
     * 定义代码块.
     */
    static {
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(connectionUrl, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**建立单例模式
     * Single
     * @return
     */
    public static GenerateDBUtils getInstance() {
        if (instance == null) {
            synchronized (GenerateDBUtils.class) {
                instance = new GenerateDBUtils();
            }
        }
        return instance;
    }


    /**
     * 查询数据
     * @param sql
     * @param params
     * @return
     */
    public static ResultSet query(String sql, List<Object> params) {
//        System.out.println("sql: " + sql);
        //System.out.println("params: " + params);
        try {
            PreparedStatement psmt = conn.prepareStatement(sql);
            if(params != null) {
                for (int i = 0; i < params.size(); i++) {
                    psmt.setObject(i+1, params.get(i));
                }
            }
            return psmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 更新
     * @param sql
     * @param params
     */
    public static void update(String sql, List<Object> params) {
//        System.out.println("sql: " + sql);
        //System.out.println("params: " + params);
        try {
            PreparedStatement psmt = conn.prepareStatement(sql);
            if(params != null) {
                for (int i = 0; i < params.size(); i++) {
                    psmt.setObject(i+1, params.get(i));
                }
            }
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取链接
     * @return
     */
    public static Connection getConnection(){
        try {
            Class.forName(driverClass);
            return DriverManager.getConnection(connectionUrl, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
