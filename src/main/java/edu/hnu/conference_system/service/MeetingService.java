package edu.hnu.conference_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.dto.BookMeetingDto;
import edu.hnu.conference_system.result.Result;

import java.util.Map;

/**
* @author lenovo
* @description 针对表【meeting】的数据库操作Service
* @createDate 2024-11-07 21:30:48
*/
public interface MeetingService extends IService<Meeting> {

    Result bookMeeting(BookMeetingDto bookMeetingDto);

    Result quickMeeting();

    Result joinMeeting(Map<String,String> request);

    void startMeeting(Meeting meeting);

    void endMeeting(Meeting meet);

    void leaveMeeting();
}
