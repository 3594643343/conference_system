package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.dto.LoginDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.LoginVo;
import edu.hnu.conference_system.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "登录接口")
@Slf4j
public class LoginController {
    @Resource
    UserInfoService userInfoService;

    @PostMapping
    @Operation(summary = "用户登录")
    /*
    用户 用户名密码登录
     */
    public Result login(@RequestBody LoginDto loginDto){
        return userInfoService.userLogin(loginDto);
    }
}
