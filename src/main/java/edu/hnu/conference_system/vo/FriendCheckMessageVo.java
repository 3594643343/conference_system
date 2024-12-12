package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendCheckMessageVo {
    /**
     * 记录id
     */
    private Integer recordId;
    /**
     * 发送人id
     */
    private Integer senderId;
    /**
     * 接收人id
     */
    private Integer receiverId;
    /**
     * 验证消息内容
     */
    private String message;
    /**
     * 验证结果  0: 未处理  1: 同意  2: 拒绝
     */
    private Integer result;

}
