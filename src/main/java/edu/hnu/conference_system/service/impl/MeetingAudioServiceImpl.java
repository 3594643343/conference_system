package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.MeetingAudio;
import edu.hnu.conference_system.service.MeetingAudioService;
import edu.hnu.conference_system.mapper.MeetingAudioMapper;
import edu.hnu.conference_system.utils.Base64Utils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author lenovo
* @description 针对表【meeting_audio】的数据库操作Service实现
* @createDate 2024-11-11 19:00:37
*/
@Service
public class MeetingAudioServiceImpl extends ServiceImpl<MeetingAudioMapper, MeetingAudio>
    implements MeetingAudioService{

    @Resource
    private MeetingAudioMapper meetingAudioMapper;

    @Override
    public String getAudioById(Long meetingAudioId) {
        MeetingAudio meetingAudio = meetingAudioMapper.selectById(meetingAudioId);
        String audioPath = meetingAudio.getAudioPath();
        return Base64Utils.encode(audioPath);
    }

    @Override
    public Long recordAudio(Long meetingId, String thisAudioPath) {
        MeetingAudio meetingAudio = new MeetingAudio();
        meetingAudio.setAudioPath(thisAudioPath);
        meetingAudio.setMeetingId(meetingId);
        //meetingAudio.setAudioTime();
        meetingAudioMapper.insert(meetingAudio);

        return meetingAudio.getMeetingAudioId();


    }

    @Override
    public Long getAudioIdByMeetingId(Long meetingId) {
        MeetingAudio meetingAudio = meetingAudioMapper.selectOne(
                new QueryWrapper<MeetingAudio>().eq("meeting_id", meetingId)
        );
        if(meetingAudio == null){
            throw new RuntimeException("查询数据库失败!");
        }
        System.out.println(meetingAudio.getMeetingAudioId());
        return meetingAudio.getMeetingAudioId();
    }
}




