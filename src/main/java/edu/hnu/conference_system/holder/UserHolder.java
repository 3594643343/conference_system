package edu.hnu.conference_system.holder;


import edu.hnu.conference_system.dto.UserDto;

public class UserHolder {
    private static final ThreadLocal<UserDto> THREAD_LOCAL = new ThreadLocal<>();

    private UserHolder() {
    }

    /**
     * 获取线程中的用户
     * @return 用户信息
     */
    public static UserDto getUserInfo() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置当前线程中的用户
     * @param info 用户信息
     */
    public static void setUserInfo(UserDto info) {
        THREAD_LOCAL.set(info);
    }

    public static Integer getUserId() {
        UserDto dto = THREAD_LOCAL.get();
        if (dto != null) {
            return dto.getUserId();
        } else {
            // 注册或登录时没有，返回 0
            return 0;
        }
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
