package edu.hnu.conference_system.controller;

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
    public Result<Long> register(@RequestBody Map<String, String> request) {
        String userName = request.get("userName");
        String userPassword = request.get("userPassword");
        String checkPassword = request.get("checkPassword");

        // 调用服务层方法进行注册
        long userId = userInfoService.userRegister(userName, userPassword, checkPassword);

        if (userId == -1) {
            return Result.error("注册失败");
        } else {
            return Result.success(userId);
        }
    }
}
