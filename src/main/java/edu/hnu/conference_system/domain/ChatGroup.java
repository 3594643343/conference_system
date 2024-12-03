package edu.hnu.conference_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName chat_group
 */
@TableName(value ="chat_group")
@Data
public class ChatGroup implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer groupId;

    /**
     * 
     */
    private Integer groupCreatorId;

    /**
     * 
     */
    private String groupName;

    /**
     * 
     */
    private LocalDateTime groupCreateTime;

    /**
     * 
     */
    private String groupAvatarPath;

    /**
     * 
     */
    private Integer needCheck;

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
        ChatGroup other = (ChatGroup) that;
        return (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getGroupCreatorId() == null ? other.getGroupCreatorId() == null : this.getGroupCreatorId().equals(other.getGroupCreatorId()))
            && (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
            && (this.getGroupCreateTime() == null ? other.getGroupCreateTime() == null : this.getGroupCreateTime().equals(other.getGroupCreateTime()))
            && (this.getGroupAvatarPath() == null ? other.getGroupAvatarPath() == null : this.getGroupAvatarPath().equals(other.getGroupAvatarPath()))
            && (this.getNeedCheck() == null ? other.getNeedCheck() == null : this.getNeedCheck().equals(other.getNeedCheck()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getGroupCreatorId() == null) ? 0 : getGroupCreatorId().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getGroupCreateTime() == null) ? 0 : getGroupCreateTime().hashCode());
        result = prime * result + ((getGroupAvatarPath() == null) ? 0 : getGroupAvatarPath().hashCode());
        result = prime * result + ((getNeedCheck() == null) ? 0 : getNeedCheck().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", groupId=").append(groupId);
        sb.append(", groupCreatorId=").append(groupCreatorId);
        sb.append(", groupName=").append(groupName);
        sb.append(", groupCreateTime=").append(groupCreateTime);
        sb.append(", groupAvatarPath=").append(groupAvatarPath);
        sb.append(", needCheck=").append(needCheck);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}