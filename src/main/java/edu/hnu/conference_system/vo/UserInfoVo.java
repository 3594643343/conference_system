package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 以base64格式储存的图片字符串
     */
    private String avatar;
    /**
     * 个性签名
     */
    private String signature;

    /**
     * 用户权限  与会者0, 管理员1, 创建者2
     */
    private int permission;
    /**
     * 是否加好友要验证 0:不需要 1:需要
     */
    private int needCheck;


}
