package edu.hnu.conference_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String userName ;

    private String userEmail ;

    private String userPassword ;

    private String checkPassword ;
}
