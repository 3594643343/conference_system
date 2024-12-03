package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.dto.ChangeGroupDto;
import edu.hnu.conference_system.dto.CreateGroupDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.ChatGroupService;
import edu.hnu.conference_system.service.UserAndGroupService;
import edu.hnu.conference_system.service.UserContactService;
import edu.hnu.conference_system.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/friend8group")
@Tag(name = "好友与群聊接口")
@Slf4j
public class FriendAndGroupController {


    @Resource
    ChatGroupService chatGroupService;

    @Resource
    UserContactService userContactService;

    @Resource
    UserAndGroupService userAndGroupService;

    @PostMapping("/createGroup")
    @Operation(summary = "创建群聊")
    public Result createGroup(CreateGroupDto groupDto) {
        return chatGroupService.createGroup(groupDto);
    }

    @GetMapping("/showGroupInfo")
    @Operation(summary = "获得一个群聊信息(群名称、头像)")
    public Result showGroupInfo(@RequestParam("groupId") Integer groupId) {
        return chatGroupService.getGroupInfo(groupId);
    }

    @GetMapping("/getGroupMembers")
    @Operation(summary = "获得群聊成员(头像、用户名)")
    public Result getGroupMembers(@RequestParam("groupId") Integer groupId) {
        return userAndGroupService.getMembers(groupId);
    }

    @PostMapping("/changeGroup")
    @Operation(summary = "修改群聊信息")
    public Result changeGroupInfo(@RequestBody ChangeGroupDto groupDto){
        return chatGroupService.changeGroupInfo(groupDto);
    }

    @PostMapping("/disbandGroup")
    @Operation(summary = "解散群聊")
    public Result disbandGroup(@RequestParam("groupId") Integer groupId) {
        chatGroupService.deleteGroup(groupId);
        userAndGroupService.deleteGroup(groupId);
        return Result.success("已解散群聊!");
    }

    @PostMapping("/search/friend")
    @Operation(summary = "搜索用户")
    public Result searchFriend(@RequestParam("friendId") Integer friendId){

        return userContactService.searchById(friendId);
    }

    @PostMapping("/search/group")
    @Operation(summary = "搜索群聊")
    public Result searchGroup(@RequestParam("groupId") Integer groupId){
        return userAndGroupService.searchGroup(groupId);
    }

    @PostMapping("/add/friend")
    @Operation(summary = "申请添加好友")
    public Result addFriend(@RequestParam("friendId") Integer friendId) {
        return userContactService.addFriend(friendId);
    }

    @PostMapping("/add/group")
    @Operation(summary = "申请加入群聊")
    public Result addGroup(@RequestParam("groupId") Integer groupId) {
        return userAndGroupService.addGroup(groupId);
    }

    @DeleteMapping("/delete/friend")
    @Operation(summary = "删除好友")
    public Result deleteFriend(@RequestParam("friendId") Integer friendId) {
        return userContactService.deleteFriend(friendId);
    }

    @DeleteMapping("/delete/group")
    @Operation(summary = "退出群聊")
    public Result leaveGroup(@RequestParam("groupId") Integer groupId) {
        if(Objects.equals(UserHolder.getUserId(), chatGroupService.getGroupCreatorId(groupId))){
            return disbandGroup(groupId);
        }
        else{
            return userAndGroupService.leaveGroup(UserHolder.getUserId(),groupId);
        }
    }

}
