package edu.hnu.conference_system.service.impl;

import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.Room;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.RoomService;
import edu.hnu.conference_system.service.UserInMeetingService;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.vo.UserBriefVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.hnu.conference_system.service.impl.UserInfoServiceImpl.userList;
import static java.time.LocalTime.ofSecondOfDay;

@Service
public class RoomServiceImpl implements RoomService {

    /**
     * 存放服务器上存在的(未开始与进行中)的会议房间
     */
    static List<Room> roomList = new ArrayList<>();


    @Resource
    UserInMeetingService userInMeetingService;

    @Resource
    UserInfoService userInfoService;

    /**
     * 实例化一个Room,将room加入到roomlist中
     * @param meeting
     */
    @Override
    public void addRoom(Meeting meeting) {
        Room room = new Room(meeting.getMeetingName(),meeting.getMeetingId(), meeting.getMeetingNumber(), LocalDateTime.now(),
                meeting.getDefaultPermission(), meeting.getUserId(),new ArrayList<>(),new ArrayList<>());

        for(User user : userList) {
            if(user.getId().equals(UserHolder.getUserId())) {
                room.getMembersOn().add(user);
                room.getMembersAll().add(user.getId());

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
                room.getMembersOn().add(user);
                //所有用户列表中没有该用户则加入该用户
                if(!room.getMembersAll().contains(user)) {
                    room.getMembersAll().add(user.getId());
                }
                break;
            }
        }

    }

    @Override
    public void leaveRoom(String meetingNumber) {
        for (Room room : roomList) {
            if(room.getMeetingNumber().equals(meetingNumber)) {
                for(User user : room.getMembersOn()) {
                    if(user.getId().equals(UserHolder.getUserId())) {
                        //将离开会议的user与会议相关的变量清空,再把它从这个房间里踢出
                        user.setMeetingNumber(null);
                        user.setMeetingId(null);
                        user.setMeetingPermission(-1);
                        room.getMembersOn().remove(user);
                    }
                    break;
                }
            }
            break;
        }
    }

    /**
     * 关闭一个房间并返回一个Meeting对象,记录这个Meeting的相关信息,用于更新数据库中Meeting
     * 方法中处理参会人员\记录\纪要\音频这些要持久化的对象
     * @param meetingNumber
     * @return
     */
    @Override
    public Meeting deleteRoom(String meetingNumber) {
        Meeting meeting = new Meeting();
        for(Room room : roomList) {
            //找到要关闭的房间
            if(room.getMeetingNumber().equals(meetingNumber)) {

                //记录参会者
                saveAllUserInMeeting(room);
                /**
                 * 记录纪要等,还没写
                 */

                //为meeting赋值
                meeting.setMeetingNumber(meetingNumber);
                meeting.setMeetingEndTime(LocalDateTime.now());
                meeting.setMeetingTime(ofSecondOfDay(LocalDateTime.now().toLocalTime().toSecondOfDay()-
                        room.getStartTime().toLocalTime().toSecondOfDay())
                );
                meeting.setParticipantCount(room.getMembersOn().size());
                meeting.setMeetingState("end");
                /**
                 * 纪要id等,还没写
                 */


                //将要关闭的房间中的用户与会议相关的变量清空并全踢出房间
                for(User user : room.getMembersOn()) {
                    user.setMeetingNumber(null);
                    user.setMeetingId(null);
                    user.setMeetingPermission(-1);
                    //room.getMembers().remove(user);
                }
                room.getMembersOn().clear();
                //将这个房间从房间列表移除
                roomList.remove(room);
                break;

            }
        }
        return meeting;
    }


    /**
     * 将进入过房间的人保存到数据库中
     * 会议结束时调用
     */
    @Override
    public void saveAllUserInMeeting(Room room) {
        List<Long> userIds = new ArrayList<>(room.getMembersAll());
        userInMeetingService.saveAllUserInMeeting(room.getMeetingId(), userIds);
    }


    /**
     * 返回在会者简要信息(头像与昵称)用于侧边显示
     * @return
     */
    @Override
    public Result getUserBrief(Map<String,String> request) {
        List<UserBriefVo> userBriefVos = new ArrayList<>();
        for (Room room : roomList) {
            if(room.getMeetingNumber().equals(request.get("meetingNumber"))) {
                for(User user : room.getMembersOn()) {
                    userBriefVos.add(userInfoService.buildUserBriefVo(user.getId()));
                }
                break;
            }
        }
        return Result.success(userBriefVos);
    }
}
