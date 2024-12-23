package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.CheckMessageRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.result.Result;

/**
* @author lenovo
* @description 针对表【check_message_record】的数据库操作Service
* @createDate 2024-12-03 22:08:35
*/
public interface CheckMessageRecordService extends IService<CheckMessageRecord> {

    Result getCheckMessageRecord(Integer userId);

    Integer initFriendCheck(Integer senderId, Integer friendId, String checkWords);

    Integer initGroupCheck(Integer senderId, Integer groupId, Integer creatorId, String checkWords);

    void refuseFriendCheck(Integer recordId);

    void passFriendCheck(Integer recordId);

    void refuseGroupCheck(Integer recordId);

    void passGroupCheck(Integer recordId);

    Result getOnesAllCheckMessage(Integer userId);

    void dealSameFriendRecord(Integer recordId,Integer result);

    void dealSameGroupRecord(Integer recordId, Integer check);
}
