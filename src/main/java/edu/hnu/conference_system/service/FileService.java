package edu.hnu.conference_system.service;

import edu.hnu.conference_system.domain.File;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hnu.conference_system.dto.FileDto;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.FileListVo;

/**
* @author lenovo
* @description 针对表【file】的数据库操作Service
* @createDate 2024-11-11 18:59:56
*/
public interface FileService extends IService<File> {
    String insertFile(FileDto fileDto);

    FileListVo buildFileListVo(String id);

    Result downloadFile(String fileId);
}
