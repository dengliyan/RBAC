package com.xyz.rbac.controller;

import com.xyz.rbac.config.LoginRequired;
import com.xyz.rbac.data.domain.Department;
import com.xyz.rbac.data.domain.User;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.service.DepartmentService;
import com.xyz.rbac.util.UUIDUtil;
import com.xyz.rbac.vo.UserLoginVo;
import com.xyz.rbac.result.JSONResult;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.service.UserService;
import com.xyz.rbac.util.MD5Util;
import com.xyz.rbac.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    UserService userService;


    //登录
    @PostMapping("/user/login")
    @ResponseBody
    public JSONResult login(HttpServletResponse response, @Valid UserLoginVo model) {
        User user = userService.login(response, model.getName(), model.getPassword());
        if (user != null) {
            return JSONResult.success(user);
        }
        return JSONResult.error(Result.EXCUTE_ERROR);
    }

    //注册
    @PostMapping("/user/regist")
    @ResponseBody
    public JSONResult regist (@Valid UserVo model) {
        //验证
        //验证密码是否正确
        //
        if (StringUtils.isEmpty(model.getPassword()) || StringUtils.isEmpty(model.getPassword2())) {
            return JSONResult.error(Result.ARGUMENTS_ERROR.format("密码不能为空"));
        }
        if (!model.getPassword().equals(model.getPassword2())) {
            return JSONResult.error(Result.ARGUMENTS_ERROR.format("两次密码不一致"));
        }

        //转换
        User user = new User();
        user.setName(model.getName());
        user.setEmail(model.getEmail());
        user.setMobile(model.getMobile());
        user.setPassword(model.getPassword());
        user.setDeptId(model.getDept());
        //返回
        return JSONResult.result(userService.add(user));
    }


    //读取用户
    @GetMapping("/user")
    @ResponseBody
    public JSONResult get(@RequestParam("id") Integer id) {
        //
        User user = userService.get(id);
        UserVo vo = new UserVo();
        vo.setId(user.getId());
        vo.setName(user.getName());
        vo.setEmail(user.getEmail());
        vo.setMobile(user.getMobile());
        vo.setDept(user.getDeptId());
        return JSONResult.success(vo);
    }
}
