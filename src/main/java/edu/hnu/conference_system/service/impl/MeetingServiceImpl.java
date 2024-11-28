package edu.hnu.conference_system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.mapper.MeetingMapper;
import edu.hnu.conference_system.service.*;
import edu.hnu.conference_system.vo.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;


/**
* @author lenovo
* @description 针对表【meeting】的数据库操作Service实现
* @createDate 2024-11-07 21:30:48
*/
@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting>
    implements MeetingService {

    @Resource
    MeetingMapper meetingMapper;
    @Resource
    UserInfoService userInfoService;



    @Override
    public void insertMeeting(Meeting meeting) {
        meetingMapper.insert(meeting);
    }

    @Override
    public void turnOnMeeting(Long meetingId) {
        meetingMapper.update(lambdaUpdate()
                .eq(Meeting::getMeetingId,meetingId)
                .set(Meeting::getMeetingState, "on")
                .getWrapper()
        );
    }

    @Override
    public void endMeeting(Meeting meet) {

        UpdateWrapper<Meeting> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("meeting_id", meet.getMeetingId());

        //更改数据库中会议状态
        meetingMapper.update(meet, updateWrapper);
    }

    @Override
    public Meeting selectByNumber(String meetingNumber) {
        return meetingMapper.selectOne(
                new QueryWrapper<Meeting>().eq("meeting_number",meetingNumber));
    }

    @Override
    public Meeting selectById(Long meetingId) {
        return meetingMapper.selectById(meetingId);
    }

    @Override
    public Long getMeetingIdByNumber(String meetingNumber) {
        Meeting meeting = meetingMapper.selectOne(
                new QueryWrapper<Meeting>().eq("meeting_number",meetingNumber)
        );
        if(meeting == null){
            return null;
        }
        return meeting.getMeetingId();
    }


    @Override
    public ScheduleShowVo buildSceduleShowVoById(Long meetingId) {
        Meeting meeting = meetingMapper.selectOne(
                new QueryWrapper<Meeting>().eq("meeting_id", meetingId)
        );
        ScheduleShowVo scheduleShowVo = new ScheduleShowVo();

        scheduleShowVo.setMeetingName(meeting.getMeetingName());
        scheduleShowVo.setMeetingHost(userInfoService.getNameById(meeting.getUserId()));
        scheduleShowVo.setMeetingStartTime(meeting.getMeetingStartTime());
        scheduleShowVo.setMeetingEndTime(meeting.getMeetingEndTime());
        scheduleShowVo.setMeetingState(meeting.getMeetingState());
        scheduleShowVo.setMeetingId(meetingId);
        scheduleShowVo.setMeetingNumber(meeting.getMeetingNumber());
        return scheduleShowVo;
    }

    @Override
    public Long validate(String meetingNumber, String meetingPassword) {
        Meeting meeting = meetingMapper.selectOne(
                new QueryWrapper<Meeting>().eq("meeting_number", meetingNumber)
        );
        if(meeting == null){
            throw new RuntimeException("不存在该会议!");
        }
        if(Objects.equals(meeting.getMeetingState(), "end")){
            throw new RuntimeException("会议已结束!");
        }
        else if(!meeting.getMeetingPassword().equals(meetingPassword)){
            throw new RuntimeException("会议密码错误!");
        }
        else return meeting.getMeetingId();
    }


    @Override
    public RecordVo buildRecordVoById(Long meetingId) {
        RecordVo recordVo = new RecordVo();
        recordVo.setParticipants(new ArrayList<>());
        Meeting meeting = meetingMapper.selectOne(
                new QueryWrapper<Meeting>().eq("meeting_id", meetingId)
        );
        recordVo.setMeetingName(meeting.getMeetingName());
        recordVo.setMeetingHost(userInfoService.getNameById(meeting.getUserId()));
        recordVo.setMeetingStartTime(meeting.getMeetingStartTime());
        recordVo.setMeetingEndTime(meeting.getMeetingEndTime());
        return recordVo;
    }

    @Override
    public Long getMeetingMinutesId(Long meetingId) {
        return meetingMapper.selectOne(
                new QueryWrapper<Meeting>().eq("meeting_id",meetingId)
        ).getMeetingsMinutesId();
    }

    @Override
    public Long getMeetingRecordId(Long meetingId) {
        return meetingMapper.selectOne(
                new QueryWrapper<Meeting>().eq("meeting_id",meetingId)
        ).getRecordId();
    }

    @Override
    public Long getMeetingAudioId(Long meetingId) {
        return meetingMapper.selectOne(
                new QueryWrapper<Meeting>().eq("meeting_id",meetingId)
        ).getMeetingAudioId();
    }

}




