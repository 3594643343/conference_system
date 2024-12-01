package edu.hnu.conference_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName user_record
 */
@TableName(value ="user_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecord implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long userRecordId;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Long meetingId;

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
        UserRecord other = (UserRecord) that;
        return (this.getUserRecordId() == null ? other.getUserRecordId() == null : this.getUserRecordId().equals(other.getUserRecordId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMeetingId() == null ? other.getMeetingId() == null : this.getMeetingId().equals(other.getMeetingId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserRecordId() == null) ? 0 : getUserRecordId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMeetingId() == null) ? 0 : getMeetingId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userRecordId=").append(userRecordId);
        sb.append(", userId=").append(userId);
        sb.append(", meetingId=").append(meetingId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}