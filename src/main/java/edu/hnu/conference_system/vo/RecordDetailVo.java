package edu.hnu.conference_system.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDetailVo {
    /**
     * 会议纪要
     */
    private String meetingMinutes;

    /**
     * 会议文本记录
     */
    private String meetingRecord;

    /**
     * 文本翻译
     */
    private String meetingTranslation;

    /**
     * 会议音频
     */
    private String meetingAudio;

}
