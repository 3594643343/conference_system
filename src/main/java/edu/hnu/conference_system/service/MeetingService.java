package edu.hnu.conference_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.dto.BookMeetingDto;
import edu.hnu.conference_system.dto.JoinMeetingDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.CreateMeetingVo;
import edu.hnu.conference_system.vo.MeetingInfoVo;
import edu.hnu.conference_system.vo.RecordVo;
import edu.hnu.conference_system.vo.ScheduleShowVo;

import java.util.List;
import java.util.Map;

/**
* @author lenovo
* @description 针对表【meeting】的数据库操作Service
* @createDate 2024-11-07 21:30:48
*/
public interface MeetingService extends IService<Meeting> {

    void endMeeting(Meeting meet);

    ScheduleShowVo buildSceduleShowVoById(Long meetingId);

    Long validate(String meetingNumber, String meetingPassword);

    RecordVo buildRecordVoById(Long meetingId);

    Long getMeetingMinutesId(Long meetingId);

    Long getMeetingRecordId(Long meetingId);

    Long getMeetingAudioId(Long meetingId);

    void insertMeeting(Meeting meeting);

    Meeting selectByNumber(String meetingNumber);

    void turnOnMeeting(Long meetingId);

    Meeting selectById(Long meetingId);

    Long getMeetingIdByNumber(String meetingNumber);
}
