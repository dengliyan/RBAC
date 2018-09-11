package com.xyz.rbac.controller;

import com.xyz.rbac.data.domain.Category;
import com.xyz.rbac.vo.CategoryVo;
import com.xyz.rbac.result.JSONResult;
import com.xyz.rbac.result.Result;
import com.xyz.rbac.service.CategoryService;
import com.xyz.rbac.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/api/auth")
public class CategoryController {




    @Autowired
    CategoryService categoryService;

    @PostMapping("/category/add")

    public JSONResult add(@Valid CategoryVo model) {
        Category category = new Category();
        category.setName(model.getName());
        category.setDescription(model.getDescription());
        category.setParentId(model.getPid());
        category.setRank(model.getRank().getTime());
        if(categoryService.add(category)){
            return JSONResult.SUCCESS;
        }
        return JSONResult.FAILURE;
    }

    @GetMapping("/category")
    public JSONResult get(@RequestParam("id") Integer id) {
        return JSONResult.success(categoryService.get(id));
    }

    @PostMapping("/category/update")
    public JSONResult update(@Valid CategoryVo model) {
        Category category = new Category();
        category.setId(model.getId());
        category.setName(model.getName());
        category.setDescription(model.getDescription());
        category.setParentId(model.getPid());
        category.setRank(model.getRank().getTime());
        if(categoryService.update(category)){
            return JSONResult.SUCCESS;
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
}
