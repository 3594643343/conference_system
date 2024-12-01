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
    public Result getRecordList(Integer userId) {
        List<RecordVo> recordVos = new ArrayList<>();
        List<UserRecord> userRecords = userRecordMapper.selectList(
                new QueryWrapper<UserRecord>().eq("user_id", userId)
        );
        System.out.println("共有记录条数: "+userRecords.size());

        for(UserRecord userRecord : userRecords){
            Long meetingId = userRecord.getMeetingId();
            RecordVo recordVo = meetingService.buildRecordVoById(meetingId);
            recordVo.setRecordId(userRecord.getUserRecordId());

            List<Integer> usersIds = userInMeetingService.getUsersIdsFromMeetingId(meetingId);

            for(Integer usersId : usersIds){
                recordVo.getParticipants().add(userInfoService.getNameById(usersId));
            }
            recordVos.add(recordVo);
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
        UserRecord userRecord = userRecordMapper.selectById(recordId);
        if(userRecord == null){
            throw new IOException("没有该记录");
        }
        Long meetingId = userRecord.getMeetingId();

        //Long meetingMinutesId = meetingService.getMeetingMinutesId(meetingId);
        Long meetingRecordId = meetingService.getMeetingRecordId(meetingId);
        Long meetingAudioId = meetingService.getMeetingAudioId(meetingId);

        //String minutes = meetingsMinutesService.getMinutesById(meetingMinutesId);
        String record = recordService.getRecordById(meetingRecordId);
        String audio = meetingAudioService.getAudioById(meetingAudioId);

        RecordDetailVo recordDetailVo = new RecordDetailVo();
        recordDetailVo.setMeetingRecord(record);
        recordDetailVo.setMeetingAudio(audio);
        //recordDetailVo.setMeetingMinutes(minutes);

        return Result.success(recordDetailVo);
    }

    @Override
    public void insertRecord(Integer userId, Long meetingId) {
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userId);
        userRecord.setMeetingId(meetingId);
        userRecordMapper.insert(userRecord);
    }
}




