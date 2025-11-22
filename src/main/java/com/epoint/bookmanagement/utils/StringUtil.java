package com.epoint.bookmanagement.utils;

import java.util.List;

public class StringUtil
{

    /**
     * 判断是否为非空
     */
    public static boolean isNotBlank(String str) {
        // TODO Auto-generated method stub
        //
        return str != null && str.trim().length() > 0;
    }

    /**
     * 判断是否为空
     */
    public static boolean isBlank(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 将list中的字符串通过字符进行连接
     * 
     * @param deleteSuccess
     * @param string
     * @return
     */
    public static Object joinListToString(List<String> list, String str) {
        String result = "";
        if (list != null && !list.isEmpty()) {
            int strLength = str.length();
            StringBuffer sb = new StringBuffer();
            for (String s : list) {
                sb.append(s).append(str);
            }
            String tempString = sb.toString();
            result = tempString.substring(0, tempString.length() - strLength);
        }
        return result;
    }

}
