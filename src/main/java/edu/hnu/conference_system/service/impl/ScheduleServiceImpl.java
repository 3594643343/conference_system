package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.Schedule;
import edu.hnu.conference_system.dto.JoinMeetingDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.mapper.ScheduleMapper;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.MeetingService;
import edu.hnu.conference_system.service.RoomService;
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
    @Resource
    private RoomService roomService;

    @Override
    public Result show(Integer userId) {
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
    public Result add(Integer userId, JoinMeetingDto joinMeetingDto) {
        Long meetingId = meetingService.validate(joinMeetingDto.getMeetingNumber(),joinMeetingDto.getMeetingPassword());

        Schedule s = scheduleMapper.selectOne(
                new QueryWrapper<Schedule>().eq("user_id",userId).eq("meeting_id",meetingId)
        );
        if(s!=null){
            return Result.error("已有该日程!");
        }
        Schedule schedule = new Schedule();
        schedule.setUserId(userId);
        schedule.setMeetingId(meetingId);
        scheduleMapper.insert(schedule);
        return Result.success("添加日程成功!");
    }

    @Override
    public Result join(String meetingnumber) {
        Result r = roomService.joinFromSchedule(meetingnumber);
        if( r.getCode() == 1){
            cancel(UserHolder.getUserId(),meetingnumber);
            return r;
        }
        else{
            return r;
        }
    }

    private Result cancel(Integer userId,Long meetingId){
        scheduleMapper.delete(
                new QueryWrapper<Schedule>().eq("user_id",userId).eq("meeting_id",meetingId)
                );
        return show(userId);
    }

    @Override
    public Result cancel(Integer userId, String meetingNumber) {
        Long meetingId = meetingService.getMeetingIdByNumber(meetingNumber);
        return cancel(userId,meetingId);
    }
}
