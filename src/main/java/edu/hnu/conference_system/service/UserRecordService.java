package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.UserRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.result.Result;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.http.HttpResponse;

/**
* @author lenovo
* @description 针对表【user_record】的数据库操作Service
* @createDate 2024-11-15 16:13:16
*/
public interface UserRecordService extends IService<UserRecord> {

    Result getRecordList(Integer userId);

    Result deleteRecord(Long recordId);

    Result getRecordDetail(Long recordId) throws IOException;

    void insertRecord(Integer userId, Long meetingId);

    Result getFileList(Long recordId);

    //Result downloadFile(String fileId);

    Result downloadFile(HttpServletResponse response, String fileId);

    Result downloadAudio(HttpServletResponse response, Long recordId);
}
