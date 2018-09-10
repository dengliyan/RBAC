package com.xyz.rbac.controller;

import com.xyz.rbac.data.domain.Category;
import com.xyz.rbac.model.CategoryModel;
import com.xyz.rbac.result.JSONResult;
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
@RequestMapping("/api/auth/category")
public class CategoryController {




    @Autowired
    CategoryService categoryService;

    @PostMapping("/add")

    public JSONResult add(@Valid CategoryModel model) {
        Category category = new Category();
        category.setName(model.getName());
        category.setDescription(model.getDescription());
        category.setParentId(model.getPid());
        category.setRank(model.getRank().getTime());
        categoryService.add(category);
        return JSONResult.SUCCESS;
    }

    @PostMapping("/update")
    public JSONResult update(@Valid CategoryModel model) {

        return JSONResult.SUCCESS;
    }

    @PostMapping("/delete")
    public JSONResult delete(@RequestParam("id") Integer id) {

        return JSONResult.SUCCESS;
    }


    @GetMapping("/list")
    public JSONResult list() {
        List<Category> lists=categoryService.get();
        //List<Integer> lists2=categoryService.get2();
        return JSONResult.success(lists);
    }

    @GetMapping("/tree")
    public JSONResult tree(HttpServletRequest request,@RequestParam("id") Integer[] id) {
        List<Category> lists = categoryService.get();
        return JSONResult.success(TreeUtil.tree(lists, id));
    }
}
