package edu.hnu.conference_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.dto.BookMeetingDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.MeetingInfoVo;
import edu.hnu.conference_system.vo.UserInfoVo;

import java.util.Map;

public interface ScheduleService extends IService<Meeting> {

    Result bookMeeting(BookMeetingDto bookMeetingDto);

    Result quickMeeting();

    Result joinMeeting(Map<String,String> request);

    void startMeeting(Meeting meeting);

    void endMeeting(Meeting meet);

    void leaveMeeting();
}