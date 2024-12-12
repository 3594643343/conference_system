package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.UserAndGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.result.Result;

import java.util.List;

/**
* @author lenovo
* @description 针对表【user_and_group】的数据库操作Service
* @createDate 2024-12-01 11:11:55
*/
public interface UserAndGroupService extends IService<UserAndGroup> {

    Result searchGroup(Integer groupId);

    Result addGroup(Integer groupId,String checkWords);

    Result getMembers(Integer groupId);

    void deleteGroup(Integer groupId);

    Result leaveGroup(Integer userId, Integer groupId);

    List<Integer> getMembersId(Integer toWho);

    boolean beforeSendCheck(Integer userId, Integer groupId);

    Result getOnesAllGroup(Integer userId);

    Result dealCheck(Integer recordId,Integer userId, Integer groupId, Integer check);

    void directJoinGroup(Integer groupId, Integer userId);

    Boolean getIsIn(Integer userId, Integer groupId);

    Result kickOneOut(Integer groupId, Integer userId);
}
