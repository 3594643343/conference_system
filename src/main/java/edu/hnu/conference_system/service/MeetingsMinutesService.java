package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.MeetingsMinutes;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lenovo
* @description 针对表【meetings_minutes】的数据库操作Service
* @createDate 2024-11-11 19:00:59
 *
 * 纪要
*/
public interface MeetingsMinutesService extends IService<MeetingsMinutes> {

    String getMinutesById(Long meetingMinutesId);
}
