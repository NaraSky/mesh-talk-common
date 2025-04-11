package com.lb.im.common.cache.md5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash {

    /**
     * 计算输入字符串的MD5哈希值并返回其十六进制表示。
     *
     * @param message 需要计算哈希的输入字符串
     * @return 输入字符串的MD5哈希值的十六进制字符串，如果发生异常则返回null
     */
    public static String md5Java(String message) {
        String digest = null;
        try {
            /*
             * 使用MD5算法生成输入字符串的哈希值
             */
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));

            /*
             * 将字节数组转换为十六进制字符串表示
             */
            //converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }

            digest = sb.toString();

        } catch (UnsupportedEncodingException ex) {
            /*
             * 捕获不支持的编码异常，但未记录日志
             */
            //Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            /*
             * 捕获算法不存在异常，但未记录日志
             */
            //Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE, null, ex);
        }
        return digest;
    }
}
