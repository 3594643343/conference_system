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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping("/change/info")
    @Operation(summary = "修改用户信息")
    public Result changeUserInfo(@RequestBody UserInfoDto userInfoDto){
        return userInfoService.changeUserInfo(userInfoDto);
    }

    @PostMapping("/change/needCheck")
    @Operation(summary = "修改是否需要验证")
    public Result changeNeedCheck(@RequestParam("needCheck") Integer needCheck){
        return userInfoService.changeNeedCheck(needCheck);
    }

    @PutMapping("/change/avatar")
    @Operation(summary = "修改头像")
    public Result changeAvatar(@RequestParam("avatar") MultipartFile avatar) throws IOException {
        return userInfoService.changeAvatar(avatar);
    }

    @PutMapping("/change/password")
    @Operation(summary = "修改用户密码")
    public Result changePassword(PasswordChangeDto passwordChangeDto){
        return userInfoService.changePassword(passwordChangeDto);
    }
}
