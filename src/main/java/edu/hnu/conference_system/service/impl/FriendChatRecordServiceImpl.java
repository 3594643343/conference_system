package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.FriendChatRecord;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.FriendChatRecordService;
import edu.hnu.conference_system.mapper.FriendChatRecordMapper;
import edu.hnu.conference_system.utils.TimeUtils;
import edu.hnu.conference_system.vo.FriendChatVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author lenovo
* @description 针对表【friend_chat_record】的数据库操作Service实现
* @createDate 2024-12-01 11:11:28
*/
@Service
public class FriendChatRecordServiceImpl extends ServiceImpl<FriendChatRecordMapper, FriendChatRecord>
    implements FriendChatRecordService{

    @Resource
    private FriendChatRecordMapper friendChatRecordMapper;


    @Override
    public Result getRecord(Integer userId, Integer friendId) {
        LambdaQueryWrapper<FriendChatRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendChatRecord::getSpeakerId, userId)
                .eq(FriendChatRecord::getFriendId, friendId)
                .or()
                .eq(FriendChatRecord::getSpeakerId, friendId)
                .eq(FriendChatRecord::getFriendId, userId);

        List<FriendChatRecord> list = friendChatRecordMapper.selectList(queryWrapper);
        List<FriendChatVo> voList = new ArrayList<>();
        for (FriendChatRecord friendChatRecord : list) {
            FriendChatVo vo = new FriendChatVo();
            vo.setSpeakerId(friendChatRecord.getSpeakerId());
            vo.setText(friendChatRecord.getText());
            vo.setTime(friendChatRecord.getTime());
            voList.add(vo);
        }
        return Result.success(voList);
    }

    @Override
    public void insertRecord(Integer senderId, Integer friendId, String content, String time) {
        FriendChatRecord friendChatRecord = new FriendChatRecord();
        friendChatRecord.setSpeakerId(senderId);
        friendChatRecord.setFriendId(friendId);
        friendChatRecord.setText(content);
        friendChatRecord.setTime(TimeUtils.string2LocalDateTime(time));
        friendChatRecordMapper.insert(friendChatRecord);
    }
}




