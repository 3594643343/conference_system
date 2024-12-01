package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoForAdmin {
    private Integer userId;
    private String userName;
    private String userEmail;
}
