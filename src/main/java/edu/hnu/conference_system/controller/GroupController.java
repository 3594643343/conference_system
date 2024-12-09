package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.dto.AddGroupDto;
import edu.hnu.conference_system.dto.ChangeGroupDto;
import edu.hnu.conference_system.dto.CreateGroupDto;
import edu.hnu.conference_system.dto.KickDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.ChatGroupService;
import edu.hnu.conference_system.service.GroupChatRecordService;
import edu.hnu.conference_system.service.UserAndGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/group")
@Tag(name = "群聊接口")
@Slf4j
public class GroupController {

    @Resource
    ChatGroupService chatGroupService;

    @Resource
    UserAndGroupService userAndGroupService;

    @Resource
    GroupChatRecordService groupChatRecordService;


    @GetMapping("/get/isin")
    @Operation(summary = "获取是否在该群中")
    public Result getIsIn(@RequestParam("groupId") Integer groupId) {
        return userAndGroupService.getIsIn(UserHolder.getUserId(),groupId);
    }

    @GetMapping("/get/allGroupId")
    @Operation(summary = "获取用户所有群聊id")
    public Result getOnesAllGroup() {
        return userAndGroupService.getOnesAllGroup(UserHolder.getUserId());
    }

    @PostMapping("/create")
    @Operation(summary = "创建群聊")
    public Result createGroup(CreateGroupDto groupDto) {
        return chatGroupService.createGroup(groupDto);
    }

    @GetMapping("/showGroupInfo")
    @Operation(summary = "获得一个群聊信息(群名称、头像、群主id、群主用户名)")
    public Result showGroupInfo(@RequestParam("groupId") Integer groupId) {
        return chatGroupService.getGroupInfo(groupId);
    }

    @GetMapping("/getGroupMembers")
    @Operation(summary = "获得群聊成员(id、头像、用户名)")
    public Result getGroupMembers(@RequestParam("groupId") Integer groupId) {
        return userAndGroupService.getMembers(groupId);
    }

    @PostMapping("/change")
    @Operation(summary = "修改群聊信息")
    public Result changeGroupInfo(ChangeGroupDto groupDto){
        return chatGroupService.changeGroupInfo(groupDto);
    }

    @PostMapping("/disband")
    @Operation(summary = "解散群聊")
    public Result disbandGroup(@RequestParam("groupId") Integer groupId) {
        //chatGroupService.deleteGroup(groupId);
        userAndGroupService.deleteGroup(groupId);
        return Result.success("已解散群聊!");
    }

    @PostMapping("/search")
    @Operation(summary = "搜索群聊")
    public Result searchGroup(@RequestParam("groupId") Integer groupId){
        return userAndGroupService.searchGroup(groupId);
    }

    @PostMapping("/add")
    @Operation(summary = "申请加入群聊")
    public Result addGroup(@RequestBody AddGroupDto addGroupDto) {
        return userAndGroupService.addGroup(addGroupDto.getGroupId(),addGroupDto.getCheckWords());
    }

    @PostMapping("/deal")
    @Operation(summary = "处理添加群聊验证")
    public Result dealGroupCheck(@RequestParam("recordId") Integer recordId,@RequestParam("userId") Integer userId
            ,@RequestParam("groupId") Integer groupId, @RequestParam("check") Integer check ) {
        return userAndGroupService.dealCheck(recordId,userId,groupId,check);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "退出群聊")
    public Result leaveGroup(@RequestParam("groupId") Integer groupId) {
        if(Objects.equals(UserHolder.getUserId(), chatGroupService.getGroupCreatorId(groupId))){
            return disbandGroup(groupId);
        }
        else{
            return userAndGroupService.leaveGroup(UserHolder.getUserId(),groupId);
        }
    }

    @DeleteMapping("/kick")
    @Operation(summary = "踢人")
    public Result kickOneOut(@RequestParam("groupId") Integer groupId,@RequestParam("userId") Integer userId){
        return userAndGroupService.kickOneOut(groupId,userId);
    }

    @GetMapping("/record")
    @Operation(summary = "获取一个群的聊天记录")
    public Result getGroupRecord(@RequestParam("groupId") Integer groupId) {
        return groupChatRecordService.getRecord(groupId);
    }

}
