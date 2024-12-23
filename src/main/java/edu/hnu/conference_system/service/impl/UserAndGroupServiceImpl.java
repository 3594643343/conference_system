package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.ChatGroup;
import edu.hnu.conference_system.domain.UserAndGroup;
import edu.hnu.conference_system.domain.UserContact;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.ChatGroupService;
import edu.hnu.conference_system.service.CheckMessageRecordService;
import edu.hnu.conference_system.service.UserAndGroupService;
import edu.hnu.conference_system.mapper.UserAndGroupMapper;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.socket.WebSocketChatServer;
import edu.hnu.conference_system.utils.Base64Utils;
import edu.hnu.conference_system.vo.GroupInfoVo;
import edu.hnu.conference_system.vo.GroupMemberVo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* @author lenovo
* @description 针对表【user_and_group】的数据库操作Service实现
* @createDate 2024-12-01 11:11:55
*/
@Service
public class UserAndGroupServiceImpl extends ServiceImpl<UserAndGroupMapper, UserAndGroup>
    implements UserAndGroupService{

    @Resource
    private UserAndGroupMapper userAndGroupMapper;

    @Resource
    private ChatGroupService chatGroupService;

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
    public Result searchGroup(Integer groupId) {
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        if(chatGroup == null){
            return Result.error("未找到群聊");
        }

        UserAndGroup userAndGroup = userAndGroupMapper.selectOne(
                new QueryWrapper<UserAndGroup>().eq("group_id", groupId)
                        .eq("user_id", UserHolder.getUserId())
                        .eq("is_in",1)
        );

        GroupInfoVo groupInfoVo = chatGroupService.buildGroupInfo(groupId);

        groupInfoVo.setIsIn(userAndGroup != null);

        return Result.success(groupInfoVo);
    }

    @Override
    public Result addGroup(Integer groupId,String checkWords) {
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        if(chatGroup.getNeedCheck() == 0){
            makeGroupContact(groupId,UserHolder.getUserId());
            return Result.success("已加入群聊: "+groupId);
        }
        else{
            webSocketChatServer.sendAddGroupCheckMessage(UserHolder.getUserId(),groupId,checkWords);
            return Result.success("已发送群聊验证");
        }
    }

    private void makeGroupContact(Integer groupId, Integer userId) {
        UserAndGroup ug = userAndGroupMapper.selectOne(new QueryWrapper<UserAndGroup>()
                .eq("user_id", userId).eq("group_id", groupId));
        if(ug == null){
            UserAndGroup userAndGroup = new UserAndGroup();
            userAndGroup.setUserId(userId);
            userAndGroup.setGroupId(groupId);
            userAndGroup.setIsIn(1);
            userAndGroupMapper.insert(userAndGroup);
        }
        else if(ug.getIsIn() == 0){
            UserAndGroup ugn = new UserAndGroup();
            ugn.setIsIn(1);
            userAndGroupMapper.update(ugn, new UpdateWrapper<UserAndGroup>()
                    .eq("user_id", userId).eq("group_id", groupId)
            );
        }
    }

    @Override
    public Result getMembers(Integer groupId) {
        List<GroupMemberVo> groupMemberVoList = new ArrayList<>();
        List<UserAndGroup> contacts = userAndGroupMapper.selectList(
                new QueryWrapper<UserAndGroup>().eq("group_id",groupId)
        );
        for(UserAndGroup userAndGroup : contacts){
            GroupMemberVo groupMemberVo = new GroupMemberVo();
            groupMemberVo.setUserId(userAndGroup.getUserId());
            groupMemberVo.setUserName(userInfoService.getNameById(userAndGroup.getUserId()));
            groupMemberVo.setUserAvatar(userInfoService.getUserAvatar(userAndGroup.getUserId()));
            groupMemberVoList.add(groupMemberVo);
        }
        return Result.success(groupMemberVoList);
    }

    private void oneLeaveGroup(Integer groupId, Integer userId) {
        UserAndGroup userAndGroup = new UserAndGroup();
        userAndGroup.setIsIn(0);
        userAndGroupMapper.update(userAndGroup, new UpdateWrapper<UserAndGroup>()
                .eq("group_id", groupId).eq("user_id", userId));
    }

    @Override
    public void deleteGroup(Integer groupId) {
        List<UserAndGroup> contacts = userAndGroupMapper.selectList(
                new QueryWrapper<UserAndGroup>().eq("group_id", groupId)
        );
        for(UserAndGroup userAndGroup : contacts){
            oneLeaveGroup(groupId, userAndGroup.getUserId());
        }
    }

    @Override
    public Result leaveGroup(Integer userId, Integer groupId) {
        oneLeaveGroup(groupId, userId);
        return Result.success("已退出群聊: "+groupId);
    }

    @Override
    public List<Integer> getMembersId(Integer toWho) {
        List<UserAndGroup> contacts = userAndGroupMapper.selectList(
                new QueryWrapper<UserAndGroup>().eq("group_id", toWho)
        );
        List<Integer> userIds = new ArrayList<>();
        for(UserAndGroup userAndGroup : contacts){
            userIds.add(userAndGroup.getUserId());
        }
        return userIds;
    }

    @Override
    public boolean beforeSendCheck(Integer userId, Integer groupId) {
        UserAndGroup userAndGroup = userAndGroupMapper.selectOne(
                new QueryWrapper<UserAndGroup>().eq("group_id", groupId).eq("user_id", userId)
        );
        return userAndGroup != null && userAndGroup.getIsIn() != 0;
    }

    @Override
    public Result getOnesAllGroup(Integer userId) {
        List<UserAndGroup> groups = userAndGroupMapper.selectList(
                new QueryWrapper<UserAndGroup>().eq("user_id", userId)
        );
        List<Integer> ids = new ArrayList<>();
        for (UserAndGroup userAndGroup : groups) {
            if(userAndGroup.getIsIn() == 1){
                ids.add(userAndGroup.getGroupId());
            }
        }
        return Result.success(ids);
    }

    @Override
    public Result dealCheck(Integer recordId,Integer userId, Integer groupId, Integer check) {
        if(!userInGroup(userId,groupId)){
            if(check == 0){

                checkMessageRecordService.refuseGroupCheck(recordId);
                checkMessageRecordService.refuseGroupCheck(recordId+1);
                checkMessageRecordService.dealSameGroupRecord(recordId,check);
                return Result.success("已拒绝申请");
            }
            else{
                checkMessageRecordService.passGroupCheck(recordId);
                checkMessageRecordService.passGroupCheck(recordId+1);
                webSocketChatServer.sendAddGroupMessage(UserHolder.getUserId(),userId);
                makeGroupContact(groupId,userId);
                checkMessageRecordService.dealSameGroupRecord(recordId,check);
                return Result.success("已同意申请");
            }

        }else{
            return  Result.error("用户已在群聊");
        }

    }

    private boolean userInGroup(Integer userId, Integer groupId) {
        UserAndGroup userAndGroup = userAndGroupMapper.selectOne(
                new QueryWrapper<UserAndGroup>().eq("user_id", userId).eq("group_id", groupId)
        );
        return userAndGroup != null && userAndGroup.getIsIn() == 1;
    }

    @Override
    public void directJoinGroup(Integer groupId, Integer userId) {
        UserAndGroup userAndGroup =new UserAndGroup();
        userAndGroup.setGroupId(groupId);
        userAndGroup.setUserId(userId);
        userAndGroup.setIsIn(1);
        userAndGroupMapper.insert(userAndGroup);
    }

    @Override
    public Boolean getIsIn(Integer userId, Integer groupId) {
        UserAndGroup userAndGroup = userAndGroupMapper.selectOne(
                new QueryWrapper<UserAndGroup>().eq("user_id",userId).eq("group_id",groupId)
        );
        return userAndGroup.getIsIn() == 1;
    }

    @Override
    public Result kickOneOut(Integer groupId, Integer userId) {
        if (!Objects.equals(chatGroupService.getGroupCreatorId(groupId), UserHolder.getUserId())){
            return Result.error("权限不足!");
        }
        if (Objects.equals(chatGroupService.getGroupCreatorId(groupId), userId)){
            return Result.error("无法踢出自己!");
        }

        UserAndGroup userAndGroup = userAndGroupMapper.selectOne(
                new QueryWrapper<UserAndGroup>().eq("group_id", groupId).eq("user_id", userId)
        );
        if (userAndGroup == null){
            return Result.error("用户不在群中!");
        }

        userAndGroup.setIsIn(0);
        userAndGroupMapper.updateById(userAndGroup);
        return Result.success("已踢出: "+userId);
    }

}




