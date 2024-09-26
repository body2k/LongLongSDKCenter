
package com.longlong.common.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密相关工具类直接使用Spring util封装，减少jar依赖
 *
 *
 */
public class DigestUtil extends DigestUtils {

	/**
	 * 返回data的MD5摘要，以16进制字符串的形式表示
	 * 继承 org.springframework.util.DigestUtils md5DigestAsHex的方法
	 * @param data 要加密的数据
	 * @return MD5 十六进制字符串
	 */
	public static String md5Hex(final String data) {
	    // 返回data的MD5摘要，以16进制字符串的形式表示
	return DigestUtil.md5DigestAsHex(data.getBytes(Charsets.UTF_8));
	}

	/**
	 * 返回data的MD5摘要，以16进制字符串的形式表示
	 * 继承 org.springframework.util.DigestUtils md5DigestAsHex的方法
	 * @param bytes 要加密的数据
	 * @return MD5 十六进制字符串
	 */
	public static String md5Hex(final byte[] bytes) {
		return md5DigestAsHex(bytes);
	}

	private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

	public static String sha1(String srcStr) {
		return hash("SHA-1", srcStr);
	}

	public static String sha256(String srcStr) {
		return hash("SHA-256", srcStr);
	}
	public static String sha256(byte[] bytes) {
		return hash("SHA-256", bytes);
	}

	public static String sha384(String srcStr) {
		return hash("SHA-384", srcStr);
	}

	public static String sha512(String srcStr) {
		return hash("SHA-512", srcStr);
	}

	public static String hash(String algorithm, String srcStr) {
		try {
			// 获取MessageDigest实例
			MessageDigest md = MessageDigest.getInstance(algorithm);
			// 对srcStr进行加密
			byte[] bytes = md.digest(srcStr.getBytes(Charsets.UTF_8));
			// 将加密后的字节数组转换为十六进制字符串
			return toHex(bytes);
		} catch (NoSuchAlgorithmException e) {
			// 抛出异常
			throw Exceptions.unchecked(e);
		}
	}
	public static String hash(String algorithm, byte[] bytes) {
		try {
			// 获取MessageDigest实例
			MessageDigest md = MessageDigest.getInstance(algorithm);

			// 将加密后的字节数组转换为十六进制字符串
			return toHex(md.digest(bytes));
		} catch (NoSuchAlgorithmException e) {
			// 抛出异常
			throw Exceptions.unchecked(e);
		}
	}
	public static String toHex(byte[] bytes) {
		// 创建StringBuilder对象
		StringBuilder ret = new StringBuilder(bytes.length * 2);
		// 遍历字节数组
		for (int i = 0; i < bytes.length; i++) {
			// 将字节数组的每一位转换为十六进制字符
			ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
			ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
		}
		// 返回十六进制字符串
		return ret.toString();
	}

	public static boolean slowEquals(@Nullable String a, @Nullable String b) {
		// 如果a或b为null，则返回false
		if (a == null || b == null) {
			return false;
		}
		// 对a和b进行加密
		return slowEquals(a.getBytes(Charsets.UTF_8), b.getBytes(Charsets.UTF_8));
	}

	public static boolean slowEquals(@Nullable byte[] a, @Nullable byte[] b) {
		// 如果a或b为null，则返回false
		if (a == null || b == null) {
			return false;
		}
		// 如果a和b的长度不同，则返回false
		if (a.length != b.length) {
			return false;
		}
		// 计算a和b的长度异或结果
		int diff = a.length ^ b.length;
		// 遍历a和b，计算每一个元素异或结果
		for (int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}
		// 如果异或结果为0，则返回true，否则返回false
		return diff == 0;
	}

	/**
	 * 自定义加密 先MD5再SHA1
	 *
	 * @param data 数据
	 * @return String
	 */
	public static String encrypt(String data) {

		return sha1(md5Hex(data));
	}



	public static String decryptMD5(String md5Encrypted) {
		try {
			// 创建一个 MessageDigest 对象，指定加密算法为 MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// 将加密的字符串转换为字节数组
			byte[] encryptedBytes = hexStringToByteArray(md5Encrypted);

			// 解密
			byte[] decryptedBytes = md.digest(encryptedBytes);

			// 将解密后的字节数组转换为字符串
			return new String(decryptedBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 解密失败", e);
		}
	}

	public static byte[] hexStringToByteArray(String hexString) {
		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return data;
	}
}
