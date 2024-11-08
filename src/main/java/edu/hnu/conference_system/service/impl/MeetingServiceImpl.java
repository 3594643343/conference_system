package edu.hnu.conference_system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.mapper.MeetingMapper;
import edu.hnu.conference_system.service.MeetingService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【meeting】的数据库操作Service实现
* @createDate 2024-11-07 21:30:48
*/
@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting>
    implements MeetingService {

}




