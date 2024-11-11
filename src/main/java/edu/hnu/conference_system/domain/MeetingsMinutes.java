package edu.hnu.conference_system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName meetings_minutes
 * 会议纪要
 */
@TableName(value ="meetings_minutes")
@Data
public class MeetingsMinutes implements Serializable {
    /**
     * 
     */
    @TableId
    private Long meetingsMinutesId;

    /**
     * 
     */
    private Long meetingId;

    /**
     * 
     */
    private String meetingRecord;

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
        MeetingsMinutes other = (MeetingsMinutes) that;
        return (this.getMeetingsMinutesId() == null ? other.getMeetingsMinutesId() == null : this.getMeetingsMinutesId().equals(other.getMeetingsMinutesId()))
            && (this.getMeetingId() == null ? other.getMeetingId() == null : this.getMeetingId().equals(other.getMeetingId()))
            && (this.getMeetingRecord() == null ? other.getMeetingRecord() == null : this.getMeetingRecord().equals(other.getMeetingRecord()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMeetingsMinutesId() == null) ? 0 : getMeetingsMinutesId().hashCode());
        result = prime * result + ((getMeetingId() == null) ? 0 : getMeetingId().hashCode());
        result = prime * result + ((getMeetingRecord() == null) ? 0 : getMeetingRecord().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", meetingsMinutesId=").append(meetingsMinutesId);
        sb.append(", meetingId=").append(meetingId);
        sb.append(", meetingRecord=").append(meetingRecord);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}