package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.UserContact;
import edu.hnu.conference_system.domain.UserInfo;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.mapper.UserInfoMapper;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.CheckMessageRecordService;
import edu.hnu.conference_system.service.UserContactService;
import edu.hnu.conference_system.mapper.UserContactMapper;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.socket.WebSocketChatServer;
import edu.hnu.conference_system.utils.Base64Utils;
import edu.hnu.conference_system.vo.UserFriendVo;
import edu.hnu.conference_system.vo.UserInfoVo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Resource
    private CheckMessageRecordService checkMessageRecordService;

    private WebSocketChatServer webSocketChatServer;

    @Autowired
    @Lazy
    public void setWebSocketChatServer(WebSocketChatServer webSocketChatServer) {
        this.webSocketChatServer = webSocketChatServer;
    }

    @Override
    public Result addFriend(Integer friendId,String checkWords) {
        if(userInfoService.isNeedCheck(friendId)){
            webSocketChatServer.sendAddFriendCheckMessage(UserHolder.getUserId(),friendId,checkWords);
            return Result.success("已发送好友验证");
        }
        else{
            webSocketChatServer.sendAddFriendMessage(UserHolder.getUserId(),friendId,checkWords);
            makeFriendContact(UserHolder.getUserId(),friendId);
            return Result.success("已添加 "+friendId);
        }

    }

    private void makeFriendContact(Integer userId, Integer friendId) {
        UserContact uc = userContactMapper.selectOne(new QueryWrapper<UserContact>()
                .eq("user_id", userId).eq("userfriend_id", friendId));
        if(uc == null){
            UserContact userContact = new UserContact();
            userContact.setUserId(userId);
            userContact.setUserfriendId(friendId);
            userContact.setIsFriend(1);
            userContactMapper.insert(userContact);
            userContact.setUserId(friendId);
            userContact.setUserfriendId(userId);
            userContact.setIsFriend(1);
            userContactMapper.insert(userContact);
        }
        else if(uc.getIsFriend() == 0){
            UserContact ucn = new UserContact();
            ucn.setIsFriend(1);
            userContactMapper.update(ucn, new UpdateWrapper<UserContact>()
                    .eq("user_id", userId).eq("userfriend_id", friendId)
            );
            userContactMapper.update(ucn, new UpdateWrapper<UserContact>()
                    .eq("user_id", friendId).eq("userfriend_id", userId)
            );
        }

    }

    @Override
    public Result searchById(Integer friendId) {
        UserInfo userInfo = userInfoService.getById(friendId);
        if (userInfo == null) {
            return Result.error("未找到用户!");
        }
        UserContact userContact = userContactMapper.selectOne(
                new QueryWrapper<UserContact>().eq("user_id", UserHolder.getUserId())
                        .eq("userfriend_id", friendId)
        );

        UserFriendVo userFriendVo = new UserFriendVo();

        userFriendVo.setFriendId(friendId);
        userFriendVo.setFriendName(userInfo.getUserName());
        userFriendVo.setFriendEmail(userInfo.getUserEmail());
        userFriendVo.setFriendSignature(userInfo.getUserSignature());
        userFriendVo.setFriendAvatar(Base64Utils.encode(userInfo.getAvatarPath()));

        userFriendVo.setIsFriend(userContact != null);

        return Result.success(userFriendVo);
    }

    @Override
    public Result deleteFriend(Integer friendId) {
        UserContact userContact = new UserContact();
        userContact.setIsFriend(0);
        userContactMapper.update(userContact,new QueryWrapper<UserContact>()
                .eq("user_id", UserHolder.getUserId()).eq("userfriend_id", friendId)
        );
        userContactMapper.update(userContact,new QueryWrapper<UserContact>()
                .eq("userfriend_id", UserHolder.getUserId()).eq("user_id", friendId)
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

    @Override
    public Result dealCheck(Integer recordId,Integer frienId, Integer check) {
        if(!isFriend(UserHolder.getUserId(),frienId)){
            //不是好友, 添加为好友, 修改验证消息记录
            if(check == 0){

                checkMessageRecordService.refuseFriendCheck(recordId);
                checkMessageRecordService.refuseFriendCheck(recordId+1);
                checkMessageRecordService.dealSameFriendRecord(recordId,check);
                return Result.success("已拒绝"+frienId+"添加好友");
            }
            else{
                checkMessageRecordService.passFriendCheck(recordId);
                checkMessageRecordService.passFriendCheck(recordId+1);
                webSocketChatServer.sendAddFriendMessage(UserHolder.getUserId(),frienId,"pass");
                makeFriendContact(frienId,UserHolder.getUserId());
                checkMessageRecordService.dealSameFriendRecord(recordId,check);
                return Result.success("已同意"+frienId+"添加好友");
            }
        }else{
            return Result.error("已是好友");
        }

    }

    private boolean isFriend(Integer userId, Integer frienId) {
        UserContact userContact = userContactMapper.selectOne(
                new QueryWrapper<UserContact>().eq("user_id", userId).eq("userfriend_id", frienId)
        );
        return userContact != null && userContact.getIsFriend() == 1;
    }

    @Override
    public Result getOnesAllFriend(Integer userId) {
        List<UserContact> contacts = userContactMapper.selectList(
                new QueryWrapper<UserContact>().eq("user_id", userId)
        );
        List<Integer> friendIds = new ArrayList<>();
        for(UserContact uc : contacts){
            if(uc.getIsFriend() == 1){
                friendIds.add(uc.getUserfriendId());
            }
        }
        return Result.success(friendIds);
    }

    @Override
    public Result getAllFriendInfo(Integer userId) {
        List<UserContact> contacts = userContactMapper.selectList(
                new QueryWrapper<UserContact>().eq("user_id", userId)
        );
        List<UserInfoVo> friendInfos = new ArrayList<>();
        for(UserContact uc : contacts){
            if(uc.getIsFriend() == 1){
                friendInfos.add(userInfoService.getUserInfo(uc.getUserfriendId()));
            }
        }
        return Result.success(friendInfos);
    }
}




