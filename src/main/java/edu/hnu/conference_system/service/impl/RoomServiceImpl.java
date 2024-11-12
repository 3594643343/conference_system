package edu.hnu.conference_system.service.impl;

import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.Room;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.dto.*;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.*;
import edu.hnu.conference_system.vo.UserInfoVo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
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


    @Value("${files-upload-url.file}")
    private String filePath;

    @Resource
    UserInMeetingService userInMeetingService;

    @Resource
    UserInfoService userInfoService;

    @Resource
    FileService fileService;

    @Resource
    FileUploadService fileUploadService;

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
                //TODO 记录会议音频、纪要等到数据库

                //为meeting赋值
                meeting.setMeetingNumber(meetingNumber);
                meeting.setMeetingEndTime(LocalDateTime.now());
                meeting.setMeetingTime(ofSecondOfDay(LocalDateTime.now().toLocalTime().toSecondOfDay()-
                        room.getStartTime().toLocalTime().toSecondOfDay())
                );
                meeting.setParticipantCount(room.getMembersOn().size());
                meeting.setMeetingState("end");
                //TODO 保存会议音频、纪要等id到meeting表


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
     * 传入会议号
     * 返回在会者信息(id 头像 昵称 个性签名)用于侧边显示
     * @return
     */
    @Override
    public Result getUserInfo(Map<String,String> request) {
        List<UserInfoVo> userInfoVos = new ArrayList<>();
        for (Room room : roomList) {
            if(room.getMeetingNumber().equals(request.get("meetingNumber"))) {
                for(User user : room.getMembersOn()) {
                    userInfoVos.add(userInfoService.buildUserInfoVo(user.getId()));
                }
                break;
            }
        }
        System.out.println("在会人:"+userInfoVos.size()+"/n"+"房间数"+roomList.size());
        return Result.success(userInfoVos);
    }

    /**
     * 禁言
     * @param muteDto
     * @return
     */
    @Override
    public Result mute(MuteDto muteDto) {

        for(Room room : roomList) {
            if(room.getMeetingNumber().equals(muteDto.getMeetingNumber())) {
                for(User user : room.getMembersOn()) {
                    if(user.getId().equals(muteDto.getUserId())) {
                        //TODO 禁言的实现
                        return Result.success("已禁言"+user.getUsername());
                    }
                }
                return Result.error("用户解析错误!");
            }
        }
        return Result.error("房间解析错误!");
    }

    /**
     * 解除禁言
     * @param muteDto
     * @return
     */
    @Override
    public Result disMute(MuteDto muteDto) {

        for(Room room : roomList) {
            if(room.getMeetingNumber().equals(muteDto.getMeetingNumber())) {
                for(User user : room.getMembersOn()) {
                    if(user.getId().equals(muteDto.getUserId())) {
                        //TODO 解除禁言的实现
                        return Result.success("已解除"+user.getUsername()+"禁言");
                    }
                }
                return Result.error("用户解析错误!");
            }
        }
        return Result.error("房间解析错误!");
    }


    /**
     *修改用户权限
     * @param pmChangeDto
     * @return
     */
    @Override
    public Result permissionChange(PmChangeDto pmChangeDto) {
        for(Room room : roomList) {
            if(room.getMeetingNumber().equals(pmChangeDto.getMeetingNumber())) {
                for(User user : room.getMembersOn()) {
                    if(user.getId().equals(pmChangeDto.getId())) {
                        user.setMeetingPermission(pmChangeDto.getPermission());
                        return Result.success("成功将"+user.getUsername()+"设置为"+
                                (pmChangeDto.getPermission()==0?"参会者":"管理员"));
                    }
                }
                return Result.error("用户解析错误!");
            }
        }
        return Result.error("房间解析错误!");
    }

    /**
     * 踢人
     * @param kickDto
     * @return
     */
    @Override
    public Result kickOneOut(KickDto kickDto) {
        for(Room room : roomList) {
            if(room.getMeetingNumber().equals(kickDto.getMeetingNumber())) {
                for(User user : room.getMembersOn()) {
                    if(user.getId().equals(kickDto.getId())) {
                        //将离开会议的user与会议相关的变量清空,再把它从这个房间里踢出
                        user.setMeetingNumber(null);
                        user.setMeetingId(null);
                        user.setMeetingPermission(-1);
                        room.getMembersOn().remove(user);
                        return Result.success("成功踢出"+user.getUsername());
                    }
                }
                return Result.error("用户解析错误!");
            }
        }
        return Result.error("房间解析错误!");
    }

    /**
     * 文件上传
     * @param uploadFileDto
     * @return
     */
    @Override
    public Result uploadFile(UploadFileDto uploadFileDto) {
        String meetingNumber = uploadFileDto.getMeetingNumber();
        MultipartFile file = uploadFileDto.getFile();

        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
        String fileNameWithoutExt = fileName.substring(0,fileName.lastIndexOf("."));


        /*创建存放文件的文件夹,格式如:
        D://file/演示文件_JDK234Io9_小王
        */
        String dirPath = filePath + "/"+fileNameWithoutExt+"_"+meetingNumber+"_"+UserHolder.getUserInfo().getUserName();
        File fileDir = new File(dirPath);
        try{
            fileDir.mkdirs();
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        String path = dirPath+"/"+fileName;
        try {
            //byte[] bytes = file.getBytes();
            File newFile = new File(path);
            file.transferTo(newFile);

            Long meetingId = null;
            for (Room room : roomList) {
                if(room.getMeetingNumber().equals(meetingNumber)) {
                    meetingId = room.getMeetingId();
                }
            }

            //文件数据库记录该文件
            FileDto fileDto = new FileDto(meetingId,fileName,fileType,path);
            fileService.insertFile(fileDto);

            return Result.success("上传成功!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
