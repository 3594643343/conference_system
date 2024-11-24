package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.hnu.conference_system.domain.Meeting;
import edu.hnu.conference_system.domain.Room;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.dto.*;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.*;
import edu.hnu.conference_system.utils.Base64Utils;
import edu.hnu.conference_system.utils.FileToPicUtils;
import edu.hnu.conference_system.vo.CreateMeetingVo;
import edu.hnu.conference_system.vo.FileShowVo;
import edu.hnu.conference_system.vo.UserInfoVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    MeetingService meetingService;

    @Resource
    UserInMeetingService userInMeetingService;

    @Resource
    UserInfoService userInfoService;

    @Resource
    FileService fileService;

    @Resource
    UserRecordService userRecordService;

    @Override
    public CreateMeetingVo bookMeeting(BookMeetingDto bookMeetingDto) {
        String meetingNumber = RandomStringUtils.randomAlphanumeric(9);
        //System.out.println(meetingNumber);
        Meeting meeting = new Meeting(bookMeetingDto.getMeetingName(), meetingNumber, bookMeetingDto.getMeetingPassword(),
                UserHolder.getUserId(),bookMeetingDto.getMeetingStartTime(),bookMeetingDto.getMeetingEndTime(),
                bookMeetingDto.getDefaultPermission());


        meetingService.insertMeeting(meeting);
        return new CreateMeetingVo(meetingNumber,bookMeetingDto.getMeetingPassword());
    }

    @Override
    public Result quickMeeting() {
        String meetingNumber = RandomStringUtils.randomAlphanumeric(9);
        Meeting meeting = new Meeting(UserHolder.getUserInfo().getUserName(), meetingNumber,
                UserHolder.getUserId(), LocalDateTime.now() );
        meetingService.insertMeeting(meeting);
        //加入会议

        JoinMeetingDto joinMeetingDto = new JoinMeetingDto(meetingNumber,null);
        joinMeeting(joinMeetingDto);
        return Result.success(new CreateMeetingVo(meetingNumber,"0"));

    }

    @Override
    public Result joinMeeting(JoinMeetingDto joinMeetingDto) {
        String meetingNumber = joinMeetingDto.getMeetingNumber();
        String meetingPassword = joinMeetingDto.getMeetingPassword();

        Meeting meeting = meetingService.selectByNumber(meetingNumber);

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
                        joinRoom(meeting.getMeetingNumber(),user);
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
    public Result leaveMeeting() {
        for (User user : userList) {
            if (Objects.equals(user.getId(), UserHolder.getUserId())) {
                if (user.getMeetingPermission() == 2) {
                    //用户在某个会议中权限为2及创建者, 退出会议即为结束会议
                    //System.out.println(user.getId()+" "+user.getMeetingNumber()+" "+ user.getMeetingPermission());

                    Meeting meet = deleteRoom(user.getMeetingNumber(),user.getMeetingId());
                    meetingService.endMeeting(meet);
                } else {
                    leaveRoom(user.getMeetingNumber());
                }

                //System.out.println("存在"+roomList.size()+"个房间!");
                break;
            }

        }
        return Result.success("已退出会议!");
    }

    @Override
    public Result joinFromSchedule(String meetingNumber) {
        Meeting meeting = meetingService.selectByNumber(meetingNumber);
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
            //用户为会议参与者, 在用户list中修改相关信息
            for(User user:userList){
                if(Objects.equals(user.getId(), UserHolder.getUserId())){
                    user.setMeetingId(meeting.getMeetingId());
                    user.setMeetingNumber(meetingNumber);
                    user.setMeetingPermission(meeting.getDefaultPermission());
                    //加入会议房间
                    joinRoom(meeting.getMeetingNumber(),user);
                    break;
                }
            }
            return Result.success("加入会议成功!");
        }
        else if(Objects.equals(meeting.getMeetingState(), "end")){
            return Result.error("会议已结束!");
        }
        else{
            return Result.error("发生未知错误!");
        }
    }

    @Override
    public List<Long> getOnMeetingUserId(Long meetingId) {
        for (Room room:roomList){
            if(Objects.equals(room.getMeetingId(), meetingId)){
                List<Long> users = new ArrayList<>();
                for(User user:room.getMembersOn()){
                    users.add(user.getId());
                }
                return users;
            }
        }
        return null;
    }


    private void startMeeting(Meeting meeting) {
        addRoom(meeting);
        //更改数据库中会议状态
        meetingService.turnOnMeeting(meeting.getMeetingId());

        //System.out.println("存在"+roomList.size()+"个房间!");

    }

    /**
     * 实例化一个Room,将room加入到roomlist中
     * @param meeting
     */
    private void addRoom(Meeting meeting) {
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
        System.out.println("房间号:"+room.getMeetingNumber()+" 房间名:"+room.getRoomName()+" 上线");
        System.out.println("当前共有"+roomList.size()+"个房间");
    }

    private void joinRoom(String meetingNumber,User user) {
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

    private void leaveRoom(String meetingNumber) {
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
    private Meeting deleteRoom(String meetingNumber,Long meetingId) {
        Meeting meeting = new Meeting();
        for(Room room : roomList) {
            //找到要关闭的房间
            if(room.getMeetingNumber().equals(meetingNumber)) {

                //记录参会者
                saveAllUserInMeeting(room);
                //TODO 记录会议音频、纪要等到数据库

                //为meeting赋值
                meeting.setMeetingId(meetingId);
                meeting.setMeetingNumber(meetingNumber);
                meeting.setMeetingEndTime(LocalDateTime.now());
                meeting.setMeetingTime(ofSecondOfDay(LocalDateTime.now().toLocalTime().toSecondOfDay()-
                        room.getStartTime().toLocalTime().toSecondOfDay())
                );
                meeting.setParticipantCount(room.getMembersOn().size());
                meeting.setMeetingState("end");
                //TODO 保存会议音频、纪要等id到meeting表


                //为每一个与会者插入记录索引表
                for(Long userId : room.getMembersAll()) {
                    userRecordService.insertRecord(userId,meetingId);
                }

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
    private void saveAllUserInMeeting(Room room) {
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
                System.out.println("找到房间");
                for(User user : room.getMembersOn()) {
                    System.out.println("找到人");
                    userInfoVos.add(userInfoService.buildUserInfoVo(user.getId()));
                }
                break;
            }
        }
        System.out.println("在线用户:"+userList.size()+" 人");
        System.out.println("在会人:"+userInfoVos.size()+"  "+"房间数"+roomList.size());
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

            pushFileToAll(UserHolder.getUserInfo().getUserName(), fileName,path);


            return Result.success("上传成功!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将上传的文件推送给所有人
     * @param path
     */
    @Override
    public void pushFileToAll(String UploadUserName,String fileName,String path) throws Exception {
        FileShowVo fileShowVo = new FileShowVo(fileName,UploadUserName,0,new ArrayList<>());

        //将文件转化成图片以便传输, 转换后得到的是一个文件夹,里面以1.jpg 2.jpg这样命名
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
        if(fileType.equals("ppt")){
            fileShowVo.setPageNumber(FileToPicUtils.pptToPic(path));

        }
        else if (fileType.equals("pdf")) {
            fileShowVo.setPageNumber(FileToPicUtils.pdfToPic(path));

        }
        else{
            throw new RuntimeException("文件异常!");
        }

        String picsPath = path.substring(0,path.lastIndexOf("."))+"_PIC";
        for(int i =0;i<fileShowVo.getPageNumber();i++){
            String pic = picsPath+"/"+(i+1)+".jpg";
            fileShowVo.getFilePics().add(Base64Utils.encode(pic));
        }

        //TODO 将文件推送给所有人


    }






}
