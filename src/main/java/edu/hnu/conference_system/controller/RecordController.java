package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.RecordService;
import edu.hnu.conference_system.service.UserInMeetingService;
import edu.hnu.conference_system.service.UserRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/record")
@Tag(name = "会议记录接口")
@Slf4j
public class RecordController {

    @Resource
    private UserRecordService userRecordService;

    @GetMapping("/list")
    @Operation(summary = "获得记录列表")
    public Result getRecordList(){
        return userRecordService.getRecordList(UserHolder.getUserId());
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除记录")
    public Result deleteRecord(@RequestParam("recordId") Long recordId){
        return userRecordService.deleteRecord(recordId);
    }

    @GetMapping("/detail")
    @Operation(summary = "查看详情")
    public Result getRecordDetail(@RequestParam("recordId") Long recordId) throws IOException {
        return userRecordService.getRecordDetail(recordId);
    }
}
