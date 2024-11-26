package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.MeetingAudio;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lenovo
* @description 针对表【meeting_audio】的数据库操作Service
* @createDate 2024-11-11 19:00:37
*/
public interface MeetingAudioService extends IService<MeetingAudio> {

    String getAudioById(Long meetingAudioId);

    Long recordAudio(Long meetingId, String thisAudioPath);

    Long getAudioIdByMeetingId(Long meetingId);
}
