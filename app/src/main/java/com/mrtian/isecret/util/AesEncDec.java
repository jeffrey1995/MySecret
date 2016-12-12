package com.mrtian.isecret.util;

/**
 * Created by tianxiying on 16/10/19.
 */
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AesEncDec {

    // 加密算法
    private static final String ALG               = "AES";
    // 字符编码
    private static final String ENC               = "UTF-8";
    // 密钥正规化算法
    private static final String SEC_NORMALIZE_ALG = "MD5";

//    public static void main(String... args) throws Exception {
//        String secret = "aaaaa";// 密钥
//        // 加密
//        String text = "Hello World! 你好，世界！";// 需要加密的数据原文
//        String encrypted = encrypt(secret, text);
//        System.out.println(encrypted);
//
//        // 解密
//        System.out.println(decrypt(secret, encrypted));
//    }

    // 加密
    public static String encrypt(String secret, String data) throws Exception {
        MessageDigest dig = MessageDigest.getInstance(SEC_NORMALIZE_ALG);
        byte[] key = dig.digest(secret.getBytes(ENC));
        SecretKeySpec secKey = new SecretKeySpec(key, ALG);

        Cipher aesCipher = Cipher.getInstance(ALG);
        byte[] byteText = data.getBytes(ENC);

        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(byteText);

        Base64 base64 = new Base64();
        return new String(base64.encode(byteCipherText), ENC);
    }

    // 解密
    public static String decrypt(String secret, String ciphertext) throws Exception {
        MessageDigest dig = MessageDigest.getInstance(SEC_NORMALIZE_ALG);
        byte[] key = dig.digest(secret.getBytes(ENC));
        SecretKeySpec secKey = new SecretKeySpec(key, ALG);

        Cipher aesCipher = Cipher.getInstance(ALG);
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        Base64 base64 = new Base64();
        byte[] cipherbytes = base64.decode(ciphertext.getBytes());
        byte[] bytePlainText = aesCipher.doFinal(cipherbytes);
        return new String(bytePlainText, ENC);
    }

}