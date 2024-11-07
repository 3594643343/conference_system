package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.UserInfo;
import edu.hnu.conference_system.dto.LoginDto;
import edu.hnu.conference_system.dto.UserDto;
import edu.hnu.conference_system.mapper.UserInfoMapper;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.LoginVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import edu.hnu.conference_system.service.UserInfoService;

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

    @Resource
    UserInfoMapper userMapper;

    @Override
    public Result userLogin(LoginDto loginDto) {

        UserInfo user = userMapper.selectOne(
                new QueryWrapper<UserInfo>().eq("user_name", loginDto.getUserName()));
        String password = EncryptedPassword(loginDto.getUserPassword());
        System.out.println(password);
        if(user == null){
            return Result.error("不存在该用户!");
        }else if(user.getUserPassword().equals(password)){
            UserDto userDto = new UserDto(user.getUserId(), user.getUserName());
            String token = generateTokenForUser(userDto);
            return Result.success(token);
        }
        else{
            return Result.error("密码错误!");
        }
    }
}




