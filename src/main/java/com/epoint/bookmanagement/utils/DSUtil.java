package com.epoint.bookmanagement.utils;

import com.alibaba.druid.pool.DruidDataSource;

public class DSUtil
{

    private static DruidDataSource ds;

    /**
     * 创建druid连接池
     * 
     * @return
     */
    public static DruidDataSource getDruidDataSource() {
        if (ds == null) {
            ds = new DruidDataSource();
            ds.setDriverClassName(ConfigUtil.getJDBCConfigValue("driver"));
            ds.setUrl(ConfigUtil.getJDBCConfigValue("url"));
            ds.setUsername(ConfigUtil.getJDBCConfigValue("username"));
            ds.setPassword(ConfigUtil.getJDBCConfigValue("password"));
        }
        return ds;
    }
}
