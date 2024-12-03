package edu.hnu.conference_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName check_message_record
 */
@TableName(value ="check_message_record")
@Data
public class CheckMessageRecord implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer recordId;
    /**
     * 
     */
    private Integer userId;

    /**
     *   0: 验证消息接收方  1: 验证消息发送方
     */
    private Integer isSender;

    /**
     *  对于接收方:   0: 未处理    1:已同意    2:已拒绝
     *  对于发送方:   0: 等待验证  1:已被同意  2:已被拒绝
     */
    private Integer result;

    /**
     *  如果是群聊验证,该条为群聊id; 若非,此条无效
     */
    private Integer groupId;

    /**
     * 对于接收方:  发送方id
     * 对于发送方:  接收方id
     */
    private Integer anotherId;

    /**
     *  验证消息
     */
    private String message;

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
        CheckMessageRecord other = (CheckMessageRecord) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getIsSender() == null ? other.getIsSender() == null : this.getIsSender().equals(other.getIsSender()))
            && (this.getResult() == null ? other.getResult() == null : this.getResult().equals(other.getResult()))
            && (this.getAnotherId() == null ? other.getAnotherId() == null : this.getAnotherId().equals(other.getAnotherId()))
            && (this.getMessage() == null ? other.getMessage() == null : this.getMessage().equals(other.getMessage()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getIsSender() == null) ? 0 : getIsSender().hashCode());
        result = prime * result + ((getResult() == null) ? 0 : getResult().hashCode());
        result = prime * result + ((getAnotherId() == null) ? 0 : getAnotherId().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", isSender=").append(isSender);
        sb.append(", result=").append(result);
        sb.append(", anotherId=").append(anotherId);
        sb.append(", message=").append(message);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}