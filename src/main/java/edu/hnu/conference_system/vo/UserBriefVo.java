package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBriefVo {
    private Long id;
    private String username;
    private MultipartFile avatar;
}
