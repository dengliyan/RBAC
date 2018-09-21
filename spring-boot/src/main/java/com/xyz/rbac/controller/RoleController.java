package com.xyz.rbac.controller;

import com.alibaba.fastjson.JSON;
import com.xyz.rbac.data.domain.Category;
import com.xyz.rbac.data.domain.Role;
import com.xyz.rbac.result.JSONResult;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.service.RoleCategoryService;
import com.xyz.rbac.service.RoleService;
import com.xyz.rbac.vo.RoleVo;
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


    @Autowired
    RoleCategoryService roleCategoryService;


    @GetMapping("/role")
    public JSONResult get(@RequestParam(name = "id", required = false, defaultValue = "0") Integer id) {
        if (id > 0) {
            Role role = roleService.get(id,true);
            if (role == null) {
                return JSONResult.error(Result.DB_QUERY_NOT_EXISTS);
            }
            return JSONResult.success(role);
        }
        return JSONResult.success(roleService.get());
    }


    @PostMapping("/role/add")
    public JSONResult add(@RequestParam("name") String name,
                          @RequestParam(name = "description", required = false) String description,
                          @RequestParam("parent") Integer parent) {
        //
        if (StringUtils.isEmpty(name)) {
            return JSONResult.error(Result.ARGUMENTS_ERROR.format("名字不能不空"));
        }
        //判断名字是否存在
        List<RoleVo> list = roleService.get();
        for (RoleVo r : list) {
            if (r.getName() == name) {
                return JSONResult.error(Result.ARGUMENTS_ERROR.format("名字已存在"));
            }
        }
        //
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setParent(parent);

        if (roleService.add(role)) {
            return JSONResult.success(role);
        }
        return JSONResult.result(false);
    }


    @PostMapping("/role/update")
    public JSONResult update(
            @RequestParam("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "parent", required = false,defaultValue = "0") Integer parent) {
        if (StringUtils.isEmpty(name)) {
            return JSONResult.error(Result.ARGUMENTS_ERROR.format("名字不能不空"));
        }
        //判断名字是否存在
        List<RoleVo> list = roleService.get();
        for (RoleVo r : list) {
            if (r.getName() == name && r.getId() != id) {
                return JSONResult.error(Result.ARGUMENTS_ERROR.format("名字已存在"));
            }
        }
        //

        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        role.setParent(parent);
        if (roleService.update(role)) {
            return JSONResult.success(role);
        }
        return JSONResult.result(false);
    }


    @PostMapping("/role/delete")
    public JSONResult delete(@RequestParam("id") Integer id) {
        Role role = roleService.get(id,false);
        if (role!=null&&role.getId()==id) {
            return JSONResult.result(roleService.delete(role));
        }
        return JSONResult.error(Result.ARGUMENTS_ERROR.format("记录不存在"));
    }

    @GetMapping("/role/category")
    public JSONResult category(@RequestParam("id") Integer id) {
        //读取当前角色关联的
        return JSONResult.success(roleCategoryService.get(id));
    }

    @PostMapping("/role/category")
    public JSONResult setCategory(@RequestParam("id") Integer id,@RequestParam("data") Integer[] data) {
        //读取当前角色关联的
        return JSONResult.result(roleCategoryService.set(id, data));
    }
}
