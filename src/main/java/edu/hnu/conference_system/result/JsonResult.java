package edu.hnu.conference_system.result;


import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonResult implements Serializable {

    /** 0:错误消息   1:聊天消息  2:验证消息
     * 编码
     */
    private Integer code;

    /**
     * 数据
     */
    private JSONObject data;


    public static JsonResult errJson(String message){
        JSONObject innerJson = new JSONObject();
        innerJson.set("message", message);
        return JsonResult.builder().code(0).data(innerJson).build();
    }

    public static JsonResult chatJson(Integer senderId,String time,String content){
        JSONObject innerJson = new JSONObject();
        innerJson.set("senderId", senderId);
        innerJson.set("time", time);
        innerJson.set("content", content);
        return JsonResult.builder().code(1).data(innerJson).build();
    }

}
