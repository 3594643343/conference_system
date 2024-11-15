package edu.hnu.conference_system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingInfoVo {
    /**
     * 会议名称
     */
    private String meetingname;
    /**
     * 会议创建者用户名
     */
    private String meetingHost;
    /**
     * 会议开始时间
     */
    private LocalDateTime startTime;
    /**
     * 会议结束时间
     */
    private LocalDateTime endTime;


}
