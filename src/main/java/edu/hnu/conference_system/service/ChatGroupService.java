package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.ChatGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.dto.ChangeGroupDto;
import edu.hnu.conference_system.dto.CreateGroupDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.GroupInfoVo;

/**
* @author lenovo
* @description 针对表【chat_group】的数据库操作Service
* @createDate 2024-12-01 11:11:41
*/
public interface ChatGroupService extends IService<ChatGroup> {

    Result createGroup(CreateGroupDto groupDto);

    Result changeGroupInfo(ChangeGroupDto groupDto);

    Result getGroupInfo(Integer groupId);

    GroupInfoVo buildGroupInfo(Integer groupId);

    void deleteGroup(Integer groupId);

    Integer getGroupCreatorId(Integer groupId);
}
