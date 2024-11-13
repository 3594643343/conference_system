package edu.hnu.conference_system.service;


import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.Room;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.result.Result;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface RoomService {

    void addRoom(Meeting meeting);

    void joinRoom(String meetingNumber, User user);

    void leaveRoom(String meetingNumber);

    Meeting deleteRoom(String meetingNumber);

    void saveAllUserInMeeting(Room room);

    Result getUserInfo(Map<String,String> request);

    //Result getUserDetail(Map<String,String> request);
}
