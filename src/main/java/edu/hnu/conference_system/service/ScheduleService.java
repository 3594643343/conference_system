package edu.hnu.conference_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.Schedule;
import edu.hnu.conference_system.dto.BookMeetingDto;
import edu.hnu.conference_system.dto.JoinMeetingDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.MeetingInfoVo;
import edu.hnu.conference_system.vo.UserInfoVo;

import java.util.Map;

public interface ScheduleService extends IService<Schedule> {


    Result show(Long userId);

    Result add(Long userId, JoinMeetingDto joinMeetingDto);

    Result join(Long meetingId,String meetingnumber);

    Result cancel(Long userId, Long meetingId);
}