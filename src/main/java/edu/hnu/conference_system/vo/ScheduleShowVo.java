package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleShowVo {

    private Long meetingId;

    private String meetingNumber;

    private String meetingName;

    private String meetingHost;

    private LocalDateTime meetingStartTime;

    private LocalDateTime meetingEndTime;

    private String meetingState;
}
