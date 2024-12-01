package edu.hnu.conference_system.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 用户所在会议id,不在会议为null
     */
    private Long meetingId;
    /**
     * 用户所在会议会议号,不在会议为null
     */
    private String meetingNumber;
    /**
     * 用户所在会议权限,不在会议为-1, 与会者0, 管理员1, 创建者2
     */
    private int meetingPermission;
    /**
     * 用户客户端url, 通信时可以要用
     */
    private String url;

}
