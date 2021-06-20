package com.test.store.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class PasswordUtils {
    /**
     * 将密码使用MD5加密
     * @param Password 待加密的密码
     * @return
     */
    public static String encrypt(String Password) {
        String md5Password = DigestUtils.md5DigestAsHex(Password.getBytes(StandardCharsets.UTF_8));
        return md5Password;
    }

    /**
     * 验证密码是否正确
     * @param realPassword_MD5 从数据库中取出的加密后的密码
     * @param thisPassword 前端传来待验证的密码
     * @return
     */
    public static boolean checkPassword(String realPassword_MD5,String thisPassword) {
        String thisPassword_MD5 = encrypt(thisPassword);
        return realPassword_MD5.equals(thisPassword_MD5);
    }
}
