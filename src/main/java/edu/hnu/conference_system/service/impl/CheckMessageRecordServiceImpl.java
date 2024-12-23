package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.CheckMessageRecord;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.CheckMessageRecordService;
import edu.hnu.conference_system.mapper.CheckMessageRecordMapper;
import edu.hnu.conference_system.vo.CheckMessageVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
        Integer recordId = checkMessageRecord.getRecordId();

        //插入接收方记录
        checkMessageRecord = new CheckMessageRecord();
        checkMessageRecord.setUserId(friendId);
        checkMessageRecord.setIsSender(0);
        checkMessageRecord.setResult(0);
        checkMessageRecord.setMessage(checkWords);
        checkMessageRecord.setAnotherId(senderId);
        checkMessageRecordMapper.insert(checkMessageRecord);

        return recordId;
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
        Integer recordId = checkMessageRecord.getRecordId();

        //插入接收方记录
        checkMessageRecord = new CheckMessageRecord();
        checkMessageRecord.setUserId(creatorId);
        checkMessageRecord.setIsSender(0);
        checkMessageRecord.setResult(0);
        checkMessageRecord.setGroupId(groupId);
        checkMessageRecord.setMessage(checkWords);
        checkMessageRecord.setAnotherId(senderId);
        checkMessageRecordMapper.insert(checkMessageRecord);

        return recordId;
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

    @Override
    public Result getOnesAllCheckMessage(Integer userId) {
        List<CheckMessageRecord> records = checkMessageRecordMapper.selectList(
                new QueryWrapper<CheckMessageRecord>().eq("user_id", userId)
        );
        List<CheckMessageVo> checkMessageVos = new ArrayList<>();
        for (CheckMessageRecord record : records) {
            CheckMessageVo checkMessageVo = new CheckMessageVo();
            checkMessageVo.setRecordId(record.getRecordId());
            checkMessageVo.setMessage(record.getMessage());
            checkMessageVo.setResult(record.getResult());
            if(record.getIsSender() == 1){
                //是发送方
                checkMessageVo.setSenderId(userId);
                checkMessageVo.setReceiverId(record.getAnotherId());
                checkMessageVo.setGroupId(record.getGroupId());
            }else if(record.getIsSender() == 0){
                //是接收方
                checkMessageVo.setSenderId(record.getAnotherId());
                checkMessageVo.setReceiverId(userId);
                checkMessageVo.setGroupId(record.getGroupId());
            }else{
                return Result.error("获取验证消息失败!");
            }
            checkMessageVos.add(checkMessageVo);
        }
        return Result.success(checkMessageVos);
    }

    @Override
    public void dealSameFriendRecord(Integer recordId,Integer result) {
        dealSameRecord(recordId, result);

    }

    @Override
    public void dealSameGroupRecord(Integer recordId, Integer check) {
        dealSameRecord(recordId, check);
    }

    private void dealSameRecord(Integer recordId, Integer check) {
        CheckMessageRecord checkMessageRecord = checkMessageRecordMapper.selectById(recordId);
        Integer userId1 = checkMessageRecord.getUserId();
        Integer userId2 = checkMessageRecord.getAnotherId();
        List<CheckMessageRecord> checkMessageRecords = checkMessageRecordMapper.selectList(
                new QueryWrapper<CheckMessageRecord>()
                        .eq("user_id", userId1).eq("another_id", userId2)
                        .or()
                        .eq("user_id", userId2).eq("another_id", userId1)
        );
        for(CheckMessageRecord record : checkMessageRecords){
            record.setResult(check);
        }
    }

}




