package com.xyz.rbac.controller;

import com.xyz.rbac.model.LoginUser;
import com.xyz.rbac.result.JSONResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/login")
    public JSONResult login(@Valid LoginUser user) {

        return null;
    }
}
