package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.dto.BookMeetingDto;
import edu.hnu.conference_system.dto.JoinMeetingDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.MeetingService;
import edu.hnu.conference_system.service.RoomService;
import edu.hnu.conference_system.service.ScheduleService;
import edu.hnu.conference_system.vo.CreateMeetingVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/menu")
@Tag(name = "会议菜单接口")
@Slf4j
public class MenuController {
    @Resource
    RoomService roomService;
    @Resource
    ScheduleService scheduleService;

    //TODO 创建会议后将会议加入自己的日程
    @PostMapping("/book")
    @Operation(summary = "预定会议")
    public Result bookMeeting(@RequestBody BookMeetingDto bookMeetingDto) {
        CreateMeetingVo vo = roomService.bookMeeting(bookMeetingDto);
        scheduleService.add(UserHolder.getUserId(),new JoinMeetingDto(vo.getMeetingNumber(), vo.getMeetingPassword()));
        return Result.success(vo);
    }

    @GetMapping("/quick")
    @Operation(summary = "快速会议")
    public Result quickMeeting(){
        return roomService.quickMeeting();
    }

    @PostMapping("/join")
    @Operation(summary = "加入会议")
    public Result joinMeeting(@RequestBody JoinMeetingDto joinMeetingDto) {
        return roomService.joinMeeting(joinMeetingDto);
    }
}
