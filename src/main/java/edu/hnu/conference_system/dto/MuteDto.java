package edu.hnu.conference_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MuteDto {
    /**
     * 要禁言的用户id
     */
    private Integer userId;
    /**
     * 所在的会议号
     */
    private String meetingNumber;
}
