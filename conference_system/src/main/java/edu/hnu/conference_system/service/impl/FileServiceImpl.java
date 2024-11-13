package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.File;
import edu.hnu.conference_system.service.FileService;
import edu.hnu.conference_system.mapper.FileMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【file】的数据库操作Service实现
* @createDate 2024-11-11 18:59:56
*/
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
    implements FileService{

}




