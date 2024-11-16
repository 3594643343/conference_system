package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordVo {
    private Long recordId;
    private String meetingName;
    private String meetingHost;
    private LocalDateTime meetingStartTime;
    private LocalDateTime meetingEndTime;
    private List<String> participants;
}
