package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingVo {
    /**
     * 会议号
     */
    private String meetingNumber;

    private String meetingPassword;
}
