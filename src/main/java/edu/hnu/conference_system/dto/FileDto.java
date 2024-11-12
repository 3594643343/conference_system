package edu.hnu.conference_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    /**
     *
     */
    private Long meetingId;

    /**
     *
     */
    private String fileName;

    /**
     *
     */
    private String fileType;


    /**
     *
     */
    private String filePath;
}
