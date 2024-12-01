package edu.hnu.conference_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.domain.Schedule;
import edu.hnu.conference_system.dto.JoinMeetingDto;
import edu.hnu.conference_system.result.Result;


public interface ScheduleService extends IService<Schedule> {


    Result show(Integer userId);

    Result add(Integer userId, JoinMeetingDto joinMeetingDto);

    Result join(String meetingnumber);

    Result cancel(Integer userId, String meetingNumber);
}