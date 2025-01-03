package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.MeetingsMinutes;
import edu.hnu.conference_system.service.MeetingsMinutesService;
import edu.hnu.conference_system.mapper.MeetingsMinutesMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【meetings_minutes】的数据库操作Service实现
* @createDate 2024-11-11 19:00:59
*/
@Service
public class MeetingsMinutesServiceImpl extends ServiceImpl<MeetingsMinutesMapper, MeetingsMinutes>
    implements MeetingsMinutesService{

    @Resource
    private MeetingsMinutesMapper meetingsMinutesMapper;

    @Override
    public String getMinutesById(Long meetingMinutesId) {
        MeetingsMinutes m =  meetingsMinutesMapper.selectOne(
                new QueryWrapper<MeetingsMinutes>().eq("meetings_minutes_id",meetingMinutesId)
        );
        return null;
    }
}




