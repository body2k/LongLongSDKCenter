
package com.longlong.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64工具
 */
public class Base64Util {

    /**
     * 编码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    /**
     * 编码
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String encode(String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        return new String(Base64.getEncoder().encode(value.getBytes()), charset);
    }

    /**
     * 解码
     *
     * @param value   字符串
     * @return {String}
     */
    public static String decode(String value) {
        byte[] decode = Base64.getDecoder().decode(value);
        return new String(decode);
    }

    /**
     * 解码
     *
     * @param value   字符串
     * @return {String}
     */
    public static String decode(String value, java.nio.charset.Charset charset) {
        byte[] decode = Base64.getDecoder().decode(value);
        return new String(decode,charset);
    }
}
