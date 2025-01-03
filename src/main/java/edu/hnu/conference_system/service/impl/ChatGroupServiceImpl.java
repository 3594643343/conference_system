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
import edu.hnu.conference_system.service.UserAndGroupService;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.utils.Base64Utils;
import edu.hnu.conference_system.vo.CreateGroupVo;
import edu.hnu.conference_system.vo.GroupInfoVo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private UserAndGroupService userAndGroupService;

    @Autowired
    @Lazy
    private void setUserAndGroupService(UserAndGroupService userAndGroupService) {
        this.userAndGroupService = userAndGroupService;
    }

    @Override
    public Result createGroup(CreateGroupDto groupDto) {
        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setGroupCreatorId(UserHolder.getUserId());
        chatGroup.setGroupCreateTime(LocalDateTime.now());
        chatGroup.setGroupName(groupDto.getGroupName());
        chatGroup.setNeedCheck(groupDto.getNeedCheck());
        String thisAvatarPath = avatarPath +"/"+ UserHolder.getUserId()+"_"+groupDto.getGroupName()+ LocalTime.now()+".jpg";
        File file = new File(thisAvatarPath);
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                System.out.println("创建群头像文件失败!"+e.getMessage());
            }

        }
        try{
            groupDto.getGroupAvatar().transferTo(file);
        } catch (IOException e) {
            return Result.error("创建群聊失败: 文件IO错误!  "+e.getMessage());
        }
        chatGroup.setGroupAvatarPath(thisAvatarPath);
        chatGroupMapper.insert(chatGroup);
        Integer id = chatGroup.getGroupId();
        userAndGroupService.directJoinGroup(id,UserHolder.getUserId());
        return Result.success(new CreateGroupVo(id));
    }

    @Override
    public Result changeGroupInfo(ChangeGroupDto groupDto) {
        UpdateWrapper<ChatGroup> updateWrapper = new UpdateWrapper<ChatGroup>();
        updateWrapper.eq("group_id", groupDto.getGroupId());
        if(groupDto.getGroupName() != null && !groupDto.getGroupName().equals("")) {
            updateWrapper.set("group_name", groupDto.getGroupName());
        }
        if(groupDto.getAvatar() != null&& !groupDto.getAvatar().equals("")) {
            try {
                File oldAvatar = new File(findGroupAvatarPathById(groupDto.getGroupId()));
                groupDto.getAvatar().transferTo(oldAvatar);
            }catch (IOException e) {
                return Result.error("更新群信息失败: "+e.getMessage());
            }
        }
        if(groupDto.getNeedCheck() != null&& !groupDto.getNeedCheck().equals("")) {
            updateWrapper.set("need_check", groupDto.getNeedCheck());
        }
        chatGroupMapper.update(updateWrapper);
        return Result.success("更新成功!");

    }


    @Override
    public Result getGroupInfo(Integer groupId) {
        ChatGroup chatGroup = chatGroupMapper.selectById(groupId);
        if(chatGroup == null) {
            return Result.error("未找到群聊!");
        }
        GroupInfoVo groupInfoVo = new GroupInfoVo();
        groupInfoVo.setGroupId(chatGroup.getGroupId());
        groupInfoVo.setCreatorId(chatGroup.getGroupCreatorId());
        groupInfoVo.setGroupName(chatGroup.getGroupName());
        groupInfoVo.setCreatorName(userInfoService.getNameById(chatGroup.getGroupCreatorId()));
        groupInfoVo.setGroupAvatar(Base64Utils.encode(chatGroup.getGroupAvatarPath()));
        groupInfoVo.setIsIn(userAndGroupService.getIsIn(UserHolder.getUserId(),groupId));
        return Result.success(groupInfoVo);

    }

    @Override
    public GroupInfoVo buildGroupInfo(Integer groupId) {
        ChatGroup chatGroup = chatGroupMapper.selectById(groupId);
        GroupInfoVo groupInfoVo = new GroupInfoVo();
        groupInfoVo.setGroupId(chatGroup.getGroupId());
        groupInfoVo.setCreatorId(chatGroup.getGroupCreatorId());
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




