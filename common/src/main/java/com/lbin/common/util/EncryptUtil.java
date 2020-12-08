package com.lbin.common.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.Digester;

import java.io.UnsupportedEncodingException;

public class EncryptUtil {
    /** 加密循环次数 */
    public final static int HASH_ITERATIONS = 1024;
    /** 字符编码 */
    private final static String CHARSET = "UTF-8";

    /**
     * 加密处理
     * @param password 密码
     * @param salt 密码盐
     */
    public static String encrypt(String password, String salt) {
        Digester digester = SecureUtil.sha256();
        try {
            digester.setSalt(salt.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        digester.setDigestCount(HASH_ITERATIONS);
        return digester.digestHex(password);
    }
}
