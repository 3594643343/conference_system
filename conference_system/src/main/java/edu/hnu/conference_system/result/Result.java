package edu.hnu.conference_system.result;

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
public class Result<T> implements Serializable {
    /**
     * 请求时间
     */
    private LocalDateTime time;
    /**
     * 编码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;



    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setTime(LocalDateTime.now());
        result.setCode(1);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setTime(LocalDateTime.now());
        result.setCode(1);
        result.setData(data);
        return result;
    }
    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setTime(LocalDateTime.now());
        result.setCode(1);
        result.setMsg(message);
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setTime(LocalDateTime.now());
        result.setCode(0);
        result.setMsg(message);
        return result;
    }
}

