package com.xyz.rbac.controller;

import com.xyz.rbac.config.LoginRequired;
import com.xyz.rbac.data.domain.User;
import com.xyz.rbac.model.LoginUser;
import com.xyz.rbac.result.JSONResult;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.service.UserService;
import com.xyz.rbac.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public JSONResult login(HttpServletResponse response, @Valid LoginUser model) {
        User user = userService.login(response, model.getName(), model.getPassword());
        if (user != null) {
            return JSONResult.success(user);
        }
        return JSONResult.error(Result.EXCUTE_ERROR);
    }



    @LoginRequired
    @PostMapping("/change-pwd")
    @ResponseBody
    public JSONResult login(HttpServletResponse response, User user,
                            @RequestParam(name = "oldpwd", required = true) String oldPassword,
                            @RequestParam(name = "pwd1", required = true) String newPassword,
                            @RequestParam(name = "pwd2", required = true) String newPassword2) {

        if (!user.getPassword().equals(MD5Util.pwd(oldPassword, user.getSalt()))) {
            return JSONResult.error(Result.USER_ORIGNAL_PASSWORD_ERROR);
        }
        if (!StringUtils.isEmpty(newPassword) || !StringUtils.isEmpty(newPassword2)) {
            return JSONResult.error(Result.USER_PASSWORD_IS_NULL);
        }
        if (!newPassword.equals(newPassword2)) {
            return JSONResult.error(Result.USER_PASSWORD_NOT_EQUAL);
        }
        if (userService.updatePassword(user, newPassword)) {
            return JSONResult.SUCCESS;
        }
        return JSONResult.error(Result.EXCUTE_ERROR);
    }

}
