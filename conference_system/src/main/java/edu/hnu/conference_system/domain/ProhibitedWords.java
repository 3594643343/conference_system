package edu.hnu.conference_system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName prohibited_words
 */
@TableName(value ="prohibited_words")
@Data
public class ProhibitedWords implements Serializable {
    /**
     * 
     */
    @TableId
    private String prohibitedWordId;

    /**
     * 
     */
    private String prohibitedWord;

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
        ProhibitedWords other = (ProhibitedWords) that;
        return (this.getProhibitedWordId() == null ? other.getProhibitedWordId() == null : this.getProhibitedWordId().equals(other.getProhibitedWordId()))
            && (this.getProhibitedWord() == null ? other.getProhibitedWord() == null : this.getProhibitedWord().equals(other.getProhibitedWord()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getProhibitedWordId() == null) ? 0 : getProhibitedWordId().hashCode());
        result = prime * result + ((getProhibitedWord() == null) ? 0 : getProhibitedWord().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", prohibitedWordId=").append(prohibitedWordId);
        sb.append(", prohibitedWord=").append(prohibitedWord);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}