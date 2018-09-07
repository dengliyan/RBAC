package com.xyz.rbac.service;

import com.xyz.rbac.data.domain.User;
import com.xyz.rbac.data.mapper.UserMapper;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.validation.LoginNameConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public boolean login(HttpServletResponse response, String loginName, String loginPassword, String verifyCode) {
        //判断是否需要验证码
        //判断是否有验证码标识

        //判断用户是否正确
        User user = null;
        Matcher m = LoginNameConstraint.EMAIL_PATTERN.matcher(loginPassword);
        if (m.matches()) {
            user = userMapper.getByEmail(loginName);
        } else {
            m = LoginNameConstraint.MOBILE_PATTERN.matcher(loginPassword);
            if (m.matches()) {
                user = userMapper.getByMobile(loginName);
            }
        }
        //找到不用户
        if (user == null) {
            throw new BusinessException(Result.USER_NOT_EXISTS);
        }
        //判断密码是否一致
        //密码错误，添加验证码标识
        //
        //

        return true;
    }

    public boolean updatePassword(String token, String oldPassword, String newPassword) {
        return true;
    }
}
