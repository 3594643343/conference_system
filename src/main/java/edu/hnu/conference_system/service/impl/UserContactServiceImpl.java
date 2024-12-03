package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.UserContact;
import edu.hnu.conference_system.domain.UserInfo;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.UserContactService;
import edu.hnu.conference_system.mapper.UserContactMapper;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.socket.WebSocketChatServer;
import edu.hnu.conference_system.utils.Base64Utils;
import edu.hnu.conference_system.vo.UserFriendVo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Resource
    private UserInfoService userInfoService;

    private WebSocketChatServer webSocketChatServer;

    @Autowired
    public void setWebSocketChatServer(WebSocketChatServer webSocketChatServer) {
        this.webSocketChatServer = webSocketChatServer;
    }

    @Override
    public Result addFriend(Integer friendid) {
        return null;
    }

    @Override
    public Result searchById(Integer friendId) {
        UserInfo userInfo = userInfoService.getById(friendId);
        if (userInfo == null) {
            return Result.error("未找到用户!");
        }
        UserContact userContact = userContactMapper.selectOne(
                new QueryWrapper<UserContact>().eq("user_id", UserHolder.getUserId())
                        .eq("friend_id", friendId)
        );

        UserFriendVo userFriendVo = new UserFriendVo();

        userFriendVo.setFriendId(friendId);
        userFriendVo.setFriendName(userInfo.getUserName());
        userFriendVo.setFriendEmail(userInfo.getUserEmail());
        userFriendVo.setFriendSignature(userInfo.getUserSignature());
        userFriendVo.setFriendAvatar(Base64Utils.encode(userInfo.getAvatarPath()));

        if (userContact != null) {
            userFriendVo.setIsFriend(true);
        }
        else userFriendVo.setIsFriend(false);

        return Result.success(userFriendVo);
    }

    @Override
    public Result deleteFriend(Integer friendId) {
        UserContact userContact = new UserContact();
        userContact.setIsFriend(0);
        userContactMapper.update(userContact,new QueryWrapper<UserContact>()
                .eq("user_id", UserHolder.getUserId()).eq("friend_id", friendId)
        );
        userContactMapper.update(userContact,new QueryWrapper<UserContact>()
                .eq("friend_id", UserHolder.getUserId()).eq("user_id", friendId)
        );
        return Result.success("删除好友成功!");
    }

    @Override
    public Boolean beforeSendCheck(Integer userId, Integer toWho) {

        UserContact userContact = userContactMapper.selectOne(
                new QueryWrapper<UserContact>().eq("user_id", userId).eq("userfriend_id", toWho)
        );
        return userContact != null && userContact.getIsFriend() != 0;
    }
}




