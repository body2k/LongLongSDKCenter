package com.longlong.common.utils;

import java.nio.charset.StandardCharsets;

public class DBUtil {
    /**
     * 数据库Url 提取表名字
     *
     * @param url jdbc:mysql://0.0.0.0:3308/ha_book_user?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
     * @return ha_book_use
     */
    /**
     * 将数据库URL转换为数据库名称
     * @param url 数据库URL
     * @return 数据库名称
     */
    public static String dbUrlToDBName(String url) {
        // 将url转换为UTF-8编码
        url = new String(url.getBytes(), StandardCharsets.UTF_8);
        // 获取url中"/"后面的第一个字符的位置
        int start = url.lastIndexOf("/") + 1;
        // 获取url中"?"前面的最后一个字符的位置
        int end = url.indexOf("?", start);
        // 截取数据库名称
        return url.substring(start, end);
    }
}
