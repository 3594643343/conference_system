package edu.hnu.conference_system.controller;

import edu.hnu.conference_system.dto.JoinMeetingDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
@Tag(name = "日程")
@Slf4j
public class ScheduleController {

        @Resource
        private ScheduleService scheduleService;

        @GetMapping("/show")
        @Operation(summary = "显示日程")
        public Result getSchedule() {
            return scheduleService.show(UserHolder.getUserId());
        }

        @PostMapping("/add")
        @Operation(summary = "添加日程")
        public Result addSchedule(@RequestBody JoinMeetingDto joinMeetingDto) {
            return scheduleService.add(UserHolder.getUserId(),joinMeetingDto);
        }

        @GetMapping("/join")
        @Operation(summary = "加入日程会议")
        public Result joinSchedule(@RequestParam("meetingId") Long meetingId,@RequestParam("meetingnumber") String meetingnumber) {
            return scheduleService.join(meetingId,meetingnumber);
        }

        @DeleteMapping("/cancle")
        @Operation(summary = "取消日程")
        public Result cancelSchedule(@RequestParam("meetingid") Long meetingId) {
            return scheduleService.cancel(UserHolder.getUserId(),meetingId);
        }
    }
