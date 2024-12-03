package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.CheckMessageRecord;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.CheckMessageRecordService;
import edu.hnu.conference_system.mapper.CheckMessageRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【check_message_record】的数据库操作Service实现
* @createDate 2024-12-03 22:08:35
*/
@Service
public class CheckMessageRecordServiceImpl extends ServiceImpl<CheckMessageRecordMapper, CheckMessageRecord>
    implements CheckMessageRecordService{


    @Resource
    private CheckMessageRecordMapper checkMessageRecordMapper;


    @Override
    public Result getCheckMessageRecord(Integer userId) {
        return null;
    }

    @Override
    public Integer initFriendCheck(Integer senderId, Integer friendId, String checkWords) {
        CheckMessageRecord checkMessageRecord = new CheckMessageRecord();

        //插入发送方记录
        checkMessageRecord.setUserId(senderId);
        checkMessageRecord.setIsSender(1);
        checkMessageRecord.setResult(0);
        checkMessageRecord.setMessage(checkWords);
        checkMessageRecord.setAnotherId(friendId);
        checkMessageRecordMapper.insert(checkMessageRecord);

        //插入接收方记录
        checkMessageRecord.setUserId(friendId);
        checkMessageRecord.setIsSender(0);
        checkMessageRecord.setResult(0);
        checkMessageRecord.setMessage(checkWords);
        checkMessageRecord.setAnotherId(senderId);
        checkMessageRecordMapper.insert(checkMessageRecord);

        return checkMessageRecord.getRecordId();
    }

    @Override
    public Integer initGroupCheck(Integer senderId, Integer groupId, Integer creatorId, String checkWords) {
        CheckMessageRecord checkMessageRecord = new CheckMessageRecord();

        //插入发送方记录
        checkMessageRecord.setUserId(senderId);
        checkMessageRecord.setIsSender(1);
        checkMessageRecord.setResult(0);
        checkMessageRecord.setGroupId(groupId);
        checkMessageRecord.setMessage(checkWords);
        checkMessageRecord.setAnotherId(creatorId);
        checkMessageRecordMapper.insert(checkMessageRecord);

        //插入接收方记录
        checkMessageRecord.setUserId(creatorId);
        checkMessageRecord.setIsSender(0);
        checkMessageRecord.setResult(0);
        checkMessageRecord.setGroupId(groupId);
        checkMessageRecord.setMessage(checkWords);
        checkMessageRecord.setAnotherId(senderId);
        checkMessageRecordMapper.insert(checkMessageRecord);

        return checkMessageRecord.getRecordId();
    }

    @Override
    public void refuseFriendCheck(Integer recordId) {
        CheckMessageRecord checkMessageRecord = new CheckMessageRecord();
        checkMessageRecord.setResult(2);
        checkMessageRecordMapper.update(checkMessageRecord, new UpdateWrapper<CheckMessageRecord>()
                .eq("record_id", recordId)
        );
    }

    @Override
    public void passFriendCheck(Integer recordId) {
        CheckMessageRecord checkMessageRecord = new CheckMessageRecord();
        checkMessageRecord.setResult(1);
        checkMessageRecordMapper.update(checkMessageRecord, new UpdateWrapper<CheckMessageRecord>()
                .eq("record_id", recordId)
        );
    }

    @Override
    public void refuseGroupCheck(Integer recordId) {
        CheckMessageRecord checkMessageRecord = new CheckMessageRecord();
        checkMessageRecord.setResult(2);
        checkMessageRecordMapper.update(checkMessageRecord, new UpdateWrapper<CheckMessageRecord>()
                .eq("record_id", recordId)
        );
    }

    @Override
    public void passGroupCheck(Integer recordId) {
        CheckMessageRecord checkMessageRecord = new CheckMessageRecord();
        checkMessageRecord.setResult(1);
        checkMessageRecordMapper.update(checkMessageRecord, new UpdateWrapper<CheckMessageRecord>()
                .eq("record_id", recordId)
        );
    }

}




