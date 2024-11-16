package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.UserInMeeting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lenovo
* @description 针对表【user_in_meeting】的数据库操作Service
* @createDate 2024-11-11 19:10:55
*/
public interface UserInMeetingService extends IService<UserInMeeting> {

    void saveAllUserInMeeting(Long meetingId, List<Long> userIds);

    List<Long> getUsersIdsFromMeetingId(Long meetingId);
}
