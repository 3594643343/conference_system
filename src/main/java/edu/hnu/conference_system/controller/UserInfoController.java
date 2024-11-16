package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.dto.PasswordChangeDto;
import edu.hnu.conference_system.dto.UserInfoDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.vo.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/userinfo")
@Tag(name = "用户信息")
@Slf4j
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @GetMapping("/get")
    @Operation(summary = "获取用户信息")
    public UserInfoVo getUserInfo() {
        return userInfoService.buildUserInfoVo(UserHolder.getUserId());
    }

    @PutMapping("/change")
    @Operation(summary = "修改用户信息")
    public Result changeUserInfo(UserInfoDto userInfoDto) throws IOException {
        return userInfoService.changeUserInfo(userInfoDto);
    }

    @PutMapping("/changepassword")
    @Operation(summary = "修改用户密码")
    public Result changePassword(PasswordChangeDto passwordChangeDto){
        return userInfoService.changePassword(passwordChangeDto);
    }
}
