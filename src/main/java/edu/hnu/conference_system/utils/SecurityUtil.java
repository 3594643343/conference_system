package edu.hnu.conference_system.utils;

import cn.hutool.crypto.SecureUtil;

public class SecurityUtil {
    public static String EncryptedPassword(String password) {
        // 使用md54加密密码
        return SecureUtil.md5(password);
    }
}
