package com.mrtian.isecret;

import com.mrtian.isecret.util.AesEncDec;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        String secret = "aaaaa";// 密钥
        // 加密
        String text = "Hello World! 你好，世界！";// 需要加密的数据原文
        String encrypted = AesEncDec.encrypt(secret, text);
        System.out.println(encrypted);
        String encrypted0 = AesEncDec.encrypt("b", text);
        System.out.println(encrypted0);

        // 解密
        System.out.println(AesEncDec.decrypt(secret, encrypted));
        System.out.println(AesEncDec.decrypt("b", encrypted0));
    }
}