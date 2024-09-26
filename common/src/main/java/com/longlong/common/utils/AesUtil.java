
package com.longlong.common.utils;

import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
*Aes 加密
 */
public class AesUtil {

    private AesUtil() {
    }

    /**
     * 生成密钥
     */
    public static String genAesKey() {
        return StringUtil.random(16);
    }

    /**
     *加密
     */
    public static byte[] encrypt(byte[] content, String key) {
        return encrypt(content, key.getBytes(Charsets.UTF_8));
    }

    public static byte[] encrypt(String content, String key) {
        return encrypt(content.getBytes(Charsets.UTF_8), key.getBytes(Charsets.UTF_8));
    }

    public static byte[] encrypt(String content, java.nio.charset.Charset charset, String key) {
        return encrypt(content.getBytes(charset), key.getBytes(Charsets.UTF_8));
    }

    public static byte[] decrypt(byte[] content, String key) {
        return decrypt(content, key.getBytes(Charsets.UTF_8));
    }

    public static String decryptToStr(byte[] content, String key) {
        return new String(decrypt(content, key.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
    }

    public static String decryptToStr(byte[] content, String key, java.nio.charset.Charset charset) {
        return new String(decrypt(content, key.getBytes(Charsets.UTF_8)), charset);
    }
/**
 * 加密
 * */
    public static byte[] encrypt(byte[] content, byte[] aesKey) {
        // 断言密钥长度是否为16位
//        Assert.isTrue(aesKey.length == 16, "密钥长度不对");
        try {
            // 创建加密对象
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            // 创建密钥
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            // 创建初始化向量
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            // 初始化加密对象
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            // 加密
            return cipher.doFinal(Pkcs7Encoder.encode(content));
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    public static byte[] decrypt(byte[] encrypted, byte[] aesKey) {
        // 断言密钥长度是否为16位
        Assert.isTrue(aesKey.length == 16, "密钥长度不对");
        try {
            // 创建一个解密器
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            // 创建一个密钥
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            // 创建一个初始化向量
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            // 返回解密后的数据
            return Pkcs7Encoder.decode(cipher.doFinal(encrypted));
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 提供基于PKCS7算法的加解密接口.
     */
    static class Pkcs7Encoder {
        static int BLOCK_SIZE = 16;

        static byte[] encode(byte[] src) {
            int count = src.length;
            // 计算需要填充的位数
            int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
            if (amountToPad == 0) {
                amountToPad = BLOCK_SIZE;
            }
            // 获得补位所用的字符
            byte pad = (byte) (amountToPad & 0xFF);
            byte[] pads = new byte[amountToPad];
            for (int index = 0; index < amountToPad; index++) {
                pads[index] = pad;
            }
            int length = count + amountToPad;
            byte[] dest = new byte[length];
            System.arraycopy(src, 0, dest, 0, count);
            System.arraycopy(pads, 0, dest, count, amountToPad);
            return dest;
        }

        static byte[] decode(byte[] decrypted) {
            int pad = (int) decrypted[decrypted.length - 1];
            if (pad < 1 || pad > BLOCK_SIZE) {
                pad = 0;
            }
            if (pad > 0) {
                return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
            }
            return decrypted;
        }
    }
}
