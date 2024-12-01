package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.UserAndGroup;
import edu.hnu.conference_system.service.UserAndGroupService;
import edu.hnu.conference_system.mapper.UserAndGroupMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【user_and_group】的数据库操作Service实现
* @createDate 2024-12-01 11:11:55
*/
@Service
public class UserAndGroupServiceImpl extends ServiceImpl<UserAndGroupMapper, UserAndGroup>
    implements UserAndGroupService{

}




