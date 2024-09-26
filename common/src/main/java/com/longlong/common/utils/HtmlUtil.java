package com.longlong.common.utils;

public class HtmlUtil {
    /*获取Html的文本*/
    public static String getText(String html) {
        return html.replaceAll("<[^>]*>", "").replaceAll("\n", "");
    }
}
