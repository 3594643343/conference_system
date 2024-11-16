package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.UserInMeeting;
import edu.hnu.conference_system.service.UserInMeetingService;
import edu.hnu.conference_system.mapper.UserInMeetingMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author lenovo
* @description 针对表【user_in_meeting】的数据库操作Service实现
* @createDate 2024-11-11 19:10:55
*/
@Service
public class UserInMeetingServiceImpl extends ServiceImpl<UserInMeetingMapper, UserInMeeting>
    implements UserInMeetingService{


    @Resource
    private UserInMeetingMapper userInMeetingMapper;

    @Override
    public void saveAllUserInMeeting(Long meetingId, List<Long> userIds) {
        for (Long userId : userIds) {
            UserInMeeting uim = new UserInMeeting();
            uim.setMeetingId(meetingId);
            uim.setUserId(userId);
            userInMeetingMapper.insert(uim);
        }
    }

    @Override
    public List<Long> getUsersIdsFromMeetingId(Long meetingId) {
        List<UserInMeeting> list = userInMeetingMapper.selectList(
                new QueryWrapper<UserInMeeting>().eq("meeting_id", meetingId)
        );
        List<Long> userIds = new ArrayList<>();
        for(UserInMeeting uim : list){
            userIds.add(uim.getUserId());
        }
        return userIds;
    }
}




