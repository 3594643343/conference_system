package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.UserContact;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.result.Result;

/**
* @author lenovo
* @description 针对表【user_contact】的数据库操作Service
* @createDate 2024-12-01 11:12:04
*/
public interface UserContactService extends IService<UserContact> {

    Result addFriend(Integer friendid);

    Result searchById(Integer friendId);

    Result deleteFriend(Integer friendId);

    Boolean beforeSendCheck(Integer userId, Integer toWho);
}
