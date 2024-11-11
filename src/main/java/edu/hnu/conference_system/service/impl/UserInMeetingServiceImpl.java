package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.UserInMeeting;
import edu.hnu.conference_system.service.UserInMeetingService;
import edu.hnu.conference_system.mapper.UserInMeetingMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【user_in_meeting】的数据库操作Service实现
* @createDate 2024-11-11 19:10:55
*/
@Service
public class UserInMeetingServiceImpl extends ServiceImpl<UserInMeetingMapper, UserInMeeting>
    implements UserInMeetingService{

}




