package com.xyz.rbac.service;

import com.xyz.rbac.cache.keys.UserKey;
import com.xyz.rbac.cache.redis.RedisService;
import com.xyz.rbac.data.domain.User;
import com.xyz.rbac.data.mapper.UserMapper;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.util.MD5Util;
import com.xyz.rbac.util.UUIDUtil;
import com.xyz.rbac.validation.LoginNameConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    public User login(HttpServletResponse response, String loginName, String loginPassword) {
        Long loginTimes = redisService.get(UserKey.LOGIN_TIMES, loginName, Long.class);
        if (loginTimes!=null&&loginTimes >= 10) {//超出限制
            throw new BusinessException(Result.USER_TRY_LOGIN_LIMIT);
        }
        //判断用户是否正确
        User user = null;
        Matcher m = LoginNameConstraint.EMAIL_PATTERN.matcher(loginName);
        if (m.matches()) {
            user = userMapper.getByEmail(loginName);
        } else {
            m = LoginNameConstraint.MOBILE_PATTERN.matcher(loginName);
            if (m.matches()) {
                user = userMapper.getByMobile(loginName);
            }
        }
        //找到不用户
        if (user == null) {
            redisService.incr(UserKey.LOGIN_TIMES, loginName);
            throw new BusinessException(Result.USER_NOT_EXISTS);
        }
        //判断密码是否一致
        //密码错误，添加验证码标识
        if (!user.getPassword().equals(MD5Util.pwd(loginPassword, user.getSalt()))) {
            //更新错误次数
            redisService.incr(UserKey.LOGIN_TIMES, loginName);
            throw new BusinessException(Result.USER_PASSWORD_ERROR);
        }
        //写入redis数据
        redisService.delete(UserKey.LOGIN_TIMES, loginName);
        user.setToken(UUIDUtil.uuid());
        redisService.set(UserKey.TOKEN, user.getToken(), user);
        //添加cookie
        //Cookie cookie = new Cookie(User.COOK_NAME_TOKEN, user.getToken());
        //cookie.setPath("/");
        //cookie.setMaxAge(UserKey.TOKEN.getSeconds());
        //response.addCookie(cookie);
        //写入cookies
        return user;
    }

    public boolean updatePassword(User user,  String newPassword) {
        //更新
        user.setPassword(MD5Util.pwd(newPassword, user.getSalt()));
        userMapper.update(user.getId(), user.getPassword());
        //更新缓存
        redisService.set(UserKey.TOKEN, user.getToken(), user);
        return true;
    }
}
