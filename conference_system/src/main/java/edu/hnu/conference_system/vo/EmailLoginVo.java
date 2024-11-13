package edu.hnu.conference_system.vo;

import lombok.Data;

@Data
public class EmailLoginVo {
    private String email; // 用户邮箱
    private String verificationCode; // 验证码
    // 提供一个方法来获取验证码
    public String getCode() {
        return verificationCode;
    }
}