package com.epoint.bookmanagement.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;

public class JDBCUtil
{
    /**
     * 创建连接方法
     */
    public static Connection getConnection() {
        DruidDataSource ds = DSUtil.getDruidDataSource();
        Connection con = null;
        try {
            con = ds.getConnection();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return con;
    }

    /**
     * 关闭所有对象方法
     */
    public static void closeConnection(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
