package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.MeetingAudio;
import edu.hnu.conference_system.service.MeetingAudioService;
import edu.hnu.conference_system.mapper.MeetingAudioMapper;
import edu.hnu.conference_system.utils.Base64Utils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
}




