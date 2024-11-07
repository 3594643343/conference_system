package edu.hnu.conference_system.utils;

import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import edu.hnu.conference_system.dto.UserDto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/*
  用于生成与解析token令牌
 */
public class JwtUtils {

    /**
     * 密钥
     */
    private static final byte[] KEY = "conference".getBytes();

    /**
     * 过期时间（秒）：7 天
     */
    public static final long EXPIRE = 7 * 24 * 60 * 60*1000;

    private JwtUtils() {
    }

    /**
     * 根据 userDto 生成 token
     *
     * @param dto    用户信息
     * @return token
     */
    public static String generateTokenForUser(UserDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", dto.getUserId());
        map.put("userName", dto.getUserName());

        return generateToken(map);
    }

    /**
     * 根据 map 生成 token 默认：HS265(HmacSHA256)算法
     *
     * @param map    携带数据
     * @return token
     */
    public static String generateToken(Map<String, Object> map) {
        JWT jwt = JWT.create();
        // 设置携带数据
        map.forEach(jwt::setPayload);
        // 设置密钥
        jwt.setKey(KEY);
        // 设置过期时间
        jwt.setExpiresAt(new Date(System.currentTimeMillis() + EXPIRE));
        return jwt.sign();
    }

    /**
     * token 校验
     * @param token token
     * @return 是否通过校验
     */
    public static boolean verify (String token) {
        if (StringUtils.isBlank(token)) return false;
        return JWT.of(token).setKey(KEY).verify();
    }

    /**
     * token 校验，并获取 userDto
     * @param token token
     * @return userDto
     */
    public static UserDto verifyAndGetUser(String token) {
        if(!verify(token)) return null;
        // 解析数据
        JWT jwt = JWT.of(token);
        Long id = Long.valueOf((jwt.getPayload("userId").toString()));
        String account = jwt.getPayload("userName").toString();
        // 返回用户信息
        return new UserDto(id, account);
    }

}
