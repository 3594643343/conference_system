package edu.hnu.conference_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    //private Long userId;

    private String userName;

    private String signature;

    /**
     * 加好友是否需要验证 0: 不需要  1:需要
     */
    private Integer needCheck;

    //private MultipartFile avatar;
}
