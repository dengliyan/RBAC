package com.xyz.rbac.controller;

import com.xyz.rbac.data.domain.Category;
import com.xyz.rbac.data.domain.Department;
import com.xyz.rbac.result.JSONResult;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.service.DepartmentService;
import com.xyz.rbac.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/auth")
@ResponseBody
public class DeptUserController {

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/dept")
    public JSONResult get(@RequestParam("id") Integer id) {
        return JSONResult.success(departmentService.get(id));
    }

   @GetMapping("/dept/tree")
   public JSONResult tree() {
       List<Department> lists = departmentService.get();
       return JSONResult.success(TreeUtil.tree(lists));
   }

    @PostMapping("/dept/add")
    public JSONResult add(@RequestParam("name") String name,
                          @RequestParam("pid") Integer pid,
                          @RequestParam("rank") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date rank) {
        //验证
        if (StringUtils.isEmpty(name)) {
            return JSONResult.error(Result.ARGUMENTS_ERROR.format("部门名称不能为空"));
        }
        Department department = new Department();
        department.setName(name);
        department.setParentId(pid);
        department.setRank(rank.getTime());

        return JSONResult.result(departmentService.add(department));
    }


    @PostMapping("/dept/update")
    public JSONResult update(
                          @RequestParam("id") Integer id,
                          @RequestParam("name") String name,
                          @RequestParam("pid") Integer pid,
                          @RequestParam("rank") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date rank) {
        //验证
        if(id<=0){
            return JSONResult.error(Result.ARGUMENTS_ERROR.format("id不正确"));
        }
        if (StringUtils.isEmpty(name)) {
            return JSONResult.error(Result.ARGUMENTS_ERROR.format("部门名称不能为空"));
        }

        Department department = new Department();
        department.setId(id);
        department.setName(name);
        department.setParentId(pid);
        department.setRank(rank.getTime());
        if (departmentService.update(department)) {
            return JSONResult.SUCCESS;
        }
        return JSONResult.FAILURE;
    }


    @PostMapping("/dept/delete")
    public JSONResult delete(@RequestParam("id") Integer id) {
        return JSONResult.result(departmentService.delete(id, 0));
    }


    @GetMapping("/dept/structure")
    public JSONResult structure( ) {
        return JSONResult.success(departmentService.getDeptAndUserTree());
    }
}
