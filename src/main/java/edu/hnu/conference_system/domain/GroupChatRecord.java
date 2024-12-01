package edu.hnu.conference_system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName group_chat_record
 */
@TableName(value ="group_chat_record")
@Data
public class GroupChatRecord implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer groupId;

    /**
     * 
     */
    private Integer speakerId;

    /**
     * 
     */
    private String text;

    /**
     * 
     */
    private LocalDateTime time;

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
        GroupChatRecord other = (GroupChatRecord) that;
        return (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getSpeakerId() == null ? other.getSpeakerId() == null : this.getSpeakerId().equals(other.getSpeakerId()))
            && (this.getText() == null ? other.getText() == null : this.getText().equals(other.getText()))
            && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getSpeakerId() == null) ? 0 : getSpeakerId().hashCode());
        result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", groupId=").append(groupId);
        sb.append(", speakerId=").append(speakerId);
        sb.append(", text=").append(text);
        sb.append(", time=").append(time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}