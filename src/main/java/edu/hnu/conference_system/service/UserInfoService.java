package edu.hnu.conference_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.domain.UserInfo;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.LoginVo;

import java.util.Map;

/**
* @author lenovo
* @description 针对表【user_info】的数据库操作Service
* @createDate 2024-11-07 10:29:00
*/
public interface UserInfoService extends IService<UserInfo> {

    Result passLogin(Map<String, String> request);

    /*Result codeLogin(LoginDto loginDto);*/

}
