package edu.hnu.conference_system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo {
    private Integer userId;
    private Boolean isAdmin;
    private String Token;
}
