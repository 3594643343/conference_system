package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.File;
import edu.hnu.conference_system.dto.FileDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.service.FileService;
import edu.hnu.conference_system.mapper.FileMapper;
import edu.hnu.conference_system.utils.Base64Utils;
import edu.hnu.conference_system.utils.FileToPicUtils;
import edu.hnu.conference_system.vo.FileListVo;
import edu.hnu.conference_system.vo.FileShowVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* @author lenovo
* @description 针对表【file】的数据库操作Service实现
* @createDate 2024-11-11 18:59:56
*/
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
    implements FileService{

    @Resource
    private FileMapper fileMapper;

    @Override
    public String insertFile(FileDto fileDto) {
        File file = new File();
        file.setFileName(fileDto.getFileName());
        file.setFileType(fileDto.getFileType());
        file.setMeetingId(fileDto.getMeetingId());
        file.setFilePath(fileDto.getFilePath());
        file.setFileUploadTime(LocalDateTime.now());
        file.setTransformed(0);
        fileMapper.insert(file);
        return file.getFileId();
    }

    @Override
    public FileListVo buildFileListVo(String id) {
        File file = fileMapper.selectById(id);
        FileListVo fileListVo = new FileListVo();
        fileListVo.setFileId(id);
        fileListVo.setFileName(file.getFileName());
        fileListVo.setFileType(file.getFileType());
        return fileListVo;
    }

    @Override
    public Result downloadFile(String fileId) {
        File file = fileMapper.selectById(fileId);
        String fileName = file.getFileName();
        String path = file.getFilePath();
        FileShowVo fileShowVo = new FileShowVo(fileName,0,new ArrayList<>());

        if(file.getTransformed() == 1){
            //已转换过
            fileShowVo.setPageNumber(file.getPageNum());
            String picsPath = path.substring(0,path.lastIndexOf("."))+"_PIC";
            for(int i =0;i<fileShowVo.getPageNumber();i++){
                String pic = picsPath+"/"+(i+1)+".jpg";
                fileShowVo.getFilePics().add(Base64Utils.encode(pic));
            }

        }
        else{
            //将文件转化成图片以便传输, 转换后得到的是一个文件夹,里面以1.jpg 2.jpg这样命名
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            Integer pageNumber = 0;
            if(fileType.equals("pptx")){
                try {
                    pageNumber = FileToPicUtils.pptToPic(path);
                }catch (Exception e){
                    e.printStackTrace();
                }

                fileShowVo.setPageNumber(pageNumber);
                file.setTransformed(1);
                file.setPageNum(pageNumber);

                fileMapper.updateById(file);
            }
            else if (fileType.equals("pdf")) {
                try {
                    pageNumber = FileToPicUtils.pdfToPic(path);
                }catch (Exception e) {
                    e.printStackTrace();
                }

                fileShowVo.setPageNumber(pageNumber);
                file.setTransformed(1);
                file.setPageNum(pageNumber);

                fileMapper.updateById(file);

            }
            else{
                throw new RuntimeException("文件异常!");
            }

            String picsPath = path.substring(0,path.lastIndexOf("."))+"_PIC";
            for(int i =0;i<fileShowVo.getPageNumber();i++){
                String pic = picsPath+"/"+(i+1)+".jpg";
                fileShowVo.getFilePics().add(Base64Utils.encode(pic));
            }
        }
        return Result.success(fileShowVo);
    }

    @Override
    public List<FileListVo> getFileListByMeetingId(Long meetingId) {
        List<FileListVo> fileListVos = new ArrayList<>();
        List<File> files = fileMapper.selectList(
                new QueryWrapper<File>().eq("meeting_id", meetingId)
        );
        for (File file : files) {
            FileListVo fileListVo = new FileListVo();
            fileListVo.setFileId(file.getFileId());
            fileListVo.setFileName(file.getFileName());
            fileListVo.setFileType(file.getFileType());
            fileListVos.add(fileListVo);
        }
        return fileListVos;
    }

    @Override
    public File getFileById(String fileId) {
        return fileMapper.selectById(fileId);
    }
}




