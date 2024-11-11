package edu.hnu.conference_system.service.impl;

import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.Room;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.service.RoomService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static edu.hnu.conference_system.service.impl.UserInfoServiceImpl.userList;
import static java.time.LocalTime.ofSecondOfDay;

@Service
public class RoomServiceImpl implements RoomService {

    /**
     * 存放服务器上存在的(未开始与进行中)的会议房间
     */
    static List<Room> roomList = new ArrayList<>();


    @Override
    public void addRoom(Meeting meeting) {
        Room room = new Room(meeting.getMeetingName(), meeting.getMeetingNumber(), LocalDateTime.now(),
                meeting.getDefaultPermission(), meeting.getUserId(),new ArrayList<>());

        for(User user : userList) {
            if(user.getId().equals(UserHolder.getUserId())) {
                room.getMembers().add(user);

                //System.out.println(room.getMeetingNumber()+"中有"+room.getMembers().size());

                break;
            }
        }
        roomList.add(room);
    }

    @Override
    public void joinRoom(String meetingNumber,User user) {
        for (Room room : roomList) {
            if(room.getMeetingNumber().equals(meetingNumber)) {
                room.getMembers().add(user);
                break;
            }
        }

    }

    @Override
    public void leaveRoom(String meetingNumber) {
        for (Room room : roomList) {
            if(room.getMeetingNumber().equals(meetingNumber)) {
                for(User user : room.getMembers()) {
                    if(user.getId().equals(UserHolder.getUserId())) {
                        //将离开会议的user与会议相关的变量清空,再把它从这个房间里踢出
                        user.setMeetingNumber(null);
                        user.setMeetingId(null);
                        user.setMeetingPermission(-1);
                        room.getMembers().remove(user);
                    }
                    break;
                }

            }
            break;
        }
    }

    @Override
    public Meeting deleteRoom(String meetingNumber) {
        Meeting meeting = new Meeting();
        for(Room room : roomList) {
            if(room.getMeetingNumber().equals(meetingNumber)) {

                meeting.setMeetingNumber(meetingNumber);
                meeting.setMeetingEndTime(LocalDateTime.now());
                meeting.setMeetingTime(ofSecondOfDay(LocalDateTime.now().toLocalTime().toSecondOfDay()-
                        room.getStartTime().toLocalTime().toSecondOfDay())
                );
                meeting.setParticipantCount(room.getMembers().size());
                meeting.setMeetingState("end");
                /**
                 * 纪要id等
                 */


                //将要关闭的房间中的用户与会议相关的变量清空并全踢出房间

                //System.out.println(room.getMeetingNumber()+"中有"+room.getMembers().size());

                for(User user : room.getMembers()) {
                    user.setMeetingNumber(null);
                    user.setMeetingId(null);
                    user.setMeetingPermission(-1);
                    //room.getMembers().remove(user);
                }
                room.getMembers().clear();
                //将这个房间从房间列表移除
                roomList.remove(room);
                break;

            }
        }
        return meeting;
    }
}
