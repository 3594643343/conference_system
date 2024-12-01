package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.UserContact;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.UserContactService;
import edu.hnu.conference_system.mapper.UserContactMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【user_contact】的数据库操作Service实现
* @createDate 2024-12-01 11:12:04
*/
@Service
public class UserContactServiceImpl extends ServiceImpl<UserContactMapper, UserContact>
    implements UserContactService{

    @Resource
    private UserContactMapper userContactMapper;

    @Override
    public Result addFriend(Long friendid) {
        return null;
    }
}




