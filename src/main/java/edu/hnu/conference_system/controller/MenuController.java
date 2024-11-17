package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.dto.BookMeetingDto;
import edu.hnu.conference_system.dto.JoinMeetingDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.MeetingService;
import edu.hnu.conference_system.service.RoomService;
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
    MeetingService meetingService;

    //TODO 创建会议后将会议加入自己的日程
    @PostMapping("/book")
    @Operation(summary = "预定会议")
    public Result bookMeeting(@RequestBody BookMeetingDto bookMeetingDto) {
        return meetingService.bookMeeting(bookMeetingDto);
    }

    @GetMapping("/quick")
    @Operation(summary = "快速会议")
    public Result quickMeeting(){
        return meetingService.quickMeeting();
    }

    @PostMapping("/join")
    @Operation(summary = "加入会议")
    public Result joinMeeting(@RequestBody JoinMeetingDto joinMeetingDto) {
        return meetingService.joinMeeting(joinMeetingDto);
    }
}
