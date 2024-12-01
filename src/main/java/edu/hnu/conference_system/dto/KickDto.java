package edu.hnu.conference_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KickDto {
    /**
     * 要踢的人的id
     */
    private Integer id;
    /**
     * 所在会议
     */
    private String meetingNumber;
}
