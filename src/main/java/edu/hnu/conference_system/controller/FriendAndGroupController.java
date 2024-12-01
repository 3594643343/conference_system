package edu.hnu.conference_system.controller;


import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.ChatGroupService;
import edu.hnu.conference_system.service.UserAndGroupService;
import edu.hnu.conference_system.service.UserContactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friend8group")
@Tag(name = "好友与群聊接口")
@Slf4j
public class FriendAndGroupController {


    @Resource
    ChatGroupService chatGroupService;

    @Resource
    UserContactService userContactService;

    @Resource
    UserAndGroupService userAndGroupService;

    public Result addFriend(@RequestParam("friendid") Long friendid) {
        return userContactService.addFriend(friendid);
    }

}
