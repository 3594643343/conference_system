package edu.hnu.conference_system.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 会议号
     */
    private String meetingNumber;
    /**
     * 会议密码
     */
//    private String meetingPassword;

    /**
     * 开始时间
     */

    private LocalDateTime startTime;
    /**
     * 入会者权限
     */
    private Integer defaultPermission;

    /**
     * 房主id
     */
    private Long createrId;

    /**
     * 房间成员
     */
    private List<User> Members;

    @Override
    public String toString() {
        return "RoomInfo{" +
                "roomName='" + roomName + '\'' +
                "meetingNumber='" + meetingNumber + '\'' +
                ", createrId='" + createrId + '\'' +
                ", Members=" + Members +
                '}';
    }

}
