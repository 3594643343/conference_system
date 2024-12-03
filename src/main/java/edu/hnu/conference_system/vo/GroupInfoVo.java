package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoVo {

    private Integer groupId;

    private String groupName;

    private Integer creatorId;

    private String creatorName;

    private String groupAvatar;

    private Boolean isIn;
}
