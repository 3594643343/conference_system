package edu.hnu.conference_system.service;


import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.Room;
import edu.hnu.conference_system.domain.User;

public interface RoomService {

    void addRoom(Meeting meeting);

    void joinRoom(String meetingNumber, User user);

    void leaveRoom(String meetingNumber);

    Meeting deleteRoom(String meetingNumber);
}
