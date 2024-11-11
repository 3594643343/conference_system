package edu.hnu.conference_system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Data;

/**
 * 
 * @TableName meeting_audio
 */
@TableName(value ="meeting_audio")
@Data
public class MeetingAudio implements Serializable {
    /**
     * 
     */
    @TableId
    private Long meetingAudioId;

    /**
     * 
     */
    private Long meetingId;

    /**
     * 
     */
    private String audioPath;

    /**
     * 
     */
    private LocalTime audioTime;

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
        MeetingAudio other = (MeetingAudio) that;
        return (this.getMeetingAudioId() == null ? other.getMeetingAudioId() == null : this.getMeetingAudioId().equals(other.getMeetingAudioId()))
            && (this.getMeetingId() == null ? other.getMeetingId() == null : this.getMeetingId().equals(other.getMeetingId()))
            && (this.getAudioPath() == null ? other.getAudioPath() == null : this.getAudioPath().equals(other.getAudioPath()))
            && (this.getAudioTime() == null ? other.getAudioTime() == null : this.getAudioTime().equals(other.getAudioTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMeetingAudioId() == null) ? 0 : getMeetingAudioId().hashCode());
        result = prime * result + ((getMeetingId() == null) ? 0 : getMeetingId().hashCode());
        result = prime * result + ((getAudioPath() == null) ? 0 : getAudioPath().hashCode());
        result = prime * result + ((getAudioTime() == null) ? 0 : getAudioTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", meetingAudioId=").append(meetingAudioId);
        sb.append(", meetingId=").append(meetingId);
        sb.append(", audioPath=").append(audioPath);
        sb.append(", audioTime=").append(audioTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}