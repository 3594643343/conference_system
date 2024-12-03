package edu.hnu.conference_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateGroupDto {

    private String groupName;

    private MultipartFile groupAvatar;

    /**
     * 加群是否需要验证 0为否  1为是
     */
    private Integer needCheck;
}
