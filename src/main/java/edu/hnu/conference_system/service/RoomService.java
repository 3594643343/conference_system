package edu.hnu.conference_system.service;


import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.Room;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.dto.KickDto;
import edu.hnu.conference_system.dto.MuteDto;
import edu.hnu.conference_system.dto.PmChangeDto;
import edu.hnu.conference_system.dto.UploadFileDto;
import edu.hnu.conference_system.result.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface RoomService {

    void addRoom(Meeting meeting);

    void joinRoom(String meetingNumber, User user);

    void leaveRoom(String meetingNumber);

    Meeting deleteRoom(String meetingNumber);

    void saveAllUserInMeeting(Room room);

    Result getUserInfo(Map<String,String> request);

    Result mute(MuteDto muteDto);

    Result disMute(MuteDto muteDto);

    Result permissionChange(PmChangeDto pmChangeDto);

    Result kickOneOut(KickDto kickDto);

    Result uploadFile(UploadFileDto uploadFileDto);

    void pushFileToAll(String UploadUserName,String fileName,String path) throws Exception;

    //Result getUserDetail(Map<String,String> request);
}
