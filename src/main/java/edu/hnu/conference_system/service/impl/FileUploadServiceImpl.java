package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.FileUpload;
import edu.hnu.conference_system.service.FileUploadService;
import edu.hnu.conference_system.mapper.FileUploadMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【file_upload】的数据库操作Service实现
* @createDate 2024-11-12 23:32:40
*/
@Service
public class FileUploadServiceImpl extends ServiceImpl<FileUploadMapper, FileUpload>
    implements FileUploadService{

}




