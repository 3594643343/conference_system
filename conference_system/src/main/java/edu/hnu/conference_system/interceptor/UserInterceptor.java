package edu.hnu.conference_system.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import edu.hnu.conference_system.dto.UserDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/*
  用于拦截前端请求,检查token是否正确,正确放行,反之返回401
 */
@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isNotBlank(token)) {
            UserDto dto = JwtUtils.verifyAndGetUser(token);
            if (dto != null) {
                UserHolder.setUserInfo(dto);
                return true;
            } else {
                System.out.println("token 验证失败1！");
                //log.error("token 验证失败！token is {}, uri is {}", token, request.getRequestURI());
                response.setStatus(401);
                return false;
            }
        } else {
            System.out.println("token 验证失败2！");
           // log.error("token 验证失败！token is {}, uri is {}", token, request.getRequestURI());
            response.setStatus(401);
            return false;
        }

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理掉当前线程中的数据，防止内存泄漏
        UserHolder.remove();
    }


}
