package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 会议中演示文件时用
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileShowVo {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 页数
     */
    private int pageNumber;
    /**
     * 文件转化成的图片数组
     */
    private List<String> FilePics;
}
