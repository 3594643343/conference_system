package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.ChatGroup;
import edu.hnu.conference_system.domain.UserAndGroup;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.ChatGroupService;
import edu.hnu.conference_system.service.UserAndGroupService;
import edu.hnu.conference_system.mapper.UserAndGroupMapper;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.utils.Base64Utils;
import edu.hnu.conference_system.vo.GroupInfoVo;
import edu.hnu.conference_system.vo.GroupMemberVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public Result addGroup(Integer groupId) {
        return null;
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
}




