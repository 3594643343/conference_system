package edu.hnu.conference_system.controller;


import com.alibaba.fastjson.JSON;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.RecordService;
import edu.hnu.conference_system.service.UserInMeetingService;
import edu.hnu.conference_system.service.UserRecordService;
import edu.hnu.conference_system.utils.ServletUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
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
    @Operation(summary = "查看详情(已废弃)")
    public Result getRecordDetail(@RequestParam("recordId") Long recordId) throws IOException {
        return userRecordService.getRecordDetail(recordId);
    }

    @GetMapping("/download/audio")
    @Operation(summary = "下载记录中音频")
    public Result downloadAudio(HttpServletResponse response, @RequestParam("fileId") Long recordId){
        return userRecordService.downloadAudio(response,recordId);
    }

    @GetMapping("/get/fileList")
    @Operation(summary = "获取某个记录中文件列表")
    public Result getFileList(@RequestParam("recordId") Long recordId){
        return userRecordService.getFileList(recordId);
    }

    @GetMapping("/download")
    @Operation(summary = "下载某个文件")
    public void downloadFile(HttpServletResponse response, @RequestParam("fileId") String fileId){
        try{
            userRecordService.downloadFile(response,fileId);
        }catch (Exception e){
            e.printStackTrace();
            response.reset();
            Result result = Result.error(e.getMessage());
            String json = JSON.toJSONString(result);
            ServletUtils.renderString(response, json);
        }
    }
}
