package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendVo {
    private Integer friendId;

    private String friendName;

    private String friendAvatar;

    private String friendEmail;

    private String friendSignature;

    private Boolean isFriend;
}
