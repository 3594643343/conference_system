package edu.hnu.conference_system.controller;



import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.EmailLoginVo;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.vo.LoginVo;
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
@RequestMapping("/login")
@Tag(name = "登录接口")
@Slf4j
public class LoginController {
    @Resource
    UserInfoService userInfoService;

    /*
    用户 用户名密码登录
     */
    @PostMapping()//指定该方法处理post请求
    @Operation(summary = "密码登录")//使用swagger注解来描述该方法功能
    public Result namePassLogin(@RequestBody Map<String, String> request){
        return userInfoService.passLogin(request);
    }//接收客户端发送的json数据，并将其解析为一个map对象，方法体调用userInfoService.passLogin(result)
    //将结果返回客户端


    /*
    用户 邮箱和验证码登录
    */
    @PostMapping("/loginbyemail")
    @Operation(summary = "邮箱和验证码登录")
    public Result emailLogin(@RequestBody EmailLoginVo emailLoginVo) {
        log.info("邮箱和验证码登录 emailLoginVo = {}", emailLoginVo);
        return userInfoService.emailLogin(emailLoginVo);
    }




    /*
    用户 邮箱密码登录
     */
    /*@PostMapping("/emailpass")
    @Operation(summary = "邮箱密码登录")
    public Result emailLogin(@RequestBody Map<String, String> request){
        return userInfoService.emailLogin(request);
    }*/

    /*
    用户 验证码登录
     */
    //先不坐
    /*@PostMapping("/valicode")
    @Operation(summary = "验证码登录")
    public Result codeLogin(@RequestBody LoginDto loginDto){
        return userInfoService.codeLogin(loginDto);
    }*/
}