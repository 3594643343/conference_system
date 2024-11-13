package edu.hnu.conference_system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName meeting
 */
@TableName(value ="meeting")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meeting implements Serializable {
    /**
     * 会议id
     */
    @TableId
    private Long meetingId;

    /**
     * 创建者id
     */
    private Long userId;

    /**
     * 纪要id
     */
    private Long meetingsMinutesId;

    /**
     * 记录id
     */
    private Long recordId;

    /**
     * 音频id
     */
    private Long meetingAudioId;

    /**
     * 会议名称
     */
    private String meetingName;

    /**
     * 参会人数
     */
    private Integer participantCount;

    /**
     * 会议号
     */
    private String meetingNumber;
    private String meetingTheme; // 会议主题
    private LocalDateTime startTime; // 开始时间
    private LocalDateTime endTime; // 结束时间
    /**
     * 会议密码  快速会议密码为"0"
     */
    private String meetingPassword;

    /**
     * 默认入会权限 0 与会者  1 管理员
     */
    private Integer defaultPermission;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 开始时间
     */
    private LocalDateTime meetingStartTime;

    /**
     * 结束时间
     */
    private LocalDateTime meetingEndTime;

    /**
     * 会议时长
     */
    private LocalTime meetingTime;

    /**
     * 会议状态   未开始 off  进行中 on   已结束 end
     */
    private String meetingState;
    private User organizer; // 组织者

    // Getters and Setters
    public Long getId() {
        return meetingId;
    }

    public void setId(Long id) {
        this.meetingId = id;
    }

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public String getMeetingPassword() {
        return meetingPassword;
    }

    public void setMeetingPassword(String meetingPassword) {
        this.meetingPassword = meetingPassword;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingTheme() {
        return meetingTheme;
    }

    public void setMeetingTheme(String meetingTheme) {
        this.meetingTheme = meetingTheme;
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

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public Meeting(String meetingName,String meetingNumber,String meetingPassword,
                   Long createrId,LocalDateTime startTime,LocalDateTime endTime, Integer defaultPermission)
    {
        this.meetingName = meetingName;
        this.meetingNumber = meetingNumber;
        this.meetingPassword = meetingPassword;
        this.userId = createrId;
        this.meetingStartTime = startTime;
        this.meetingEndTime = endTime;
        this.createTime = LocalDateTime.now();
        this.defaultPermission = defaultPermission;
        this.meetingState = "off";
    }
    public Meeting(String meetingName,String meetingNumber,
                   Long createrId,LocalDateTime startTime )
    {
        this.meetingName = meetingName;
        this.meetingNumber = meetingNumber;
        this.meetingPassword = "0";
        this.userId = createrId;
        this.meetingStartTime = startTime;
        this.createTime = LocalDateTime.now();
        this.defaultPermission = 0;
        this.meetingState = "off";
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Meeting other = (Meeting) that;
        return (this.getMeetingId() == null ? other.getMeetingId() == null : this.getMeetingId().equals(other.getMeetingId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMeetingsMinutesId() == null ? other.getMeetingsMinutesId() == null : this.getMeetingsMinutesId().equals(other.getMeetingsMinutesId()))
            && (this.getRecordId() == null ? other.getRecordId() == null : this.getRecordId().equals(other.getRecordId()))
            && (this.getMeetingAudioId() == null ? other.getMeetingAudioId() == null : this.getMeetingAudioId().equals(other.getMeetingAudioId()))
            && (this.getMeetingName() == null ? other.getMeetingName() == null : this.getMeetingName().equals(other.getMeetingName()))
            && (this.getParticipantCount() == null ? other.getParticipantCount() == null : this.getParticipantCount().equals(other.getParticipantCount()))
            && (this.getMeetingNumber() == null ? other.getMeetingNumber() == null : this.getMeetingNumber().equals(other.getMeetingNumber()))
                && (this.getMeetingPassword() == null ? other.getMeetingPassword() == null : this.getMeetingPassword().equals(other.getMeetingPassword()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getMeetingStartTime() == null ? other.getMeetingStartTime() == null : this.getMeetingStartTime().equals(other.getMeetingStartTime()))
            && (this.getMeetingEndTime() == null ? other.getMeetingEndTime() == null : this.getMeetingEndTime().equals(other.getMeetingEndTime()))
            && (this.getMeetingTime() == null ? other.getMeetingTime() == null : this.getMeetingTime().equals(other.getMeetingTime()))
            && (this.getMeetingState() == null ? other.getMeetingState() == null : this.getMeetingState().equals(other.getMeetingState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMeetingId() == null) ? 0 : getMeetingId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMeetingsMinutesId() == null) ? 0 : getMeetingsMinutesId().hashCode());
        result = prime * result + ((getRecordId() == null) ? 0 : getRecordId().hashCode());
        result = prime * result + ((getMeetingAudioId() == null) ? 0 : getMeetingAudioId().hashCode());
        result = prime * result + ((getMeetingName() == null) ? 0 : getMeetingName().hashCode());
        result = prime * result + ((getParticipantCount() == null) ? 0 : getParticipantCount().hashCode());
        result = prime * result + ((getMeetingNumber() == null) ? 0 : getMeetingNumber().hashCode());
        result = prime * result + ((getMeetingPassword() == null) ? 0 : getMeetingPassword().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getMeetingStartTime() == null) ? 0 : getMeetingStartTime().hashCode());
        result = prime * result + ((getMeetingEndTime() == null) ? 0 : getMeetingEndTime().hashCode());
        result = prime * result + ((getMeetingTime() == null) ? 0 : getMeetingTime().hashCode());
        result = prime * result + ((getMeetingState() == null) ? 0 : getMeetingState().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", meetingId=").append(meetingId);
        sb.append(", userId=").append(userId);
        sb.append(", meetingsMinutesId=").append(meetingsMinutesId);
        sb.append(", recordId=").append(recordId);
        sb.append(", meetingAudioId=").append(meetingAudioId);
        sb.append(", meetingName=").append(meetingName);
        sb.append(", participantCount=").append(participantCount);
        sb.append(", meetingNumber=").append(meetingNumber);
        sb.append(", createTime=").append(createTime);
        sb.append(", meetingStartTime=").append(meetingStartTime);
        sb.append(", meetingEndTime=").append(meetingEndTime);
        sb.append(", meetingTime=").append(meetingTime);
        sb.append(", meetingState=").append(meetingState);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

}