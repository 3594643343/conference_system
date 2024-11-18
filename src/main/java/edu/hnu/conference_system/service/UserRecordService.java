package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.UserRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.result.Result;

import java.io.IOException;

/**
* @author lenovo
* @description 针对表【user_record】的数据库操作Service
* @createDate 2024-11-15 16:13:16
*/
public interface UserRecordService extends IService<UserRecord> {

    Result getRecordList(Long userId);

    Result deleteRecord(Long recordId);

    Result getRecordDetail(Long recordId) throws IOException;

    void insertRecord(Long userId, Long meetingId);
}
