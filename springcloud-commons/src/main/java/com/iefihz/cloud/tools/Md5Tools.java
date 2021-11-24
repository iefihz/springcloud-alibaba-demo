package com.iefihz.cloud.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 *
 * @author He Zhifei
 * @date 2020/7/18 13:22
 */
public class Md5Tools {

    /**
     * 加密算法
     */
    private static final String MESSAGE_DIGEST_NAME = "MD5";

    /**
     * md5加密
     * @param str 明文
     * @return
     */
    public static final String generate(String str) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = str.getBytes();
            MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
            md.update(btInput);
            byte[] mdByte = md.digest();
            int len = mdByte.length;
            char[] charArr = new char[len * 2];
            int k = 0;
            for (int i = 0; i < len; i++) {
                byte b = mdByte[i];
                charArr[(k++)] = hexDigits[(b >>> 4 & 0xF)];
                charArr[(k++)] = hexDigits[(b & 0xF)];
            }
            return new String(charArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * md5加盐加密
     * @param str 明文
     * @param salt 加密盐
     * @return
     */
    public static final String generate(String str, String salt) {
        return generate(str + salt);
    }

    /**
     * md5加密校验
     * @param str 明文
     * @param encrypt 密文
     * @return
     */
    public static final Boolean verify(String str, String encrypt) {
        if (str == null || encrypt == null || str.trim().length() == 0 || encrypt.trim().length() == 0) {
            throw new IllegalArgumentException("md5前后参数不能为空");
        }
        return encrypt.equals(generate(str));
    }

    /**
     * md5加盐加密校验
     * @param str 明文
     * @param encrypt 密文
     * @param salt 加密盐
     * @return
     */
    public static final Boolean verify(String str, String encrypt, String salt) {
        return verify(str + salt, encrypt);
    }

    /**
     * md5加密文件
     * @param filepath 文件路径
     * @return
     */
    public static final String generate4File(String filepath) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
            File file = new File(filepath);
            FileInputStream fis = new FileInputStream(file);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi.toString(16);
    }
}
