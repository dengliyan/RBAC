package com.xyz.rbac.controller;

import com.xyz.rbac.data.domain.Role;
import com.xyz.rbac.result.JSONResult;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/auth")
@ResponseBody
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/role")
    public JSONResult get() {
        return JSONResult.success(roleService.get());
    }

    @PostMapping("/role/add")
    public JSONResult add(@RequestParam("name") String name,@RequestParam(name = "description" , required = false) String description) {
        //
        if (!StringUtils.isEmpty(name)) {
            return JSONResult.error(Result.ARGUMENTS_ERROR.format("名字不能不空"));
        }
        //判断名字是否存在
        List<Role> list = roleService.get();
        for (Role r : list) {
            if (r.getName() == name) {
                return JSONResult.error(Result.ARGUMENTS_ERROR.format("名字已存在"));
            }
        }
        //
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        return JSONResult.result(roleService.add(role));
    }


    @PostMapping("/role/update")
    public JSONResult update(
            @RequestParam("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam(name = "description" , required = false) String description) {
        if (!StringUtils.isEmpty(name)) {
            return JSONResult.error(Result.ARGUMENTS_ERROR.format("名字不能不空"));
        }
        //判断名字是否存在
        List<Role> list = roleService.get();
        for (Role r : list) {
            if (r.getName() == name && r.getId() != id) {
                return JSONResult.error(Result.ARGUMENTS_ERROR.format("名字已存在"));
            }
        }
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        return JSONResult.result(roleService.update(role));
    }


    @PostMapping("/role/delete")
    public JSONResult delete(@RequestParam("id") Integer id) {
        List<Role> list = roleService.get();
        for (Role r : list) {
            if (r.getId() == id) {
                return JSONResult.result(roleService.delete(id));
            }
        }
        return JSONResult.error(Result.ARGUMENTS_ERROR.format("记录不存在"));
    }

}
