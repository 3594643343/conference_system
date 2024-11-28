package edu.hnu.conference_system.service;


import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.Room;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.dto.*;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.CreateMeetingVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface RoomService {

    //以下为主菜单接口

    CreateMeetingVo bookMeeting(BookMeetingDto bookMeetingDto);

    Result quickMeeting();

    Result joinMeeting(JoinMeetingDto joinMeetingDto);



    //以下为会议中接口

    Result leaveMeeting();

    Result getUserInfo(String meetingNumber);

    Result mute(MuteDto muteDto);

    Result disMute(MuteDto muteDto);

    Result permissionChange(PmChangeDto pmChangeDto);

    Result kickOneOut(KickDto kickDto);

    Result uploadFile(UploadFileDto uploadFileDto);

    void pushFileToAll(String UploadUserName,String fileName,String path) throws Exception;

    Result joinFromSchedule(String meetingNumber);

    List<Long> getOnMeetingUserId(Long meetingId);

    String getAudioPath();


    //Result getUserDetail(Map<String,String> request);
}
