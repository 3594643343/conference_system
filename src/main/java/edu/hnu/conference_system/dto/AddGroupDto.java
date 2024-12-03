package edu.hnu.conference_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddGroupDto {
    private Integer groupId;
    /**
     * 验证消息
     */
    private String checkWords;
}
