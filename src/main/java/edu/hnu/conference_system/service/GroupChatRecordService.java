package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.GroupChatRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.result.Result;

/**
* @author lenovo
* @description 针对表【group_chat_record】的数据库操作Service
* @createDate 2024-12-01 11:11:32
*/
public interface GroupChatRecordService extends IService<GroupChatRecord> {

    Result getRecord(Integer groupId);

    void insertRecord(Integer groupId, Integer speakerId, String content, String time);
}
