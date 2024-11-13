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
     * 会议主题
     */
    private String meetingtheme;
    /**
     * 会议开始时间
     */
    private LocalDateTime startTime;
    /**
     * 会议结束时间
     */
    private LocalDateTime endTime;

    // Getters and Setters
    public String getMeetingName() {
        return meetingname;
    }

    public void setMeetingName(String meetingName) {
        this.meetingname = meetingName;
    }

    public String getMeetingTheme() {
        return meetingtheme;
    }

    public void setMeetingTheme(String meetingTheme) {
        this.meetingtheme = meetingTheme;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
