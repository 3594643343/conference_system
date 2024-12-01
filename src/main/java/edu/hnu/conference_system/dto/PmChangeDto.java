package edu.hnu.conference_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmChangeDto {
    /**
     * 要修改的用户id
     */
    private Integer id;

    /**
     * 所在的会议号
     */
    private String meetingNumber;

    /**
     * 要修改到的权限  与会者0, 管理员1
     */
    private int Permission;

}
