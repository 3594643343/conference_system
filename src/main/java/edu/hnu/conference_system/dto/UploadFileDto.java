package edu.hnu.conference_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileDto {
    /**
     * 上传的会议号
     */
    private String meetingNumber;
    /**
     * 上传的文件
     */
    private MultipartFile file;
}
