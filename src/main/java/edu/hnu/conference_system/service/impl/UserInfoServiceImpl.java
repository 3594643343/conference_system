package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.domain.UserInfo;
import edu.hnu.conference_system.dto.LoginDto;
import edu.hnu.conference_system.dto.PasswordChangeDto;
import edu.hnu.conference_system.dto.UserDto;
import edu.hnu.conference_system.dto.UserInfoDto;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.mapper.UserInfoMapper;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.EmailLoginVo;
import edu.hnu.conference_system.vo.LoginVo;
import edu.hnu.conference_system.utils.Base64Utils;
//import edu.hnu.conference_system.vo.UserBriefVo;
import edu.hnu.conference_system.vo.UserInfoVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import edu.hnu.conference_system.service.UserInfoService;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
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

    @Value("${files-upload-url.avatar}")
    private String filePath;

    @Value("${files-upload-url.default-avatar}")
    private String defaultAvatar;

    @Resource
    UserInfoMapper userMapper;

    @Override
    public Result passLogin(LoginDto loginDto) {
        String name = loginDto.getUserName();
        String email = loginDto.getUserEmail();
        if(name == null && email == null){
            return Result.error("请输入用户名或邮箱!");
        }
        String loginStr = (name == null) ? email : name;
        String col = (name == null) ? "user_email" : "user_name";
        String password = EncryptedPassword(loginDto.getUserPassword());

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
    public Result userRegister(String userName,String userEmail ,String userPassword, String checkPassword) throws IOException {
        //1.校验 账户、密码、效验码不可为空
        if (StringUtils.isAnyBlank(userName,userEmail ,userPassword, checkPassword)) {
            return Result.error("用户名、邮箱或密码不能为空!");
        }
        if (userName.length() < 3) {//账号长度不可小于3
            return Result.error("用户名长度不可小于3!");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {//密码长度不可小于6
            return Result.error("密码长度不可小于6!");
        }
        //账号不可重复（同一账号不可重复注册）
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return Result.error("用户名已被使用!");
        }
        //账号不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userName);
        if (matcher.find()) {
            return Result.error("用户名中不能有特殊字符!");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return Result.error("两次密码不同!");
        }
        //2.加密
        userPassword = EncryptedPassword(userPassword);
        //3.插入数据
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserEmail(userEmail);
        userInfo.setUserPassword(userPassword);
        userInfo.setUserRegTime(LocalDateTime.now());
        userInfo.setIsAdmin(false);
        userInfo.setUserSignature("尚未设置个性签名");

        boolean saveResult = this.save(userInfo);
        if (!saveResult) {
            return Result.error("内部错误!");
        }
        String avatarPath = filePath+"/"+userInfo.getUserId()+".jpg";
        userInfo.setAvatarPath(avatarPath);
        boolean f = setDefaultAvatar(avatarPath);
        if(!f){
            return Result.error("内部错误!");
        }
        userMapper.update(userInfo, new UpdateWrapper<UserInfo>().eq("user_id", userInfo.getUserId()));
        return Result.success(userInfo.getUserId());
    }
    private Boolean setDefaultAvatar(String path) throws IOException {
        FileOutputStream file = new FileOutputStream(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(defaultAvatar));
        }catch (Exception e) {
            return false;
        }
        fis.transferTo(file);
        fis.close();
        file.close();
        return true;
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

    @Override
    public String getNameById(Long userId) {
        UserInfo u = userMapper.selectById(userId);
        return u.getUserName();
    }

    @Override
    public Result changeUserInfo(UserInfoDto userInfoDto){

        if(Objects.equals(userInfoDto.getUserName(), "")){
            return Result.error("用户名不能为空!");
        }
        //账号不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userInfoDto.getUserName());
        if (matcher.find()) {
            return Result.error("用户名中不能有特殊字符!");
        }
        UserInfo userInfo = userMapper.selectOne(
                new QueryWrapper<UserInfo>().eq("user_name", userInfoDto.getUserName())
        );

        if(userInfo != null && !Objects.equals(userInfo.getUserId(), UserHolder.getUserId())){
            return Result.error("已存在昵称为"+userInfo.getUserName()+"的用户");
        }

        //没有重复用户名
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUserName(userInfoDto.getUserName());
        newUserInfo.setUserSignature(userInfoDto.getSignature());


        userMapper.update(newUserInfo,new UpdateWrapper<UserInfo>().eq("user_id", UserHolder.getUserId()));
        return Result.success("修改成功!");
    }

    @Override
    public Result changePassword(PasswordChangeDto passwordChangeDto) {
        if(!Objects.equals(passwordChangeDto.getNewPassword(), passwordChangeDto.getConfirmPassword())){
            return Result.error("两次密码不一致!");
        }
        if(passwordChangeDto.getNewPassword().length()<6){
            return Result.error("密码长度不能小于3!");
        }
        UserInfo user = userMapper.selectById(UserHolder.getUserId());
        if(!Objects.equals(user.getUserPassword(), EncryptedPassword(passwordChangeDto.getOldPassword()))){
            return Result.error("密码错误!");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserPassword(EncryptedPassword(passwordChangeDto.getNewPassword()));
        userMapper.update(userInfo,new UpdateWrapper<UserInfo>().eq("user_id", user.getUserId()));
        return Result.success("修改成功!");
    }

    @Override
    public Result changeAvatar(MultipartFile avatar) throws IOException {
        if(avatar == null){
            return Result.error("未上传文件!");
        }
        UserInfo userInfo = userMapper.selectOne(
                new QueryWrapper<UserInfo>().eq("user_id", UserHolder.getUserId())
        );
        String oldAvatar = userInfo.getAvatarPath();
        String newAvatar = null;
        if(oldAvatar != null){
            newAvatar = oldAvatar;
        }
        else{
            newAvatar = filePath+"/"+userInfo.getUserId()+".jpg";
        }
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setAvatarPath(newAvatar);

        File file = new File(newAvatar);
        try {
            avatar.transferTo(file);
        }
        catch (IOException e) {
            throw new IOException(e.getMessage());
        }

        userMapper.update(newUserInfo,new UpdateWrapper<UserInfo>().eq("user_id", userInfo.getUserId()));
        return Result.success("修改成功!");
    }

    @Override
    public Result resetPassword(Long userId) {
        UserInfo userInfo = userMapper.selectById(userId);
        //将密码重置为123456
        userInfo.setUserPassword(EncryptedPassword("123456"));
        userMapper.update(userInfo,new UpdateWrapper<UserInfo>().eq("user_id", userId));
        return Result.success("重置成功!");
    }

    @Override
    public Long getMeetingIdByUserId(Long userId) {
        for(User user:userList){
            if(user.getId().equals(userId)){
                return user.getMeetingId();
            }
        }
        return (long) -1;
    }


}




