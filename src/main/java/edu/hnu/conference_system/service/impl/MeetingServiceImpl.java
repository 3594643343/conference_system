package edu.hnu.conference_system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.dto.BookMeetingDto;
import edu.hnu.conference_system.dto.JoinMeetingDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.mapper.MeetingMapper;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.MeetingService;
import edu.hnu.conference_system.service.RoomService;
import edu.hnu.conference_system.vo.CreateMeetingVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static edu.hnu.conference_system.service.impl.UserInfoServiceImpl.userList;


/**
* @author lenovo
* @description 针对表【meeting】的数据库操作Service实现
* @createDate 2024-11-07 21:30:48
*/
@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting>
    implements MeetingService {

    @Resource
    MeetingMapper meetingMapper;
    @Resource
    RoomService roomService;

    @Override
    public Result bookMeeting(BookMeetingDto bookMeetingDto) {
        String meetingNumber = RandomStringUtils.randomAlphanumeric(9);
        //System.out.println(meetingNumber);
        Meeting meeting = new Meeting(bookMeetingDto.getMeetingName(), meetingNumber, bookMeetingDto.getMeetingPassword(),
                UserHolder.getUserId(),bookMeetingDto.getMeetingStartTime(),bookMeetingDto.getMeetingEndTime(),
                bookMeetingDto.getDefaultPermission());

        meetingMapper.insert(meeting);
        return Result.success(new CreateMeetingVo(meetingNumber));
    }

    @Override
    public Result quickMeeting() {
        String meetingNumber = RandomStringUtils.randomAlphanumeric(9);
        Meeting meeting = new Meeting(UserHolder.getUserInfo().getUserName(), meetingNumber,
                UserHolder.getUserId(), LocalDateTime.now() );
        meetingMapper.insert(meeting);
        //加入会议

        JoinMeetingDto joinMeetingDto = new JoinMeetingDto(meetingNumber,null);
        joinMeeting(joinMeetingDto);
        return Result.success(new CreateMeetingVo(meetingNumber));

    }

    @Override
    public Result joinMeeting(JoinMeetingDto joinMeetingDto) {
        String meetingNumber = joinMeetingDto.getMeetingNumber();
        String meetingPassword = joinMeetingDto.getMeetingPassword();

        Meeting meeting = meetingMapper.selectOne(
                new QueryWrapper<Meeting>().eq("meeting_number",meetingNumber));
        if(meeting == null){
            return Result.error("不存在该会议!");
        }
        else if(Objects.equals(meeting.getMeetingState(), "off")){
            if(UserHolder.getUserId().equals(meeting.getUserId())){
                //用户为会议创建者, 在用户list中修改相关信息
                for(User user:userList){
                    if(Objects.equals(user.getId(), UserHolder.getUserId())){
                        user.setMeetingId(meeting.getMeetingId());
                        user.setMeetingNumber(meetingNumber);
                        user.setMeetingPermission(2);
                    }
                }
                //创建者加入会议, 开始会议
                startMeeting(meeting);

                return Result.success("加入会议成功!");
            }
            else{
                return Result.error("会议尚未开始!");
            }
        }
        else if(Objects.equals(meeting.getMeetingState(), "on")){
            if(meeting.getMeetingPassword().equals(meetingPassword)){
                //用户为会议参与者, 在用户list中修改相关信息
                for(User user:userList){
                    if(Objects.equals(user.getId(), UserHolder.getUserId())){
                        user.setMeetingId(meeting.getMeetingId());
                        user.setMeetingNumber(meetingNumber);
                        user.setMeetingPermission(meeting.getDefaultPermission());
                        //加入会议房间
                        roomService.joinRoom(meeting.getMeetingNumber(),user);
                        break;
                    }
                }
                return Result.success("加入会议成功!");
            }
            else{
                return Result.error("密码错误!");
            }
        }
        else if(Objects.equals(meeting.getMeetingState(), "end")){
            return Result.error("会议已结束!");
        }
        else{
            return Result.error("发生未知错误!");
        }

    }

    @Override
    public void startMeeting(Meeting meeting) {
        roomService.addRoom(meeting);

        //更改数据库中会议状态
        meetingMapper.update(lambdaUpdate()
                .eq(Meeting::getMeetingId, meeting.getMeetingId())
                .set(Meeting::getMeetingState, "on")
                .getWrapper()
        );

        //System.out.println("存在"+roomList.size()+"个房间!");

    }

    @Override
    public void endMeeting(Meeting meet) {

        UpdateWrapper<Meeting> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("meeting_number", meet.getMeetingNumber());

        //更改数据库中会议状态
        meetingMapper.update(meet, updateWrapper);
    }

    @Override
    public void leaveMeeting() {
        for (User user : userList) {
            if (Objects.equals(user.getId(), UserHolder.getUserId())) {
                //用户在某个会议中权限为2及创建者, 退出会议即为结束会议

                //System.out.println(user.getId()+" "+user.getMeetingNumber()+" "+ user.getMeetingPermission());

                if (user.getMeetingPermission() == 2) {
                    Meeting meet = roomService.deleteRoom( user.getMeetingNumber());
                    endMeeting( meet);
                } else {
                    roomService.leaveRoom(user.getMeetingNumber());
                }

                //System.out.println("存在"+roomList.size()+"个房间!");

                break;
            }

        }

    }

}




