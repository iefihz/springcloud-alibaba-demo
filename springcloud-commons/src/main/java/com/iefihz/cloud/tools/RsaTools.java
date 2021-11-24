package com.iefihz.cloud.tools;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Rsa工具类：
 * 1. 公钥加密结合私钥解密使用（登录表单的密码）
 * 2. 私钥加密结合公钥解密使用（jwt）
 *
 * @author He Zhifei
 * @date 2020/7/18 0:28
 */
public class RsaTools {

    /**
     * 默认字符集
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * Base64 encoder
     */
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();

    /**
     * Base64 decoder
     */
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    /**
     * 随机生成密钥对
     *
     * @return
     * @throws Exception
     */
    public static KeyPair genKeyPair(int keySize) throws Exception {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

        // 初始化密钥对生成器，密钥大小为 96 - 2048 位
        keyPairGen.initialize(keySize, new SecureRandom());

        // 生成一个密钥对，保存在keyPair中
        return keyPairGen.generateKeyPair();
    }

    /**
     * 获取公钥
     *
     * @param keyPair
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getPublicKey(KeyPair keyPair) throws UnsupportedEncodingException {
        return new String(BASE64_ENCODER.encode(keyPair.getPublic().getEncoded()), DEFAULT_CHARSET);
    }

    /**
     * 获取私钥
     *
     * @param keyPair
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getPrivateKey(KeyPair keyPair) throws UnsupportedEncodingException {
        return new String(BASE64_ENCODER.encode(keyPair.getPrivate().getEncoded()), DEFAULT_CHARSET);
    }

    /**
     * publicKey 转 RSAPublicKey
     *
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static RSAPublicKey fromPublicKeyStr(String publicKey) throws Exception {
        byte[] decoded = BASE64_DECODER.decode(publicKey);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }

    /**
     * privateKey 转 RSAPrivateKey
     *
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey fromPrivateKeyStr(String privateKey) throws Exception {
        byte[] decoded = BASE64_DECODER.decode(privateKey);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    /**
     * 公钥加解密
     * @param str
     * @param publicKey
     * @param isEncrypt
     * @return
     * @throws Exception
     */
    private static String publicKeyAction(String str, String publicKey, boolean isEncrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, fromPublicKeyStr(publicKey));
        return getStr(str, cipher, isEncrypt);
    }

    /**
     * 私钥加解密
     * @param str
     * @param privateKey
     * @param isEncrypt
     * @return
     * @throws Exception
     */
    private static String privateKeyAction(String str, String privateKey, boolean isEncrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, fromPrivateKeyStr(privateKey));
        return getStr(str, cipher, isEncrypt);
    }

    /**
     * 处理公私钥加解密的结果
     * @param str
     * @param cipher
     * @param isEncrypt
     * @return
     * @throws Exception
     */
    private static String getStr(String str, Cipher cipher, boolean isEncrypt) throws Exception {
        return isEncrypt ? new String(BASE64_ENCODER.encode(cipher.doFinal(str.getBytes(DEFAULT_CHARSET))), DEFAULT_CHARSET) :
                new String(cipher.doFinal(BASE64_DECODER.decode(str.getBytes(DEFAULT_CHARSET))));
    }

    /**
     * 公钥加密
     * @param str
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String str, String publicKey) throws Exception {
        return publicKeyAction(str, publicKey, true);
    }

    /**
     * 私钥解密
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String str, String privateKey) throws Exception {
        return privateKeyAction(str, privateKey, false);
    }

    /**
     * 私钥加密
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String str, String privateKey) throws Exception {
        return privateKeyAction(str, privateKey, true);
    }

    /**
     * 公钥解密
     * @param str
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String str, String publicKey) throws Exception {
        return publicKeyAction(str, publicKey, false);
    }

}