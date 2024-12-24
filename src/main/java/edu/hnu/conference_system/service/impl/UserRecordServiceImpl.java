package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.Room;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.domain.UserRecord;
import edu.hnu.conference_system.mapper.FileMapper;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.*;
import edu.hnu.conference_system.mapper.UserRecordMapper;
import edu.hnu.conference_system.vo.FileListVo;
import edu.hnu.conference_system.vo.RecordDetailVo;
import edu.hnu.conference_system.vo.RecordVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static edu.hnu.conference_system.service.impl.UserInfoServiceImpl.userList;

/**
* @author lenovo
* @description 针对表【user_record】的数据库操作Service实现
* @createDate 2024-11-15 16:13:16
*/
@Service
public class UserRecordServiceImpl extends ServiceImpl<UserRecordMapper, UserRecord>
    implements UserRecordService{

    @Resource
    private UserRecordMapper userRecordMapper;
    @Resource
    private MeetingService meetingService;
    @Resource
    private UserInMeetingService userInMeetingService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private MeetingsMinutesService meetingsMinutesService;
    @Resource
    private RecordService recordService;
    @Resource
    private MeetingAudioService meetingAudioService;
    @Resource
    private FileService fileService;

    @Override
    public Result getRecordList(Integer userId) {
        List<RecordVo> recordVos = new ArrayList<>();
        List<UserRecord> userRecords = userRecordMapper.selectList(
                new QueryWrapper<UserRecord>().eq("user_id", userId)
        );
        System.out.println("共有记录条数: "+userRecords.size());

        for(UserRecord userRecord : userRecords){
            Long meetingId = userRecord.getMeetingId();
            RecordVo recordVo = meetingService.buildRecordVoById(meetingId);
            recordVo.setRecordId(userRecord.getUserRecordId());

            List<Integer> usersIds = userInMeetingService.getUsersIdsFromMeetingId(meetingId);

            for(Integer usersId : usersIds){
                recordVo.getParticipants().add(userInfoService.getNameById(usersId));
            }
            recordVos.add(recordVo);
        }
        return Result.success(recordVos);

    }

    @Override
    public Result deleteRecord(Long recordId) {
        userRecordMapper.deleteById(recordId);
        return Result.success("成功删除记录!");
    }

    @Override
    public Result getRecordDetail(Long recordId) throws IOException {
        UserRecord userRecord = userRecordMapper.selectById(recordId);
        if(userRecord == null){
            throw new IOException("没有该记录");
        }
        Long meetingId = userRecord.getMeetingId();

        //Long meetingMinutesId = meetingService.getMeetingMinutesId(meetingId);
        Long meetingRecordId = meetingService.getMeetingRecordId(meetingId);
        Long meetingAudioId = meetingService.getMeetingAudioId(meetingId);

        //String minutes = meetingsMinutesService.getMinutesById(meetingMinutesId);
        String record = meetingRecordId == null? null:recordService.getRecordById(meetingRecordId);

        String audio = meetingAudioId == null? null:meetingAudioService.getAudioById(meetingAudioId);

        RecordDetailVo recordDetailVo = new RecordDetailVo();
        recordDetailVo.setMeetingRecord(record);
        recordDetailVo.setMeetingAudio(audio);
        //recordDetailVo.setMeetingMinutes(minutes);

        return Result.success(recordDetailVo);
    }

    @Override
    public void insertRecord(Integer userId, Long meetingId) {
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userId);
        userRecord.setMeetingId(meetingId);
        userRecordMapper.insert(userRecord);
    }

    @Override
    public Result getFileList(Long recordId) {
        UserRecord userRecord = userRecordMapper.selectById(recordId);
        if(userRecord == null){
            return Result.error("未找到会议记录!");
        }
        Long meetingId = userRecord.getMeetingId();
        List<FileListVo> fileListVos = fileService.getFileListByMeetingId(meetingId);

        return Result.success(fileListVos);
    }

//    @Override
//    public Result downloadFile(String fileId) {
//        return fileService.downloadFile(fileId);
//    }

    @Override
    public Result downloadFile(HttpServletResponse response, String fileId) {
        edu.hnu.conference_system.domain.File myFile = fileService.getFileById(fileId) ;

        File file = new File(myFile.getFilePath());
        if(!file.exists()){
            return Result.error("下载文件不存在");
        }
        response.reset();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return download(response, file);
    }

    @Override
    public Result downloadAudio(HttpServletResponse response, Long recordId) {
        UserRecord userRecord = userRecordMapper.selectById(recordId);
        Long meetingId = userRecord.getMeetingId();
        String audioPath = meetingAudioService.getAudioPathByMeetingId(meetingId);
        String name = meetingService.getMeetingNameById(meetingId);

        File file = new File(audioPath);
        if(!file.exists()){
            return Result.error("下载文件不存在");
        }
        response.reset();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return download(response, file,name);
    }

    private Result download(HttpServletResponse response, File file){
        return download(response, file, null);
    }
    private Result download(HttpServletResponse response, File file,String name) {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(Objects.requireNonNullElseGet(name, file::getName), StandardCharsets.UTF_8));


        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            return Result.error("下载失败");
        }
        return Result.success("下载成功");
    }
}




