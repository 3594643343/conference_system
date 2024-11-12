package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.File;
import edu.hnu.conference_system.dto.FileDto;
import edu.hnu.conference_system.service.FileService;
import edu.hnu.conference_system.mapper.FileMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public void insertFile(FileDto fileDto) {
        File file = new File();
        file.setFileName(fileDto.getFileName());
        file.setFileType(fileDto.getFileType());
        file.setMeetingId(fileDto.getMeetingId());
        file.setFilePath(fileDto.getFilePath());
        file.setFileUploadTime(LocalDateTime.now());
        fileMapper.insert(file);
    }
}




