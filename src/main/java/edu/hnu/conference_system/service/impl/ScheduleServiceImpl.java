package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.Schedule;
import edu.hnu.conference_system.dto.JoinMeetingDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.mapper.ScheduleMapper;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.MeetingService;
import edu.hnu.conference_system.service.ScheduleService;
import edu.hnu.conference_system.vo.ScheduleShowVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule>
    implements ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;
    @Resource
    private MeetingService meetingService;

    @Override
    public Result show(Long userId) {
        List<ScheduleShowVo> scheduleShowVos =new ArrayList<>();
        List<Schedule> schedules = scheduleMapper.selectList(
                new QueryWrapper<Schedule>().eq("user_id",userId)
        );
        for (Schedule schedule : schedules) {
            scheduleShowVos.add(meetingService.buildSceduleShowVoById(schedule.getMeetingId()));
        }
        return Result.success(scheduleShowVos);
    }

    @Override
    public Result add(Long userId, JoinMeetingDto joinMeetingDto) {
        Long meetingId = meetingService.validate(joinMeetingDto.getMeetingNumber(),joinMeetingDto.getMeetingPassword());
        if(meetingId==-1){
            return Result.error("会议号或密码错误!");
        }
        Schedule schedule = new Schedule();
        schedule.setUserId(userId);
        schedule.setMeetingId(meetingId);
        scheduleMapper.insert(schedule);
        return Result.success("添加日程成功!");
    }

    @Override
    public Result join(Long meetingId,String meetingnumber) {
        Result r = meetingService.joinFromSchedule(meetingnumber);
        if( r == Result.success("加入会议成功!")){
            cancel(UserHolder.getUserId(),meetingId);
            return r;
        }
        else{
            return r;
        }
    }

    @Override
    public Result cancel(Long userId, Long meetingId) {
        scheduleMapper.delete(
                new QueryWrapper<Schedule>().eq("user_id",userId).eq("meeting_id",meetingId)
        );
        return show(userId);
    }
}
