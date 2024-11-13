package edu.hnu.conference_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookMeetingDto {

    /**
     * 会议名称
     */
    private String meetingName;

    /**
     * 预定开始时间
     */
    private LocalDateTime meetingStartTime;

    /**
     * 预定结束时间
     */
    private LocalDateTime meetingEndTime;

    /**
     *  会议密码
     */
    private String meetingPassword;

    /**
     *入会权限 0与会者 1管理员
     */
    private int defaultPermission;

    /**
     *预置词
     */
    private String presettingWord;
}
