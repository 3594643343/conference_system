package edu.hnu.conference_system.controller;

import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.dto.MeetingDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.MeetingService;
import edu.hnu.conference_system.service.RoomService;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.vo.MeetingInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
@Tag(name = "日程")
@Slf4j
public class ScheduleController {

        @Resource
        MeetingService meetingService;
        UserInfoService userService;
        @Resource
        RoomService roomService;

        @GetMapping("/show")
        @Operation(summary = "显示日程")
        public Result<MeetingInfoVo> getMeetingInfo(@RequestBody Map<String,String> request){
            Long userId = Long.parseLong(request.get("userId"));
            User user = userService.getUserById(userId);
            List<Meeting> userMeetings = meetingService.getUserMeetings(user);
            List<MeetingInfoVo> meetingInfoVos = userMeetings.stream()
                    .map(meetingService::buildMeetingInfoVo)
                    .collect(Collectors.toList());
            for(MeetingInfoVo meetingInfoVo :meetingInfoVos){
                if((Result.success(meetingInfoVo)).equals(Result.error("失败"))){
                    return Result.error("显示日程失败");
                }
            }
            return Result.success();
        }

        @GetMapping("/add")
        @Operation(summary = "添加日程")
        public Result<Void> addMeeting(@RequestParam String meetingNumber, @RequestParam String meetingPassword) {
            try {
                if (!meetingService.validateMeeting(meetingNumber, meetingPassword)) {
                    return Result.error("会议号或会议密码不正确");
                }
                Meeting meeting = new Meeting();
                meeting.setMeetingNumber(meetingNumber);
                meeting.setMeetingPassword(meetingPassword);
                meetingService.addMeeting(meeting);

                return Result.success(null);
            } catch (Exception e) {
                return Result.error("添加会议失败: " + e.getMessage());
            }
        }

        @PostMapping("/join")
        @Operation(summary = "加入日程会议")
        public Result joinscheduleMeeting(){
            return meetingService.joinscheduleMeeting();
        }

        @GetMapping("/cancle")
        @Operation(summary = "取消日程")
        public Result getUserInfo(@RequestBody Map<String,String> request){
            return roomService.getUserInfo(request);
        }
    }
