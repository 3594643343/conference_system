package edu.hnu.conference_system.service.impl;

import cn.hutool.json.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.ChatGroup;
import edu.hnu.conference_system.dto.ChangeGroupDto;
import edu.hnu.conference_system.dto.CreateGroupDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.ChatGroupService;
import edu.hnu.conference_system.mapper.ChatGroupMapper;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.utils.Base64Utils;
import edu.hnu.conference_system.vo.CreateGroupVo;
import edu.hnu.conference_system.vo.GroupInfoVo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author lenovo
* @description 针对表【chat_group】的数据库操作Service实现
* @createDate 2024-12-01 11:11:41
*/
@Service
public class ChatGroupServiceImpl extends ServiceImpl<ChatGroupMapper, ChatGroup>
    implements ChatGroupService{

    @Value("${files-upload-url.group-avatar}")
    private String avatarPath;

    @Resource
    private ChatGroupMapper chatGroupMapper;

    @Resource
    private UserInfoService userInfoService;

    @Override
    public Result createGroup(CreateGroupDto groupDto) {
        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setGroupCreatorId(UserHolder.getUserId());
        chatGroup.setGroupCreateTime(LocalDateTime.now());
        chatGroup.setGroupName(groupDto.getGroupName());
        String thisAvatarPath = avatarPath +"/"+ UserHolder.getUserId()+"_"+groupDto.getGroupName()+".jpg";
        File file = new File(thisAvatarPath);
        try{
            groupDto.getGroupAvatar().transferTo(file);
        } catch (IOException e) {
            return Result.error("创建群聊失败: 文件IO错误!  "+e.getMessage());
        }
        chatGroup.setGroupAvatarPath(thisAvatarPath);
        chatGroupMapper.insert(chatGroup);
        Integer id = chatGroup.getGroupId();
        return Result.success(new CreateGroupVo(id));
    }

    @Override
    public Result changeGroupInfo(ChangeGroupDto groupDto) {
        UpdateWrapper<ChatGroup> updateWrapper = new UpdateWrapper<ChatGroup>();
        updateWrapper.eq("group_id", groupDto.getGroupId());
        if(groupDto.getGroupName() != null) {
            updateWrapper.set("group_name", groupDto.getGroupName());
        }
        if(groupDto.getGroupAvatar() != null) {
            try {
                File oldAvatar = new File(findGroupAvatarPathById(groupDto.getGroupId()));
                groupDto.getGroupAvatar().transferTo(oldAvatar);
            }catch (IOException e) {
                return Result.error("更新群信息失败: "+e.getMessage());
            }
        }
        return Result.success("更新成功!");

    }

    @Override
    public Result getGroupInfo(Integer groupId) {
        ChatGroup chatGroup = chatGroupMapper.selectById(groupId);
        if(chatGroup == null) {
            return Result.error("未找到群聊!");
        }
        GroupInfoVo groupInfoVo = new GroupInfoVo();
        groupInfoVo.setGroupName(chatGroup.getGroupName());
        groupInfoVo.setCreatorName(userInfoService.getNameById(chatGroup.getGroupCreatorId()));
        groupInfoVo.setGroupAvatar(Base64Utils.encode(chatGroup.getGroupAvatarPath()));
        return Result.success(chatGroup.getGroupName());

    }

    @Override
    public GroupInfoVo buildGroupInfo(Integer groupId) {
        ChatGroup chatGroup = chatGroupMapper.selectById(groupId);
        GroupInfoVo groupInfoVo = new GroupInfoVo();
        groupInfoVo.setGroupName(chatGroup.getGroupName());
        groupInfoVo.setCreatorName(userInfoService.getNameById(chatGroup.getGroupCreatorId()));
        groupInfoVo.setGroupAvatar(Base64Utils.encode(chatGroup.getGroupAvatarPath()));
        return groupInfoVo;
    }

    @Override
    public void deleteGroup(Integer groupId) {
        chatGroupMapper.deleteById(groupId);
    }

    @Override
    public Integer getGroupCreatorId(Integer groupId) {
        ChatGroup chatGroup = chatGroupMapper.selectById(groupId);
        return chatGroup.getGroupCreatorId();
    }


    private String findGroupAvatarPathById(Integer groupId) {
        ChatGroup chatGroup = chatGroupMapper.selectById(groupId);
        return chatGroup.getGroupAvatarPath();
    }
}




