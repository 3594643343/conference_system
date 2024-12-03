package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.GroupChatRecord;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.GroupChatRecordService;
import edu.hnu.conference_system.mapper.GroupChatRecordMapper;
import edu.hnu.conference_system.utils.TimeUtils;
import edu.hnu.conference_system.vo.GroupRecordVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author lenovo
* @description 针对表【group_chat_record】的数据库操作Service实现
* @createDate 2024-12-01 11:11:32
*/
@Service
public class GroupChatRecordServiceImpl extends ServiceImpl<GroupChatRecordMapper, GroupChatRecord>
    implements GroupChatRecordService{

    @Resource
    private GroupChatRecordMapper groupChatRecordMapper;

    @Override
    public Result getRecord(Integer groupId) {
        List<GroupRecordVo> records = new ArrayList<>();
        List<GroupChatRecord> groupChatRecords = groupChatRecordMapper.selectList(
                new QueryWrapper<GroupChatRecord>().eq("group_id", groupId)
        );
        for (GroupChatRecord groupChatRecord : groupChatRecords) {
            GroupRecordVo groupRecordVo = new GroupRecordVo();
            groupRecordVo.setSpeakerId(groupChatRecord.getSpeakerId());
            groupRecordVo.setText(groupChatRecord.getText());
            groupRecordVo.setTime(groupChatRecord.getTime());
            records.add(groupRecordVo);
        }
        return Result.success(records);
    }

    @Override
    public void insertRecord(Integer groupId, Integer speakerId, String content, String time) {
        GroupChatRecord groupChatRecord = new GroupChatRecord();
        groupChatRecord.setGroupId(groupId);
        groupChatRecord.setSpeakerId(speakerId);
        groupChatRecord.setText(content);
        groupChatRecord.setTime(TimeUtils.string2LocalDateTime(time));
        groupChatRecordMapper.insert(groupChatRecord);
    }



}




