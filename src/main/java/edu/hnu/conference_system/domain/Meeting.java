package edu.hnu.conference_system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

/**
 * 
 * @TableName meeting
 */
@TableName(value ="meeting")
@Data
public class Meeting implements Serializable {
    /**
     * 
     */
    @TableId
    private String meetingId;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private Long meetingsMinutesId;

    /**
     * 
     */
    private Long recordId;

    /**
     * 
     */
    private Long meetingAudioId;

    /**
     * 
     */
    private String meetingName;

    /**
     * 
     */
    private Integer participantCount;

    /**
     * 
     */
    private String meetingNumber;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime meetingStartTime;

    /**
     * 
     */
    private LocalDateTime meetingEndTime;

    /**
     * 
     */
    private LocalTime meetingTime;

    /**
     * 
     */
    private String meetingState;

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