package edu.hnu.conference_system.controller;

import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/exit")
@Tag(name = "退出系统接口")
@Slf4j
public class ExitController {

    @Autowired
    UserInfoService userInfoService;

    @DeleteMapping()
    @Operation(summary = "退出系统")
    public Result exit(){
        int f = userInfoService.exitSystem(UserHolder.getUserId());
        if(f == 1){
            return Result.success("成功退出系统!");
        }else{
            return Result.error("退出系统失败!");
        }

    }
}
