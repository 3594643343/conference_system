package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.dto.KickDto;
import edu.hnu.conference_system.dto.MuteDto;
import edu.hnu.conference_system.dto.PmChangeDto;
import edu.hnu.conference_system.dto.UploadFileDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.MeetingService;
import edu.hnu.conference_system.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/meeting")
@Tag(name = "会议界面接口")
@Slf4j
public class MeetingController {
    @Resource
    MeetingService meetingService;
    @Resource
    RoomService roomService;


    @GetMapping("/leave")
    @Operation(summary = "退出会议")
    public Result leaveMeeting(){
        return roomService.leaveMeeting();
    }

    @GetMapping("/userlist")
    @Operation(summary = "在会用户信息(id、头像,昵称,个性签名)")
    public Result getUserInfo(@RequestParam("meetingNumber") String meetingNumber){
        return roomService.getUserInfo(meetingNumber);
    }

    @PutMapping("/mute")
    @Operation(summary = "禁言")
    public Result mute(@RequestBody MuteDto muteDto){
        return roomService.mute(muteDto);
    }

    @PutMapping("/dismute")
    @Operation(summary = "解除禁言")
    public Result dismute(@RequestBody MuteDto muteDto){
        return roomService.disMute(muteDto);
    }

    @PutMapping("/permissionchange")
    @Operation(summary = "修改与会者权限")
    public Result permissionChange(@RequestBody PmChangeDto pmChangeDto){
        return roomService.permissionChange(pmChangeDto);
    }


    @DeleteMapping("/kick")
    @Operation(summary = "踢人")
    public Result kickOneOut(@RequestBody KickDto kickDto){
        return roomService.kickOneOut(kickDto);
    }

    @PostMapping("/uploadfile")
    @Operation(summary = "上传文件")
    public Result uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("meetingNumber") String meetingNumber){
        UploadFileDto uploadFileDto = new UploadFileDto(meetingNumber,file);
        return roomService.uploadFile(uploadFileDto);
    }

}
