package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.domain.UserInfo;
import edu.hnu.conference_system.dto.UserDto;
import edu.hnu.conference_system.mapper.UserInfoMapper;
import edu.hnu.conference_system.result.Result;
import edu.hnu.conference_system.vo.LoginVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import edu.hnu.conference_system.service.UserInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Result passLogin(Map<String, String> request) {
        String name = request.get("userName");
        String email = request.get("userEmail");
        String loginStr = (name ==null)?email:name;
        String col = (name ==null)?"user_email":"user_name";
        String password = EncryptedPassword(request.get("userPassword"));

        //System.out.println(loginStr);
        //System.out.println(password);

        UserInfo user = userMapper.selectOne(
                new QueryWrapper<UserInfo>().eq(col, loginStr));


        if(user == null){
            return Result.error("不存在该用户!");
        }else if(user.getUserPassword().equals(password)){
            UserDto userDto = new UserDto(user.getUserId(), user.getUserName());
            String token = generateTokenForUser(userDto);
            User user1 = new User(user.getUserId(),user.getUserName(),null,null,-1,null);
            userList.add(user1);
            LoginVo loginVo = new LoginVo(user.getIsAdmin(),token);
            return Result.success(loginVo);
        }
        else{
            return Result.error("密码错误!");
        }
    }



}




