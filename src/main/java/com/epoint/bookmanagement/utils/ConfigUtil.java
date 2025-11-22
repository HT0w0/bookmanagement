package com.epoint.bookmanagement.utils;

import java.util.ResourceBundle;

public class ConfigUtil
{
    /**
     * 获取JDBC的配置信息
     * 
     * @param configName
     * @return
     */
    public static String getJDBCConfigValue(String configName) {
        String configValue = null;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("jdbc");
        configValue = resourceBundle.getString(configName);
        if (StringUtil.isNotBlank(configValue)) {
            configValue = configValue.trim();
        }
        return configValue;
    }
}
