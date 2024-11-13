package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.domain.UserInfo;
import edu.hnu.conference_system.dto.LoginDto;
import edu.hnu.conference_system.dto.UserDto;
import edu.hnu.conference_system.mapper.UserInfoMapper;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.utils.Base64Utils;
import edu.hnu.conference_system.vo.LoginVo;
import edu.hnu.conference_system.vo.UserInfoVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import edu.hnu.conference_system.service.UserInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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


    @Resource
    UserInfoMapper userMapper;

    @Override
    public Result passLogin(LoginDto loginDto) {
        String name = loginDto.getUserName();
        String email = loginDto.getUserEmail();
        if(name == null && email == null) {
            return Result.error("请输入账号或邮箱!");
        }
        String loginStr = (Objects.equals(name, ""))?email:name;
        String col = (Objects.equals(name, ""))?"user_email":"user_name";
        String password = EncryptedPassword(loginDto.getUserPassword());

        System.out.println(name);
        System.out.println(email);
        System.out.println(loginStr);
        System.out.println(loginDto.getUserPassword());
        System.out.println(password);

        UserInfo user = userMapper.selectOne(
                new QueryWrapper<UserInfo>().eq(col, loginStr));


        if(user == null){
            return Result.error("不存在该用户!");
        }else if(user.getUserPassword().equals(password)){
            UserDto userDto = new UserDto(user.getUserId(), user.getUserName());
            String token = generateTokenForUser(userDto);
            User user1 = new User(user.getUserId(),user.getUserName(),null,null,-1,null);
            userList.add(user1);
            System.out.println("id:"+user1.getId()+"  用户名:"+user1.getUsername()+"登录");
            LoginVo loginVo = new LoginVo(user.getIsAdmin(),token);
            return Result.success(loginVo);
        }
        else{
            return Result.error("密码错误!");
        }
    }

    /**
     * 生成一个UserInfoVo
     * @param id
     * @return
     */
    @Override
    public UserInfoVo buildUserInfoVo(Long id) {
        UserInfoVo userInfoVo = new UserInfoVo();

        UserInfo userInfo = userMapper.selectOne(
                new QueryWrapper<UserInfo>().eq("user_id",id));
        userInfoVo.setId(userInfo.getUserId());
        userInfoVo.setUsername(userInfo.getUserName());
        if(userInfo.getAvatarPath() == null){
            userInfoVo.setAvatar("No Avatar");
        }
        else {
            userInfoVo.setAvatar(Base64Utils.encode(userInfo.getAvatarPath()));
        }
        userInfoVo.setSignature(userInfo.getUserSignature());

        return userInfoVo;
    }


}




