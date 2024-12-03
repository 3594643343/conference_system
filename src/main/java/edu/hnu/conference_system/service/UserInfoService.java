package edu.hnu.conference_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.domain.UserInfo;
import edu.hnu.conference_system.dto.LoginDto;
import edu.hnu.conference_system.dto.PasswordChangeDto;
import edu.hnu.conference_system.dto.UserInfoDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.EmailLoginVo;
import edu.hnu.conference_system.vo.UserInfoVo;
//import edu.hnu.conference_system.vo.UserBriefVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author lenovo
* &#064;description  针对表【user_info】的数据库操作Service
* &#064;createDate  2024-11-07 10:29:00
 */
public interface UserInfoService extends IService<UserInfo> {

    Result passLogin(LoginDto loginDto);
    Result emailLogin(EmailLoginVo emailLoginVo);

    Result userRegister(String userName,String userEmail ,String userPassword, String checkPassword) throws IOException;

    UserInfoVo buildUserInfoVo(Integer id);

    String getNameById(Integer userId);

    Result changeUserInfo(UserInfoDto userInfoDto);

    Result changePassword(PasswordChangeDto passwordChangeDto);

    Result changeAvatar(MultipartFile avatar) throws IOException;

    Result resetPassword(Integer userId);

    Long getMeetingIdByUserId(Integer userId);

    String getUserAvatar(Integer userId);


    //Result getUserInfo(Long );

    //UserBriefVo buildUserBriefVo(Long id);

    /*Result codeLogin(LoginDto loginDto);*/

}
