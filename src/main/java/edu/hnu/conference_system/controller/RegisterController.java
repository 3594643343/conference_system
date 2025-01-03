package edu.hnu.conference_system.controller;

import edu.hnu.conference_system.dto.RegisterDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/register")
@Tag(name = "注册接口")
@Slf4j
public class RegisterController {
    @Resource
    UserInfoService userInfoService;
    /*
     * 用户注册
     */
    @PostMapping
    @Operation(summary = "用户注册")
    public Result register(@RequestBody RegisterDto registerDto) throws IOException {
        String userName = registerDto.getUserName();
        String userEmail = registerDto.getUserEmail();
        String userPassword = registerDto.getUserPassword();
        String checkPassword = registerDto.getCheckPassword();

        // 调用服务层方法进行注册
        return userInfoService.userRegister(userName, userEmail,userPassword, checkPassword);
    }
}
