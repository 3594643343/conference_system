package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.dto.AddFriendDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.CheckMessageRecordService;
import edu.hnu.conference_system.service.FriendChatRecordService;
import edu.hnu.conference_system.service.UserContactService;
import edu.hnu.conference_system.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
@Tag(name = "好友接口")
@Slf4j
public class FriendController {

    @Resource
    UserContactService userContactService;

    @Resource
    UserInfoService userInfoService;

    @Resource
    FriendChatRecordService friendChatRecordService;

    @Resource
    CheckMessageRecordService checkMessageRecordService;

    @GetMapping("/get/allFriendId")
    @Operation(summary = "获取用户所有好友id")
    public Result getOnesAllFriend() {
        return userContactService.getOnesAllFriend(UserHolder.getUserId());
    }

    @GetMapping("/get/friendInfo")
    @Operation(summary = "获取一个好友信息(id、头像,昵称,个性签名)")
    public Result getFriendInfo(@RequestParam("friendId") Integer friendId) {
        return Result.success(userInfoService.getUserInfo(friendId));
    }

    @GetMapping("/get/allFriendInfo")
    @Operation(summary = "获取所有好友信息(id、头像,昵称,个性签名)")
    public Result getAllFriendInfo() {
        return userContactService.getAllFriendInfo(UserHolder.getUserId());
    }

    @PostMapping("/search")
    @Operation(summary = "搜索用户")
    public Result searchFriend(@RequestParam("friendId") Integer friendId){

        return userContactService.searchById(friendId);
    }

    @PostMapping("/add")
    @Operation(summary = "申请添加好友")
    public Result addFriend(@RequestBody AddFriendDto addFriendDto) {
        return userContactService.addFriend(addFriendDto.getFriendId(),addFriendDto.getCheckWords());
    }

    @PostMapping("/deal")
    @Operation(summary = "处理添加好友验证")
    public Result dealFriendCheck(@RequestParam("recordId") Integer recordId,@RequestParam("friendId") Integer friendId, @RequestParam("check") Integer check ) {
        return userContactService.dealCheck(recordId,friendId,check);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除好友")
    public Result deleteFriend(@RequestParam("friendId") Integer friendId) {
        return userContactService.deleteFriend(friendId);
    }

    @GetMapping("/record")
    @Operation(summary = "获取与一个好友的聊天记录")
    public Result getFriendRecord(@RequestParam("friendId") Integer friendId) {
        return friendChatRecordService.getRecord(UserHolder.getUserId(),friendId);
    }

    @GetMapping("/checkmessage")
    @Operation(summary = "获取一个人的所有验证消息")
    public Result getAllCheckMessage(){
        return checkMessageRecordService.getOnesAllCheckMessage(UserHolder.getUserId());
    }


}
