package edu.hnu.conference_system.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hnu.conference_system.result.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/*
发给客户端的响应要求是Result格式的, 如果返回的不是Result格式,会被该类拦截并包装成Result格式
 */
@Slf4j
@RestControllerAdvice("edu.hnu.conference_system.controller") // 指定拦截请求的范围，防止拦截swagger网页
// @RestControllerAdvice(assignableTypes = BaseController.class)
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * Whether this component supports the given controller method return type
     * and the selected {@code HttpMessageConverter} type.
     * @param returnType the return type   方法返回的类型
     * @param converterType the selected converter type   参数类型装换
     * @return {@code true} if {@link #beforeBodyWrite} should be invoked;
     * {@code false} otherwise
     * 返回 true 则下面 beforeBodyWrite方法被调用, 否则就不调用下述方法
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * Invoked after an {@code HttpMessageConverter} is selected and just before
     * its write method is invoked.
     * @param body the body to be written
     * @param returnType the return type of the controller method
     * @param selectedContentType the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request the current request
     * @param response the current response
     * @return the body that was passed in or a modified (possibly new) instance
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof String){ // string 类型直接返回
            //                return objectMapper.writeValueAsString(Result.success(body)); // string 转json
            return body;
        }
        if (body == null) {
            return Result.success();
        }
        if (body instanceof Result){
            return body;
        }
        return Result.success(body);
    }
}

