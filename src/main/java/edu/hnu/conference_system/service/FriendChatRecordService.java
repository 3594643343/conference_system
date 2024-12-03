package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.FriendChatRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.result.Result;

/**
* @author lenovo
* @description 针对表【friend_chat_record】的数据库操作Service
* @createDate 2024-12-01 11:11:28
*/
public interface FriendChatRecordService extends IService<FriendChatRecord> {

    Result getRecord(Integer userId, Integer friendId);

    void insertRecord(Integer senderId, Integer friendId, String content, String time);
}
