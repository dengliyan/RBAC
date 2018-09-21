package com.xyz.rbac.controller;

import com.xyz.rbac.data.domain.Category;
import com.xyz.rbac.data.domain.CategoryPermission;
import com.xyz.rbac.exception.BusinessException;
import com.xyz.rbac.service.CategoryPermissionService;
import com.xyz.rbac.vo.ArrayVo;
import com.xyz.rbac.vo.CategoryPermissionVo;
import com.xyz.rbac.vo.CategoryVo;
import com.xyz.rbac.result.JSONResult;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.service.CategoryService;
import com.xyz.rbac.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/api/auth")
public class CategoryController {




    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryPermissionService categoryPermissionService;

    @PostMapping("/category/add")

    public JSONResult add(@Valid CategoryVo vo) {
        Category category = vo.convert();
        if (categoryService.add(category)) {
            return JSONResult.success(category);
        }
        return JSONResult.FAILURE;
    }

    @PostMapping("/category/update")
    public JSONResult update(@Valid CategoryVo vo) {
        Category category = vo.convert();
        if (categoryService.update(category)) {
            return JSONResult.success(category);
        }
        return JSONResult.FAILURE;
    }

    @PostMapping("/category/delete")
    public JSONResult delete(@RequestParam("id") Integer id) {
        if (categoryService.delete(id, 0)) {
            return JSONResult.SUCCESS;
        }
        return JSONResult.FAILURE;
    }



    @GetMapping("/category")
    public JSONResult get(@RequestParam("id") Integer id) {
        return JSONResult.success(categoryService.get(id));
    }





    /*
    @GetMapping("/list")
    public JSONResult list() {
        List<Category> lists=categoryService.get();
        return JSONResult.success(lists);
    }*/

    @GetMapping("/category/tree")
    public JSONResult tree(HttpServletRequest request,@RequestParam(name = "id",required=false) Integer[] id) {
        List<Category> lists = categoryService.get();
        return JSONResult.success(TreeUtil.tree(lists, id));
    }

    @GetMapping("/category/permission")
    public JSONResult getPermission(@RequestParam("category") Integer category) {
        Category model = categoryService.get(category);
        if (model == null) {
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("分类编号不存在"));
        }
        if (StringUtils.isEmpty(model.getPath())) {
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("不能设置权限"));
        }
        return JSONResult.success(categoryPermissionService.get(category));
    }


    @PostMapping("/category/permission/delete")
    public JSONResult getPermission(@RequestParam("category") Integer category, @RequestParam("id") Integer id) {
        Category model = categoryService.get(category);
        if (model == null) {
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("分类编号不存在"));
        }
        if (StringUtils.isEmpty(model.getPath())) {
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("不能设置权限"));
        }
        return JSONResult.success(categoryPermissionService.delete(id, category));
    }

    @PostMapping("/category/permission")
    public JSONResult postPermission(
            @RequestParam("category") Integer category,
            @RequestParam("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("path") String path,
            @RequestParam("rank") Integer rank
    ) {
        if (category <= 0) {
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("分类编号不存在"));
        }
        if (StringUtils.isEmpty(name)) {
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("名称不能为空"));
        }
        if (StringUtils.isEmpty(path)) {
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("路径不能为空"));
        }
        if (path.indexOf('/') != 0) {
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("路径必须以/开头"));
        }
        //验证当前是否能设置权限
        Category model = categoryService.get(category);
        if(model==null){
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("分类编号不存在"));
        }
        if(StringUtils.isEmpty(model.getPath())) {
            throw new BusinessException(Result.ARGUMENTS_ERROR.format("不能设置权限"));
        }
        //实体
        CategoryPermission permission=new CategoryPermission();
        permission.setId(id>0?id:0);
        permission.setName(name);
        permission.setPath(path);
        permission.setRank(rank);
        permission.setCategoryId(category);

        if(categoryPermissionService.set(permission)){
            return JSONResult.success(permission);
        }
        return JSONResult.FAILURE;
    }
}
