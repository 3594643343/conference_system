package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.UserRecord;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.*;
import edu.hnu.conference_system.mapper.UserRecordMapper;
import edu.hnu.conference_system.vo.RecordDetailVo;
import edu.hnu.conference_system.vo.RecordVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* @author lenovo
* @description 针对表【user_record】的数据库操作Service实现
* @createDate 2024-11-15 16:13:16
*/
@Service
public class UserRecordServiceImpl extends ServiceImpl<UserRecordMapper, UserRecord>
    implements UserRecordService{

    @Resource
    private UserRecordMapper userRecordMapper;
    @Resource
    private MeetingService meetingService;
    @Resource
    private UserInMeetingService userInMeetingService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private MeetingsMinutesService meetingsMinutesService;
    @Resource
    private RecordService recordService;
    @Resource
    private MeetingAudioService meetingAudioService;

    @Override
    public Result getRecordList(Long userId) {
        List<RecordVo> recordVos = new ArrayList<>();
        List<UserRecord> userRecords = userRecordMapper.selectList(
                new QueryWrapper<UserRecord>().eq("user_id", userId)
        );

        for(UserRecord userRecord : userRecords){
            Long meetingId = userRecord.getMeetingId();
            RecordVo recordVo = meetingService.buildRecordVoById(meetingId);
            recordVo.setRecordId(userRecord.getUserRecordId());

            List<Long> usersIds = userInMeetingService.getUsersIdsFromMeetingId(meetingId);

            for(Long usersId : usersIds){
                recordVo.getParticipants().add(userInfoService.getNameById(usersId));
            }
        }
        return Result.success(recordVos);

    }

    @Override
    public Result deleteRecord(Long recordId) {
        userRecordMapper.deleteById(recordId);
        return Result.success("成功删除记录!");
    }

    @Override
    public Result getRecordDetail(Long recordId) throws IOException {
        Long meetingId = userRecordMapper.selectById(recordId).getMeetingId();

        Long meetingMinutesId = meetingService.getMeetingMinutesId(meetingId);
        Long meetingRecordId = meetingService.getMeetingRecordId(meetingId);
        Long meetingAudioId = meetingService.getMeetingAudioId(meetingId);

        String minutes = meetingsMinutesService.getMinutesById(meetingMinutesId);
        String record = recordService.getRecordById(meetingRecordId);
        String audio = meetingAudioService.getAudioById(meetingAudioId);

        RecordDetailVo recordDetailVo = new RecordDetailVo();
        recordDetailVo.setMeetingRecord(record);
        recordDetailVo.setMeetingAudio(audio);
        recordDetailVo.setMeetingMinutes(minutes);

        return Result.success(recordDetailVo);
    }
}




