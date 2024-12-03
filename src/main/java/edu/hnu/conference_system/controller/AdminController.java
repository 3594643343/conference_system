package edu.hnu.conference_system.controller;


import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hnu.conference_system.domain.UserInfo;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.vo.UserInfoForAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@Tag(name = "管理员接口")
@Slf4j
public class AdminController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/getall")
    @Operation(summary = "获取全部用户信息")
    public Result getAllUsers() {
        return userInfoService.getAllUsers();
    }

    /*@DeleteMapping("/delete")
    @Operation(summary = "删除用户")
    public Result deleteUser(@RequestParam("userId") Long userId) {
        return userInfoService.deleteById(userId);
    }*/

    @GetMapping("/reset")
    @Operation(summary = "重置密码")
    public Result resetPassword(@RequestParam("userId") Integer userId) {
        return userInfoService.resetPassword(userId);
    }
}
