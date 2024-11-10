package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meeting")
@Tag(name = "会议界面接口")
@Slf4j
public class MeetingController {
    @Resource
    MeetingService meetingService;

    @GetMapping("/leave")
    @Operation(summary = "退出会议")
    public void leaveMeeting(){
        meetingService.leaveMeeting();
    }
}
