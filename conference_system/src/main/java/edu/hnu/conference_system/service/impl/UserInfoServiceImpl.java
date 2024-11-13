package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.domain.UserInfo;
import edu.hnu.conference_system.dto.UserDto;
import edu.hnu.conference_system.mapper.UserInfoMapper;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.EmailLoginVo;
import edu.hnu.conference_system.vo.LoginVo;
import edu.hnu.conference_system.utils.Base64Utils;
//import edu.hnu.conference_system.vo.UserBriefVo;
import edu.hnu.conference_system.vo.UserInfoVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import edu.hnu.conference_system.service.UserInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.hnu.conference_system.utils.JwtUtils.generateTokenForUser;
import static edu.hnu.conference_system.utils.SecurityUtil.EncryptedPassword;

/**
* @author lenovo
* @description 针对表【user_info】的数据库操作Service实现
* @createDate 2024-11-07 10:29:00
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService {

    /**
     * 存放在线的用户
     */
    static List<User> userList = new ArrayList<>();
    @Override
    public User getUserById(Long userId) {
        Optional<User> user = userList.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst();
        return user.orElse(null);
    }

    @Resource
    UserInfoMapper userMapper;

    @Override
    public Result passLogin(Map<String, String> request) {
        String name = request.get("userName");
        String email = request.get("userEmail");
        String loginStr = (name == null) ? email : name;
        String col = (name == null) ? "user_email" : "user_name";
        String password = EncryptedPassword(request.get("userPassword"));

        //System.out.println(loginStr);
        //System.out.println(password);

        UserInfo user = userMapper.selectOne(
                new QueryWrapper<UserInfo>().eq(col, loginStr));


        if (user == null) {
            return Result.error("不存在该用户!");
        } else if (user.getUserPassword().equals(password)) {
            UserDto userDto = new UserDto(user.getUserId(), user.getUserName());
            String token = generateTokenForUser(userDto);
            User user1 = new User(user.getUserId(), user.getUserName(), null, null, -1, null);
            userList.add(user1);
            LoginVo loginVo = new LoginVo(user.getIsAdmin(), token);
            return Result.success(loginVo);
        } else {
            return Result.error("密码错误!");
        }
    }

    @Override
    public Result emailLogin(EmailLoginVo emailLoginVo) {
        // 验证邮箱格式
        if (!isValidEmail(emailLoginVo.getEmail())) {
            return Result.error("无效的邮箱地址");
        }

        // 验证验证码是否正确
        if (!isValidCode(emailLoginVo.getCode())) {
            return Result.error("验证码错误");
        }

        // 验证邮箱是否已注册
        if (!isEmailRegistered(emailLoginVo.getEmail())) {
            return Result.error("该邮箱未注册");
        }

        // 验证通过，生成 token 或其他登录凭证
        // 这里可以调用 DAO 层的方法来查询用户信息并生成 token
        // ...

        return Result.success("登录成功");
    }

    private boolean isValidEmail(String email) {
        // 实现邮箱格式验证逻辑
        // 可以使用正则表达式等方法
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private boolean isValidCode(String code) {
        // 实现验证码验证逻辑
        // 可以从缓存或数据库中获取验证码并进行比对
        // ...
        return true; // 示例中假设验证码总是正确的
    }

    private boolean isEmailRegistered(String email) {
        // 实现检查邮箱是否已注册的逻辑
        // 可以查询数据库
        // ...
        return true; // 示例中假设邮箱总是已注册
    }

    @Override
    public long userRegister(String userName, String userPassword, String checkPassword) {
        //1.校验 账户、密码、效验码不可为空
        if (StringUtils.isAnyBlank(userName, userPassword, checkPassword)) {
            return -1;
        }
        if (userName.length() < 4) {//账号长度不可小于4
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {//密码长度不可小于8
            return -1;
        }
        //账号不可重复（同一账号不可重复注册）
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userName);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        //账号不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userName);
        if (matcher.find()) {
            return -1;
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //2.加密（暂且不做）
        //3.插入数据
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(userPassword);
        boolean saveResult = this.save(userInfo);
        if (!saveResult) {
            return -1;
        }
        return userInfo.getUserId();
    }
    public UserInfoVo buildUserInfoVo(Long id) {
        UserInfoVo userInfoVo = new UserInfoVo();

        UserInfo userInfo = userMapper.selectOne(
                new QueryWrapper<UserInfo>().eq("user_id",id));
        userInfoVo.setId(userInfo.getUserId());
        userInfoVo.setUsername(userInfo.getUserName());
        userInfoVo.setAvatar(Base64Utils.encode(userInfo.getAvatarPath()));
        userInfoVo.setSignature(userInfo.getUserSignature());

        return userInfoVo;
    }


}



