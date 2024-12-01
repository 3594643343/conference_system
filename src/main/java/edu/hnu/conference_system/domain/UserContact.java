package edu.hnu.conference_system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName user_contact
 */
@TableName(value ="user_contact")
@Data
public class UserContact implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer userId;

    /**
     * 
     */
    //@TableId
    private Integer userfriendId;

    /**
     * 
     */
    private Integer isFriend;

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
        UserContact other = (UserContact) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserfriendId() == null ? other.getUserfriendId() == null : this.getUserfriendId().equals(other.getUserfriendId()))
            && (this.getIsFriend() == null ? other.getIsFriend() == null : this.getIsFriend().equals(other.getIsFriend()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserfriendId() == null) ? 0 : getUserfriendId().hashCode());
        result = prime * result + ((getIsFriend() == null) ? 0 : getIsFriend().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userfriendId=").append(userfriendId);
        sb.append(", isFriend=").append(isFriend);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}