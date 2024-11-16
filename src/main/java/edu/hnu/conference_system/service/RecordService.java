package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.Record;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;

/**
* @author lenovo
* @description 针对表【record】的数据库操作Service
* @createDate 2024-11-11 18:59:03
*/
public interface RecordService extends IService<Record> {

    String getRecordById(Long meetingRecordId) throws IOException;
}
