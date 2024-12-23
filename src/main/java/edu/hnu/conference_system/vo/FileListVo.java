package edu.hnu.conference_system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileListVo {
    private String fileName;
    private String fileId;
    private String fileType;
}
